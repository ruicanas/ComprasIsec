package pt.isec.tp_amov.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R
import pt.isec.tp_amov.adapters.HelpListAdapter
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.adapters.ShoppingListAdapter
import pt.isec.tp_amov.model.ModelView
import pt.isec.tp_amov.objects.Help
import pt.isec.tp_amov.objects.ShoppingList
import java.io.FileOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.reflect.Method

class MainActivity : AppCompatActivity(){
    private var archivedLists = ArrayList<ShoppingList>()
    private var allLists = ArrayList<ShoppingList>()
    private var hintList = ArrayList<Help>()

    private lateinit var adapter: ShoppingListAdapter
    private lateinit var helpAdapter: HelpListAdapter
    private lateinit var oldListView: View

    //This is for closing the dialog of reuse lists
    private var dialogHelp: AlertDialog? = null
    private var dialogOldList: AlertDialog? = null
    private var dialogNewList: AlertDialog? = null
    private var dialogRemove: AlertDialog? = null

    private lateinit var lvList: ListView
    private lateinit var archiveAdapter: ShoppingListAdapter

    private var listID = -1
    private var editText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Model.load(applicationContext)
        setContentView(R.layout.activity_main)
        initialConfigs()    //DEALS WITH CONFIGURATIONS --> DON'T FORGET TO EXPLAIN THIS
        versionControl()
        pressAddListBtn()
        prepareLists()
        prepareHelpAdapter()
        handleModelView(savedInstanceState)
    }

    override fun onDestroy() {
        Model.save(applicationContext)
        if (dialogHelp != null)
            if (dialogHelp!!.isShowing)
                dialogHelp!!.dismiss()
        if (dialogOldList != null)
            if (dialogOldList!!.isShowing)
                dialogOldList!!.dismiss()
        if (dialogNewList != null)
            if (dialogNewList!!.isShowing)
                dialogNewList!!.dismiss()
        if (dialogRemove != null)
            if (dialogRemove!!.isShowing)
                dialogRemove!!.dismiss()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (ModelView.dialogNewListShowing && editText != null) {
            if (!editText!!.text.isNullOrEmpty())
                ModelView.dialogText = editText!!.text.toString()
        }
        if (ModelView.dialogRemoveShowing) {
            if (listID != -1)
                ModelView.removeListID = listID
        }
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        updateListView()
        super.onResume()
    }

    override fun onPause() {
        Model.save(applicationContext)
        super.onPause()
    }

    override fun onStop() {
        Model.save(applicationContext)
        super.onStop()
    }

    //onCreate functions
    private fun initialConfigs() {
        if(Model.config.units.isEmpty()) {
            Model.config.units.add(getString(R.string.units))
            Model.config.units.add(getString(R.string.kg))
            Model.config.units.add(getString(R.string.grams))
            Model.config.units.add(getString(R.string.liter))
            Model.config.units.add(getString(R.string.boxes))
        }
        if(Model.config.categories.isEmpty()) {
            Model.config.categories.add(getString(R.string.fruit_vegetables))
            Model.config.categories.add(getString(R.string.starchy_food))
            Model.config.categories.add(getString(R.string.dairy))
            Model.config.categories.add(getString(R.string.protein))
            Model.config.categories.add(getString(R.string.fat))
        }
    }

    /**
     * This method will get the view that is holding the floating button
     * and then set a listener. This listener is needed to make the pop menu show up
     */
    private fun pressAddListBtn() {
        val fab: View = findViewById(R.id.add_fab)
        fab.setOnClickListener {
            createListDialog()
        }
    }

    private fun versionControl() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m: Method = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun prepareLists() {
        lvList = findViewById(R.id.lvMainList)
        onOpenList(lvList)
        adapter = ShoppingListAdapter(allLists)
        lvList.adapter = adapter

        //Prepare adapters
        archiveAdapter = ShoppingListAdapter(archivedLists)
    }

    private fun prepareHelpAdapter() {
        createHints()
        helpAdapter = HelpListAdapter(hintList)
    }

    private fun createHints() {
        hintList.add(Help(getString(R.string.add), getString(R.string.add_new_list)))
        hintList.add(Help(getString(R.string.hold), getString(R.string.opt_remove_list)))
        hintList.add(Help(getString(R.string.press), getString(R.string.edit_existing_list)))
        hintList.add(Help(getString(R.string.bin), getString(R.string.create_from_old)))
        hintList.add(Help(getString(R.string.config), getString(R.string.configure_units_cats)))
    }

    private fun handleModelView(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            if (ModelView.dialogNewListShowing) {
                createListDialog()
                editText!!.setText(ModelView.dialogText)
            }
            if (ModelView.dialogHelpShowing)
                helpDialog()
            if (ModelView.dialogOldListShowing)
                selectOldListsDialog()
            if (ModelView.dialogRemoveShowing) {
                removeListDlg(Model.getListById(ModelView.removeListID)!!)
            }
        }
    }

    //onResume function
    private fun updateListView() {
        allLists.clear()
        val slChosen = Model.allLists

        val empty = findViewById<TextView>(R.id.emptyPlaceholderList)
        //Check if there are any list. If not, show no list message
        if (slChosen.size == 0)
            empty.text = getString(R.string.no_lists)
        else
            empty.text = "" //Clear textView

        for(list in slChosen){
            allLists.add(list)
        }
        adapter.notifyDataSetChanged()
    }

    //Inflate menu --> menu_help
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_help, menu)
        return true
    }

    //Select items from a menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val intent = Intent(this, ConfigsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.helpList -> {
                helpDialog()
            }
            R.id.reuse_opt -> {
                selectOldListsDialog()
                return true
            }
        }
        return false
    }

    //Dialogs
    private fun createListDialog() { //Handles the new list dialog box
        ModelView.dialogNewListShowing = true
        val builder = AlertDialog.Builder(this) //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_ask_list_name, null)  //The layout we want to inflate
        editText = viewLayout.findViewById(R.id.listNameDlg)                  //Before entering the .sets of the builder we will save our textView

        builder.setView(viewLayout)
        builder.setCancelable(true)
        builder.setOnCancelListener { ModelView.dialogNewListShowing = false }
        builder.setPositiveButton(getString(R.string.create_list)) { _, _ ->
            ModelView.dialogNewListShowing = false
            val intent = Intent(this, ShowListActivity::class.java)
            val listName = editText!!.text.toString()     //After the user write a name, this name will be save on listName
            //Create the list at this moment
            intent.putExtra("listId", Model.addListByName(listName)) //And then, the listName, will be sent to the next activity.
            startActivity(intent)
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, _ ->
            ModelView.dialogNewListShowing = false
            dialog.dismiss()
        }
        dialogNewList = builder.show() //Capture dialog so that it can be dismissed later
    }

    private fun removeListDlg(sL: ShoppingList) { //Handles the remove list dialog box
        ModelView.dialogRemoveShowing = true
        listID = sL.id

        val builder = AlertDialog.Builder(this)     //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_remove_item, null)  //The layout we want to inflate

        val msg = StringBuilder()
        msg.append(getString(R.string.remove_item_description_dlg)).append(" ").append(sL.name).append("?")
        viewLayout.findViewById<TextView>(R.id.tvRemoveItemDlg).text = msg.toString()

        builder.setView(viewLayout)
        builder.setCancelable(true) //Can be canceled by touching outside the box
        builder.setOnCancelListener { ModelView.dialogRemoveShowing = false }
        builder.setPositiveButton(getString(R.string.delete_dlg)) { _, _ ->
            ModelView.dialogRemoveShowing = false
            Model.removeListData(sL)
            sL.removeAll()
            updateListView()
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, _ ->
            ModelView.dialogRemoveShowing = false
            dialog.dismiss()
        }
        dialogRemove = builder.show() //Capture dialog so that it can be dismissed later
    }

    @SuppressLint("InflateParams")  //Used to remove the warning given by inflater.inflate -> 'null' at root parameter
    private fun selectOldListsDialog() {
        ModelView.dialogOldListShowing = true
        val inflater = this.layoutInflater
        oldListView = inflater.inflate(R.layout.dialog_old_lists, null)  //The layout we want to inflate
        val oldLists = oldListView.findViewById<ListView>(R.id.archivedLists)
        oldLists.adapter = archiveAdapter
        val builder = AlertDialog.Builder(this)

        onSelectOldList(oldLists)
        updateArchive()

        builder.setView(oldListView)
        builder.setCancelable(true)
        builder.setOnCancelListener { ModelView.dialogOldListShowing = false }
        builder.setNegativeButton(getString(R.string.dialog_back)) { dialog, _ ->
            ModelView.dialogOldListShowing = false
            dialog.dismiss()
        }
        dialogOldList = builder.show() //Capture dialog so that it can be dismissed later
    }

    private fun updateArchive() { //Updates the view with the removed lists
        archivedLists.clear()
        val archive = Model.archivedLists

        val empty = oldListView.findViewById<TextView>(R.id.emptyPlaceholderOldList)
        //Check if there are any list. If not, show no list message
        if (archive.size == 0)
            empty.text = getString(R.string.no_lists)
        else
            empty.text = "" //Clear textView

        for(list in archive){
            archivedLists.add(list)
        }
        archiveAdapter.notifyDataSetChanged() //Notify adapter of list change
    }

    private fun helpDialog() { //Handles the Help Dialog box
        ModelView.dialogHelpShowing = true
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_help, null)  //The layout we want to inflate
        val helpList = view.findViewById<ListView>(R.id.helpList)
        helpList.adapter = helpAdapter

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setOnCancelListener { ModelView.dialogHelpShowing = false }
        builder.setNegativeButton(getString(R.string.dialog_back)) { dialog, _ ->
            ModelView.dialogHelpShowing = false
            dialog.dismiss()
        }
        dialogHelp = builder.show() //Capture dialog so that it can be dismissed later
    }

    //onItemClickListeners
    private fun onOpenList(listView: ListView) {
        listView.setOnItemClickListener { _, _, position, _ ->
            val sL: ShoppingList = adapter.getItem(position) as ShoppingList    //It was changed
            val intent = Intent(this, ShowListActivity::class.java)
            intent.putExtra("listId", sL.id)
            startActivity(intent)
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            val sL: ShoppingList = adapter.getItem(position) as ShoppingList    //It was changed
            removeListDlg(sL)
            true
        }
    }

    private fun onSelectOldList(list: ListView) {
        list.setOnItemClickListener { _, _, position, _ ->
            val sl: ShoppingList = archiveAdapter.getItem(position) as ShoppingList    //It was changed
            val intent = Intent(this, ShowListActivity::class.java)
            Model.recreateList(sl.id)
            intent.putExtra("listId", sl.id)
            dialogOldList!!.dismiss()
            startActivity(intent)
        }
    }
}
package pt.isec.tp_amov.activities

import android.app.AlertDialog
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
import pt.isec.tp_amov.objects.Help
import pt.isec.tp_amov.objects.ShoppingList
import java.lang.StringBuilder
import java.lang.reflect.Method

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private var archivedLists = ArrayList<ShoppingList>()
    private var allLists = ArrayList<ShoppingList>()
    private var hintList = ArrayList<Help>()
    lateinit var lvList: ListView
    private lateinit var adapter: ShoppingListAdapter
    lateinit var archiveAdapter: ShoppingListAdapter
    private lateinit var helpAdapter: HelpListAdapter
    private lateinit var oldListView: View
    //This is for closing the dialog of reuse lists
    lateinit var dlg: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pressAddListBtn()

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m: Method = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        lvList = findViewById(R.id.lvMainList)
        onOpenList(lvList)
        adapter = ShoppingListAdapter(allLists)
        lvList.adapter = adapter

        archiveAdapter = ShoppingListAdapter(archivedLists)

        createHints()
        helpAdapter = HelpListAdapter(hintList)
    }

    override fun onResume() {
        super.onResume()
        updateListView()
    }

    private fun updateListView() {
        allLists.clear()
        val slChosen = Model.getAllLists()

        var empty = findViewById<TextView>(R.id.emptyPlaceholderList)
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

    private fun updateArchive() {
        archivedLists.clear()
        val archive = Model.getOldLists()

        var empty = oldListView.findViewById<TextView>(R.id.emptyPlaceholderOldList)
        //Check if there are any list. If not, show no list message
        if (archive.size == 0)
            empty.text = getString(R.string.no_lists)
        else
            empty.text = "" //Clear textView

        for(list in archive){
            archivedLists.add(list)
        }
        archiveAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_help, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        helpDialog()
        return super.onOptionsItemSelected(item)
    }

    /**
     * This method will show the popup menu, and will set a listener
     * in order to be able to receive all the clicks
     */
    private fun showPopup(v: View) {
        PopupMenu(this, v).apply{
            setOnMenuItemClickListener(this@MainActivity)
            inflate(R.menu.menu_opt_list)
            show()
        }
    }

    /**
     * This method will get the view that is holding the floating button
     * and then set a listener. This listener is needed to make the pop menu show up
     */
    private fun pressAddListBtn() {
        val fab: View = findViewById(R.id.add_fab)
        fab.setOnClickListener {
            showPopup(fab)
        }
    }

    /**
     * This method will receive the clicks that are going to be made on options
     * shown by the floating button on the main menu
     */
    override fun onMenuItemClick(item: MenuItem): Boolean {
        if(item.itemId == R.id.new_opt){
            createDialog()
            return true
        }
        if (item.itemId == R.id.reuse_opt) {
            selectOldLists()
            return true
        }
        return false
    }

    private fun createDialog(){
        val builder = AlertDialog.Builder(this)     //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_ask_list_name, null)  //The layout we want to inflate
        val editText = viewLayout.findViewById<EditText>(R.id.listNameDlg)                  //Before entering the .sets of the builder we will save our textView
        builder.setView(viewLayout)
        builder.setPositiveButton(getString(R.string.create_list)) { dialog, id ->
            val intent = Intent(this, ShowListActivity::class.java)
            val listName = editText.text.toString()     //After the user write a name, this name will be save on listName
            //Create the list at this moment
            intent.putExtra("listId", Model.addList(listName)) //And then, the listName, will be sent to the next activity.
            startActivity(intent)
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    private fun removeListDlg(sL: ShoppingList){
        val builder = AlertDialog.Builder(this)     //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_remove_item, null)  //The layout we want to inflate

        val msg = StringBuilder()
        msg.append(getString(R.string.remove_item_description_dlg)).append(" ").append(sL.name).append("?")
        viewLayout.findViewById<TextView>(R.id.tvRemoveItemDlg).text = msg.toString()

        builder.setView(viewLayout)
        builder.setPositiveButton(getString(R.string.delete_dlg)) {dialog, id ->
            Model.removeListDataBase(sL)
            sL.removeAll()
            updateListView()
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    private fun selectOldLists() {
        val inflater = this.layoutInflater
        oldListView = inflater.inflate(R.layout.dialog_old_lists, null)  //The layout we want to inflate
        val oldLists = oldListView.findViewById<ListView>(R.id.archivedLists)
        oldLists.adapter = archiveAdapter

        val builder = AlertDialog.Builder(this)

        onSelectOldList(oldLists)
        updateArchive()

        builder.setView(oldListView)
        builder.setCancelable(true)
        builder.setNegativeButton(getString(R.string.dialog_back)) { dialog, id -> dialog.dismiss() }
        dlg = builder.show()
    }

    private fun onSelectOldList(list: ListView) {
        list.setOnItemClickListener { parent, view, position, id ->
            val sl: ShoppingList = archiveAdapter.getItem(position) as ShoppingList    //It was changed
            val intent = Intent(this, ShowListActivity::class.java)
            Model.recreateList(sl.id)
            intent.putExtra("listId", sl.id)
            dlg.dismiss()
            startActivity(intent)
        }
    }

    private fun helpDialog() {
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_help, null)  //The layout we want to inflate
        var helpList = view.findViewById<ListView>(R.id.helpList)
        helpList.adapter = helpAdapter

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setNegativeButton(getString(R.string.dialog_back)) { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    private fun onOpenList(listView: ListView) {
        listView.setOnItemClickListener { parent, view, position, id ->
            val sL: ShoppingList = adapter.getItem(position) as ShoppingList    //It was changed
            val intent = Intent(this, ShowListActivity::class.java)
            intent.putExtra("listId", sL.id)
            startActivity(intent)
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            val sL: ShoppingList = adapter.getItem(position) as ShoppingList    //It was changed
            removeListDlg(sL)
            true
        }
    }

    private fun createHints() {
        hintList.add(Help(getString(R.string.plus), getString(R.string.add_new_list)))
        hintList.add(Help(getString(R.string.hold), getString(R.string.opt_remove_list)))
        hintList.add(Help(getString(R.string.press), getString(R.string.edit_existing_list)))
    }
}
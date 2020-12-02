package pt.isec.tp_amov.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.adapters.ShoppingListAdapter
import pt.isec.tp_amov.objects.ShoppingList
import java.lang.reflect.Method

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private var allLists = ArrayList<ShoppingList>()
    lateinit var lvList: ListView
    lateinit var adapter: ShoppingListAdapter

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
    }

    private fun updateListView() {
        allLists.clear()
        val slChosen = Model.getAllLists()

        var empty = findViewById<TextView>(R.id.emptyPlaceholderList)
        if (slChosen.size == 0)
            empty.text = getString(R.string.no_lists)
        else
            empty.text = ""

        for(list in slChosen){
            allLists.add(list)
        }
        adapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        updateListView()
    }


    /**
     * This method will show the popup menu, and will set a listener
     * in order to be able to receive all the clicks
     */
    fun showPopup(v: View) {
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
    fun pressAddListBtn() {
        val fab: View = findViewById(R.id.add_fab)
        fab.setOnClickListener {
            showPopup(fab)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_help, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            val intent = Intent(this, ConfigsActivity::class.java)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
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
        viewLayout.findViewById<TextView>(R.id.tvRemoveItemDlg).text = "Remove ${sL.name}?"
        builder.setView(viewLayout)
        builder.setPositiveButton(getString(R.string.delete_dlg)) {dialog, id ->
            Model.removeListDataBase(sL)
            sL.removeAll()
            updateListView()
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    fun onOpenList(listView: ListView) {
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
}
package pt.isec.tp_amov.activities

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.adapters.ShoppingListAdapter
import pt.isec.tp_amov.objects.ShoppingList

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    private var allLists = ArrayList<ShoppingList>()
    lateinit var lvList: ListView
    lateinit var adapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pressAddListBtn()

        lvList = findViewById(R.id.lvMainList)
        onOpenList(lvList)
        adapter = ShoppingListAdapter(allLists)
        lvList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        allLists.clear()
        val slChosen = Model.getAllLists()
        for(list in slChosen){
            allLists.add(list)
        }
        adapter.notifyDataSetChanged()
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
        builder.setPositiveButton("Create List") { dialog, id ->
            val intent = Intent(this, ShowListActivity::class.java)
            val listName = editText.text.toString()     //After the user write a name, this name will be save on listName
            //Create the list at this moment
            intent.putExtra("id", Model.addList(listName)) //And then, the listName, will be sent to the next activity.
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    fun onOpenList(listView: ListView) {
        listView.setOnItemClickListener { parent, view, position, id ->
            val sL = adapter.getItem(position)
            val intent = Intent(this, ShowListActivity::class.java)
            intent.putExtra("id", sL.id)
            startActivity(intent)
        }
    }
}
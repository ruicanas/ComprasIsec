package pt.isec.tp_amov.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pressAddListBtn()

//        findViewById<TextView>(R.id.lvMainList).apply {
//
//        }
    }

    override fun onResume() {
        super.onResume()
//        findViewById<TextView>(R.id.lvMainList).apply {
//
//        }
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

    fun createDialog(){
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_ask_list_name, null)
        val editText = viewLayout.findViewById<EditText>(R.id.listNameDlg)
        builder.setView(viewLayout)
        builder.setPositiveButton("Create List") { dialog, id ->
            val intent = Intent(this, ShowListActivity::class.java)
            val listName = editText.text.toString()
            intent.putExtra("listName", listName)
            startActivity(intent)
        }
        builder.setNegativeButton("Cancel") { dialog, id -> dialog.dismiss() }
        builder.show()
    }
}
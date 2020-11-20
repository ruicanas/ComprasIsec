package pt.isec.tp_amov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu

class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pressAddListBtn()
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
            val intent = Intent(this, showListActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }


}
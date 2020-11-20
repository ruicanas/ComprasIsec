package pt.isec.tp_amov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu

class showListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options,menu)
        return true
    }
}
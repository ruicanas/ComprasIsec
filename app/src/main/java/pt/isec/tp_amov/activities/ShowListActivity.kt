package pt.isec.tp_amov.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import pt.isec.tp_amov.R
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.adapters.ProductListAdapter

class ShowListActivity : AppCompatActivity() {
    private var productList = ArrayList<Product>()
    lateinit var listName: String
    lateinit var lvList: ListView
    lateinit var adapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)
        listName = intent.getStringExtra("listName")!!
        supportActionBar?.title = listName
        //Create a list on the Model
        if(Model.getList(listName) == null){
            Model.addList(listName)
        }
        lvList = findViewById(R.id.lvProductList)
        adapter = ProductListAdapter(productList)
        lvList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        productList.clear()
        //Add the elements to the vector
        val slChosen = Model.getList(listName)?.getList()
        if(slChosen != null) {
            for(prod in slChosen){
                productList.add(prod)
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.addProd){
            val intent = Intent(this, ManageProductActivity::class.java)
            intent.putExtra("listName", listName)
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
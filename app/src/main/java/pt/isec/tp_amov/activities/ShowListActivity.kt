package pt.isec.tp_amov.activities

import android.content.ClipData
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import pt.isec.tp_amov.R
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.adapters.ProductListAdapter
import pt.isec.tp_amov.objects.ShoppingList

class ShowListActivity : AppCompatActivity() {
    private var productList = ArrayList<Product>()
    lateinit var lvList: ListView
    lateinit var adapter: ProductListAdapter
    var id = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)
        lvList = findViewById(R.id.lvProductList)
        onOpenProduct(lvList)

        id = intent.getIntExtra("listId", -1)
        //Verify if the ID is valid
        if(id == -1){
            finish()
        }
        supportActionBar?.title = Model.getListById(id)?.name

        //Create a list on the Model
        lvList = findViewById(R.id.lvProductList)
        adapter = ProductListAdapter(productList)
        lvList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        productList.clear()
        //Add the elements to the vector
        val slChosen = Model.getListById(id)?.productList
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
            intent.putExtra("listId", id)
            intent.putExtra("type", "create")
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun onOpenProduct(listView: ListView) {
        listView.setOnItemClickListener { parent, view, position, id ->
            val prod: Product = adapter.getItem(position) as Product    //It was changed
            val intent = Intent(this, ManageProductActivity::class.java)
            intent.putExtra("listId", this.id)
            intent.putExtra("productId", prod.id)
            intent.putExtra("type", "edit")
            startActivity(intent)
        }
    }
}
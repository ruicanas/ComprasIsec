package pt.isec.tp_amov.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import pt.isec.tp_amov.R
import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.objects.ProductListAdapter

class ShowListActivity : AppCompatActivity() {
    var productList = ArrayList<Product>()
    lateinit var list: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        list = findViewById(R.id.productList)
        list.adapter = ProductListAdapter(productList)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.addProd){
            val intent = Intent(this, ManageProductActivity::class.java)
            startActivityForResult(intent, 10) //todo - create file to store all resultCodes
            return true
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 10) {
//            if (resultCode == RESULT_OK) {
//                val product = data!!.getSerializableExtra("Product") as Product
//                productList.add(product)
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
}
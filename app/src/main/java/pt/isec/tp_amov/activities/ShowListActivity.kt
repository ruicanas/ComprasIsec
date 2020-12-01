package pt.isec.tp_amov.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import pt.isec.tp_amov.R
import pt.isec.tp_amov.adapters.HelpListAdapter
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.objects.Product
import pt.isec.tp_amov.adapters.ProductListAdapter
import pt.isec.tp_amov.comparators.ComparatorBought
import pt.isec.tp_amov.comparators.ComparatorCategory
import pt.isec.tp_amov.comparators.ComparatorName
import pt.isec.tp_amov.objects.Help
import java.lang.StringBuilder
import kotlin.collections.ArrayList

class ShowListActivity : AppCompatActivity() {
    private var productList = ArrayList<Product>()
    private var hintList = ArrayList<Help>()
    lateinit var lvList: ListView
    lateinit var adapter: ProductListAdapter
    lateinit var helpAdapter: HelpListAdapter
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

        if (Model.getListById(id)?.name.isNullOrEmpty()) {
            supportActionBar?.title = getString(R.string.default_list_name)
            Model.setDefaultListName(id, getString(R.string.default_list_name))
        }
        else
            supportActionBar?.title = Model.getListById(id)?.name

        //Create a list on the Model
        lvList = findViewById(R.id.lvProductList)
        adapter = ProductListAdapter(productList)
        lvList.adapter = adapter

        createHints()
        helpAdapter = HelpListAdapter(hintList)
    }

    override fun onResume() {
        super.onResume()
        updateListView()
    }

    private fun updateListView() {
        productList.clear()
        //Add the elements to the vector
        val slChosen = Model.getListById(id)?.productList
        if(slChosen != null) {
            var empty = findViewById<TextView>(R.id.emptyPlaceholderProd)
            //Check if there are any products. If not, show no products message
            if (slChosen.size == 0)
                empty.text = getString(R.string.no_products)
            else
                empty.text = "" //Clear textView

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
        when (item.itemId) {
            R.id.searchProd ->{
                val intent = Intent(this, SearchProductActivity::class.java)
                intent.putExtra("listId", id)
                startActivity(intent)
                return true
            }
            R.id.helpProd -> {
                helpDialog()
            }
            R.id.addProd -> {
                val intent = Intent(this, ManageProductActivity::class.java)
                intent.putExtra("listId", id)
                intent.putExtra("type", "create")
                startActivity(intent)
                return true
            }
            R.id.orderName -> {
                orderByName()
                return true
            }
            R.id.orderProdsBought -> {
                orderByProdsBought()
                return true
            }
            else -> {
                orderByCategory()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun orderByCategory() {
        productList.sortWith(ComparatorCategory())
        adapter.notifyDataSetChanged()
    }

    private fun orderByProdsBought() {
        productList.sortWith(ComparatorBought())
        adapter.notifyDataSetChanged()
    }

    private fun orderByName() {
        productList.sortWith(ComparatorName())
        adapter.notifyDataSetChanged()
    }

    private fun removeItemDlg(prod: Product){
        val builder = AlertDialog.Builder(this)     //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_remove_item, null)  //The layout we want to inflate

        val msg = StringBuilder()
        msg.append(getString(R.string.remove_item_description_dlg)).append(" ").append(prod.name).append("?")
        viewLayout.findViewById<TextView>(R.id.tvRemoveItemDlg).text = msg.toString()

        builder.setView(viewLayout)
        builder.setPositiveButton(getString(R.string.delete_dlg)) {dialog, id ->
            Model.removeDataBase(prod.name, prod.category, prod.price)
            val slChosen = Model.getListById(this.id)
            slChosen?.removeProduct(prod.id)
            updateListView()
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, id -> dialog.dismiss() }
        builder.show()
    }

    private fun onOpenProduct(listView: ListView) {
        listView.setOnItemClickListener { parent, view, position, id ->
            val prod: Product = adapter.getItem(position) as Product    //It was changed
            val intent = Intent(this, ManageProductActivity::class.java)
            intent.putExtra("listId", this.id)
            intent.putExtra("productId", prod.id)
            intent.putExtra("type", "edit")
            startActivity(intent)
        }
        listView.setOnItemLongClickListener { parent, view, position, id ->
            val prod: Product = adapter.getItem(position) as Product    //It was changed
            removeItemDlg(prod)
            true
        }
    }

    fun onCheckBox(view: View) {
        val cbView: CheckBox = view.findViewById(R.id.cbItems)
        val pos: Int = cbView.tag as Int
        val prod: Product = adapter.getItem(pos) as Product
        prod.prodChecked = cbView.isChecked
    }

    private fun createHints() {
        hintList.add(Help(getString(R.string.plus), getString(R.string.add_new_prod)))
        hintList.add(Help(getString(R.string.hold), getString(R.string.remove_prod)))
        hintList.add(Help(getString(R.string.press), getString(R.string.edit_prod)))
        hintList.add(Help(getString(R.string.three_dots), getString(R.string.order_prod_list)))
        hintList.add(Help(getString(R.string.checkbox), getString(R.string.check_bought)))
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
}
package pt.isec.tp_amov.activities

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
import pt.isec.tp_amov.model.ModelView
import pt.isec.tp_amov.objects.Help
import java.lang.StringBuilder
import kotlin.collections.ArrayList

class ShowListActivity : AppCompatActivity() {
    private var productList = ArrayList<Product>()
    private var hintList = ArrayList<Help>()

    private var listId = -1
    private var prodId = -1

    private lateinit var lvList: ListView
    private lateinit var adapter: ProductListAdapter
    private lateinit var helpAdapter: HelpListAdapter

    private var dialogHelp: AlertDialog? = null
    private var dialogRemove: AlertDialog? = null

    //onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)
        verifyList()
        handlesTitles()
        prepareLists()
        onOpenProduct()
        handlesModelView(savedInstanceState)
    }
    private fun verifyList() { //Verifies the listID
        listId = intent.getIntExtra("listId", -1)
        if(listId == -1){
            finish()
        }
    }
    private fun handlesTitles() { //Set the title of new list
        if (Model.getListById(listId)?.name.isNullOrEmpty()) { //In case user didn't specify a title
            supportActionBar?.title = getString(R.string.default_list_name)
            Model.setDefaultListName(listId, getString(R.string.default_list_name))
        } else {
            supportActionBar?.title = Model.getListById(listId)?.name
        }
    }
    private fun prepareLists() { //Prepares all the list. Created all the adapters
        lvList = findViewById(R.id.lvProductList)
        //Create a list on the Model
        lvList = findViewById(R.id.lvProductList)
        adapter = ProductListAdapter(productList)
        lvList.adapter = adapter

        //Helper list
        createHints() //creates the arrayList of hints
        helpAdapter = HelpListAdapter(hintList)
    }
    private fun createHints() { //Add help strings to array list
        hintList.add(Help(getString(R.string.plus), getString(R.string.add_new_prod)))
        hintList.add(Help(getString(R.string.hold), getString(R.string.remove_prod)))
        hintList.add(Help(getString(R.string.press), getString(R.string.edit_prod)))
        hintList.add(Help(getString(R.string.three_dots), getString(R.string.order_prod_list)))
        hintList.add(Help(getString(R.string.checkbox), getString(R.string.check_bought)))
        hintList.add(Help(getString(R.string.magnifier), getString(R.string.search_for_objects)))
    }
    private fun onOpenProduct() {
        lvList.setOnItemClickListener { _, _, position, _ -> //click on product
            val prod: Product = adapter.getItem(position) as Product    //It was changed
            val intent = Intent(this, ManageProductActivity::class.java)
            intent.putExtra("listId", this.listId)
            intent.putExtra("productId", prod.id)
            intent.putExtra("type", "edit")
            startActivity(intent)
        }
        lvList.setOnItemLongClickListener { _, _, position, _ -> //hold to remove product
            val prod: Product = adapter.getItem(position) as Product    //It was changed
            removeItemDlg(prod)
            true
        }
    }
    private fun handlesModelView(savedInstanceState: Bundle?) { //handles the saveInstanceState part of the onCreate
        if (savedInstanceState != null) {
            if (ModelView.dialogHelpShowingSL)
                helpDialog()
            if (ModelView.dialogRemoveShowingSL)
                removeItemDlg(Model.getProdById(ModelView.removeProdID, listId)!!)
        }
    }

    override fun onStop() { //For when the app is forcefully closed
        Model.save(applicationContext)
        super.onStop()
    }

    //onResume
    override fun onResume() {
        super.onResume()
        updateListView() //update the list view with all products
        when (ModelView.currentFilter) { //order by last filter
            1 -> orderByName()
            2 -> orderByProdsBought()
            3 -> orderByCategory()
        }
    }
    private fun updateListView() {
        productList.clear()
        //Add the elements to the vector
        val slChosen = Model.getListById(listId)?.productList
        if(slChosen != null) {
            val empty = findViewById<TextView>(R.id.emptyPlaceholderProd)
            //Check if there are any products. If not, show no products message
            if (slChosen.size == 0) //if there are no products, show no products TextView
                empty.text = getString(R.string.no_products)
            else
                empty.text = "" //Clear textView

            for(prod in slChosen){ //list all products
                productList.add(prod)
            }
            adapter.notifyDataSetChanged()
        }
    }

    //Remaining
    override fun onDestroy() {
        Model.save(applicationContext)
        if (dialogHelp != null) //This is meant to close the dialog if it is opened preventing WindowLeaked
            if (dialogHelp!!.isShowing)
                dialogHelp!!.dismiss()
        if (dialogRemove != null)
            if (dialogRemove!!.isShowing)
                dialogRemove!!.dismiss()
        super.onDestroy()
    }
    override fun onSaveInstanceState(outState: Bundle) {
        if (ModelView.dialogRemoveShowingSL) {
            if (prodId != -1)
                ModelView.removeProdID = prodId
        }
        super.onSaveInstanceState(outState)
    }

    //Create Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options,menu)
        return true
    }

    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean {
        menu.close()
        return true
    }

    //Selected items from menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.searchProd ->{ //search for already existing products
                val intent = Intent(this, SearchProductActivity::class.java)
                intent.putExtra("listId", listId)
                startActivity(intent)
                return true
            }
            R.id.helpProd -> { //ask for help - will open dialog
                helpDialog()
            }
            R.id.addProd -> { //add new product
                val intent = Intent(this, ManageProductActivity::class.java)
                intent.putExtra("listId", listId)
                intent.putExtra("type", "create")
                startActivity(intent)
                return true
            }
            R.id.orderName -> {
                ModelView.currentFilter = 1
                orderByName()
                return true
            }
            R.id.orderProdsBought -> {
                ModelView.currentFilter = 2
                orderByProdsBought()
                return true
            }
            else -> {
                ModelView.currentFilter = 3
                orderByCategory()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    private fun orderByCategory() { //order the list by category
        productList.sortWith(ComparatorCategory())
        adapter.notifyDataSetChanged()
    }
    private fun orderByProdsBought() { //order the list by purchased
        productList.sortWith(ComparatorBought())
        adapter.notifyDataSetChanged()
    }
    private fun orderByName() { //order the list by name
        productList.sortWith(ComparatorName())
        adapter.notifyDataSetChanged()
    }

    //Dialogs
    private fun removeItemDlg(prod: Product){ //remove dialog that shows on long press of product
        ModelView.dialogRemoveShowingSL = true
        prodId = prod.id

        val builder = AlertDialog.Builder(this)     //Construct the builder
        val inflater = this.layoutInflater
        val viewLayout : View = inflater.inflate(R.layout.dialog_remove_item, null)  //The layout we want to inflate

        val msg = StringBuilder()
        msg.append(getString(R.string.remove_item_description_dlg)).append(" ").append(prod.name).append("?")
        viewLayout.findViewById<TextView>(R.id.tvRemoveItemDlg).text = msg.toString()

        builder.setView(viewLayout)
        builder.setCancelable(true) //can be canceled by touching outside
        builder.setOnCancelListener { ModelView.dialogRemoveShowingSL = false }
        builder.setPositiveButton(getString(R.string.delete_dlg)) { _, _ ->
            ModelView.dialogRemoveShowingSL = false
            Model.removeProdData(prod.name, prod.category, prod.price)
            val slChosen = Model.getListById(this.listId)
            slChosen?.removeProduct(prod.id)
            updateListView()
        }
        builder.setNegativeButton(getString(R.string.cancel_list)) { dialog, _ ->
            dialog.dismiss()
            ModelView.dialogRemoveShowingSL = false
        }
        dialogRemove = builder.show() //captures dialog to dismiss later if needed
    }
    private fun helpDialog() { //shows help dialog
        ModelView.dialogHelpShowingSL = true
        val inflater = this.layoutInflater
        val view: View = inflater.inflate(R.layout.dialog_help, null)  //The layout we want to inflate
        val helpList = view.findViewById<ListView>(R.id.helpList)
        helpList.adapter = helpAdapter

        val builder = AlertDialog.Builder(this)
        builder.setView(view)
        builder.setCancelable(true)
        builder.setOnCancelListener { ModelView.dialogHelpShowingSL = false }
        builder.setNegativeButton(getString(R.string.dialog_back)) { dialog, _ ->
            ModelView.dialogHelpShowingSL = false
            dialog.dismiss()
        }
        dialogHelp = builder.show() //captures dialog to dismiss later if needed
    }

    //When the checkbox gets clicked
    fun onCheckBox(view: View) {
        val cbView: CheckBox = view.findViewById(R.id.cbItems)
        val pos: Int = cbView.tag as Int
        val prod: Product = adapter.getItem(pos) as Product
        prod.prodChecked = cbView.isChecked
    }
}
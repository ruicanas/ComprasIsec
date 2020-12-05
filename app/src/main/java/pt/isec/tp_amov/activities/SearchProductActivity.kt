package pt.isec.tp_amov.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import pt.isec.tp_amov.R
import pt.isec.tp_amov.adapters.SearchProductAdapter
import pt.isec.tp_amov.comparators.ComparatorCategoryData
import pt.isec.tp_amov.comparators.ComparatorNameData
import pt.isec.tp_amov.interfaces.ItemClickListenerInterface
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.objects.DataProduct

class SearchProductActivity : AppCompatActivity(), ItemClickListenerInterface<DataProduct> {
    private val dataList = ArrayList<DataProduct>(Model.allProducts)
    private var listId = -1
    private lateinit var rvList: RecyclerView
    private lateinit var adapter: SearchProductAdapter
    private lateinit var lM: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)
        getIntents()
        verifyListId()
        prepareList()
    }

    private fun getIntents() {
        listId = intent.getIntExtra("listId", -1)
    }
    private fun verifyListId() {
        if(listId == -1){
            finish()
        }
    }
    private fun prepareList(){
        rvList = findViewById(R.id.rvDataSearch)
        adapter = SearchProductAdapter(dataList, this)
        lM = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        rvList.adapter = adapter
        rvList.layoutManager = lM
    }

    //Deals with the clicks on an item
    override fun onItemClickListener(data: DataProduct) {
        val intent = Intent(this, ManageProductActivity::class.java)
        intent.putExtra("listId", listId)
        intent.putExtra("dataName", data.name)
        intent.putExtra("dataCat", data.category)
        intent.putExtra("type", "reuseData")
        startActivity(intent)
        finish()
    }

    //Create the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_prods, menu)
        return true
    }

    //Items selected on the menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.orderName){
            orderByName()
        }
        else{
            orderByCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    //Groupings
    private fun orderByName() {
        dataList.sortWith(ComparatorNameData())
        adapter.notifyDataSetChanged()
    }
    private fun orderByCategory() {
        dataList.sortWith(ComparatorCategoryData())
        adapter.notifyDataSetChanged()
    }
}
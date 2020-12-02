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

class SearchProductActivity : AppCompatActivity(), ItemClickListenerInterface {
    private val dataList = ArrayList<DataProduct>(Model.allProducts)
    private var listId = -1
    lateinit var rvList: RecyclerView
    lateinit var adapter: SearchProductAdapter
    lateinit var lM: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)
        listId = intent.getIntExtra("listId", -1)
        if(listId == -1){
            finish()
        }

        rvList = findViewById(R.id.rvList)
        adapter = SearchProductAdapter(dataList, this)
        lM = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        rvList.adapter = adapter
        rvList.layoutManager = lM
    }

    //Deals with the clicks on item
    override fun onItemClickListener(data: DataProduct) {
        val intent = Intent(this, ManageProductActivity::class.java)
        intent.putExtra("listId", listId)
        intent.putExtra("dataName", data.name)
        intent.putExtra("dataCat", data.category.toString())
        intent.putExtra("type", "reuseData")
        startActivity(intent)
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_prods, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.orderName){
            orderByName()
        }
        else{
            orderByCategory()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun orderByName() {
        dataList.sortWith(ComparatorNameData())
        adapter.notifyDataSetChanged()
    }

    private fun orderByCategory() {
        dataList.sortWith(ComparatorCategoryData())
        adapter.notifyDataSetChanged()
    }
}
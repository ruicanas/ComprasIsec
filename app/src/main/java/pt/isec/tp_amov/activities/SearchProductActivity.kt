package pt.isec.tp_amov.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import pt.isec.tp_amov.R
import pt.isec.tp_amov.adapters.ProductListAdapter
import pt.isec.tp_amov.adapters.SearchProductAdapter
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.objects.Categories
import pt.isec.tp_amov.objects.DataProduct

class SearchProductActivity : AppCompatActivity() {
    private val dataList = ArrayList<DataProduct>(Model.allProducts)
    lateinit var rvList: RecyclerView
    lateinit var adapter: SearchProductAdapter
    lateinit var lM: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_product)

        rvList = findViewById(R.id.rvList)
        adapter = SearchProductAdapter(dataList)
        lM = StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL)
        rvList.adapter = adapter
        rvList.layoutManager = lM
    }
}
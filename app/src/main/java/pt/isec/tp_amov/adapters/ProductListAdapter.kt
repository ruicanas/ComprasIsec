package pt.isec.tp_amov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import pt.isec.tp_amov.R
import pt.isec.tp_amov.model.Model
import pt.isec.tp_amov.objects.Product
import kotlin.collections.ArrayList

class ProductListAdapter(var products: ArrayList<Product>): BaseAdapter() {
    override fun getCount(): Int {
        return products.size
    }

    override fun getItem(position: Int): Any {
        return products[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.listview_product_list, parent, false)

        val name = view.findViewById<TextView>(R.id.productName)
        name.text = products[position].name

        val brand = view.findViewById<TextView>(R.id.productBrand)
        brand.text = products[position].brand

        val amount = view.findViewById<TextView>(R.id.productAmount)
        amount.text = products[position].amount.toString()

        val price = view.findViewById<TextView>(R.id.productPrice)
        if(products[position].price > 0){
            price.text = products[position].price.toString()
        }
        else{
            price.text = "-"
        }

        val category = view.findViewById<TextView>(R.id.productCategory)
        category.text = products[position].category

        val unit = view.findViewById<TextView>(R.id.productUnit)
        unit.text = products[position].units

        val currency = view.findViewById<TextView>(R.id.currency)
        currency.text = parent.context.getString(R.string.currency)

        val lastPrices = view.findViewById<TextView>(R.id.productLastPrices)
        lastPrices.text = Model.getPrices(products[position])

        val cBox = view.findViewById<CheckBox>(R.id.cbItems)
        cBox.tag = Integer.valueOf(position);               // set the tag so we can identify the correct row in the listener
        cBox.isChecked = products[position].prodChecked     // set the status as we stored it~

        return view
    }
}
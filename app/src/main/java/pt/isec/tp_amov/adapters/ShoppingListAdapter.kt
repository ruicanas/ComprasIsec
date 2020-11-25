package pt.isec.tp_amov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import pt.isec.tp_amov.R
import pt.isec.tp_amov.objects.ShoppingList

class ShoppingListAdapter(var shoppingLists: ArrayList<ShoppingList>): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent!!.context)  //Gabriel?
        val view = layoutInflater.inflate(R.layout.main_list_layout, parent, false)

        val name = view.findViewById<TextView>(R.id.tvListName)
        name.text = shoppingLists[position].name

        val number = view.findViewById<TextView>(R.id.tvProdNum)
        number.text = shoppingLists[position].productList.size.toString()

        return view
    }

    override fun getItem(position: Int): Any {
        return shoppingLists[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return shoppingLists.size
    }
}
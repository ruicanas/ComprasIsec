package pt.isec.tp_amov.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import pt.isec.tp_amov.R
import pt.isec.tp_amov.objects.Help
import pt.isec.tp_amov.objects.Product
import kotlin.collections.ArrayList

class HelpListAdapter(var tips: ArrayList<Help>): BaseAdapter() {
    override fun getCount(): Int {
        return tips.size
    }

    override fun getItem(position: Int): Any? {
        return tips[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(parent!!.context)
        val view = layoutInflater.inflate(R.layout.layout_help_list, parent, false)

        var name = view.findViewById<TextView>(R.id.helpName)
        name.text = tips[position].name
        var description = view.findViewById<TextView>(R.id.helpDescription)
        description.text = tips[position].description

        return view
    }
}
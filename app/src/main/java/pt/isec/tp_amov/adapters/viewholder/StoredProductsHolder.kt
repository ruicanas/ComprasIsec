package pt.isec.tp_amov.adapters.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.isec.tp_amov.R

class StoredProductsHolder(view: View) : RecyclerView.ViewHolder(view) {
    var tvName : TextView = view.findViewById(R.id.tvDataName)
    var tvCategory : TextView = view.findViewById(R.id.tvDataCat)

    fun update(newName: String, newCategory: String){
        tvName.text = newName
        tvCategory.text = newCategory
    }
}
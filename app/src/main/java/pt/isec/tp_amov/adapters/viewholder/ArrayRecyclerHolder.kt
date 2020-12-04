package pt.isec.tp_amov.adapters.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pt.isec.tp_amov.R

class ArrayRecyclerHolder(view: View) : RecyclerView.ViewHolder(view){
    var configsName : TextView = view.findViewById(R.id.tvConfigsName)

    fun update(newText: String){
        configsName.text = newText
    }
}
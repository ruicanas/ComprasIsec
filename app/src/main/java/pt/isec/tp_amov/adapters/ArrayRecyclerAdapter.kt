package pt.isec.tp_amov.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import pt.isec.tp_amov.R
import pt.isec.tp_amov.adapters.viewholder.ArrayRecyclerHolder
import pt.isec.tp_amov.interfaces.ItemClickListenerInterface

class ArrayRecyclerAdapter(var data: ArrayList<String>,
                           val itemClickInterface: ItemClickListenerInterface<String>) : RecyclerView.Adapter<ArrayRecyclerHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrayRecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_configs, parent,false)
        return ArrayRecyclerHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ArrayRecyclerHolder, position: Int) {
        holder.update(data[position])
        holder.itemView.setOnLongClickListener{
            itemClickInterface.onItemClickListener(data[position])
            true
        }
    }
}
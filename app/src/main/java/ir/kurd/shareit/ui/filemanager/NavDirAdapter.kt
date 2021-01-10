package ir.kurd.shareit.ui.filemanager

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.kurd.shareit.databinding.ItemNavDirctoryBinding

class NavDirAdapter (val dir : ArrayList<String>): RecyclerView.Adapter<NavDirAdapter.NavDirVH>() {


    override fun getItemCount(): Int =  dir.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavDirAdapter.NavDirVH {
        val binding = ItemNavDirctoryBinding.inflate(LayoutInflater.from(parent.context ), parent,false)

        return NavDirVH(binding)
    }

    override fun onBindViewHolder(holder: NavDirAdapter.NavDirVH, position: Int) {
       holder.text.text=  "/${dir[position]}"
    }



inner class NavDirVH (binding: ItemNavDirctoryBinding):RecyclerView.ViewHolder (binding.root){

    val text = binding.dirTV


}

}
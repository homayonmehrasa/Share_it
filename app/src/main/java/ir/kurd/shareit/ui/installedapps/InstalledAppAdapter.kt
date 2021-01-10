package ir.kurd.shareit.ui.installedapps

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.kurd.shareit.databinding.ItemInstalledAppsBinding
import ir.kurd.shareit.model.app.InstalledAppModel

class InstalledAppAdapter ( val appslist: ArrayList<InstalledAppModel>):
    RecyclerView.Adapter<InstalledAppAdapter.ViewHolder>()
    {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {

        val binding = ItemInstalledAppsBinding.inflate(LayoutInflater
                .from(parent.context) , parent , false)

        return ViewHolder(binding)

    }
        override fun getItemCount(): Int {
            return appslist.size
        }

    override fun onBindViewHolder(holder: ViewHolder , position: Int) {


        holder.appname.text = appslist[position].name
        holder.appsize.text = appslist[position].size
        holder.img.setImageDrawable(appslist[position].icon)


    }



    inner class ViewHolder(binding: ItemInstalledAppsBinding) : RecyclerView.ViewHolder(binding.root){

        val img = binding.appImageView
        val appname =binding.appNameTextview
        val appsize =binding.appSizeTextview

    }

        fun updateData(newData:ArrayList<InstalledAppModel>){
            appslist.clear()
            appslist.addAll(newData)
            notifyDataSetChanged()
        }
}
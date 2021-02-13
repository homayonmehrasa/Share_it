package ir.kurd.shareit.ui.images

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.kurd.shareit.databinding.ItemFileManagerBinding
import ir.kurd.shareit.databinding.ItemImagesBinding
import ir.kurd.shareit.model.file.FilesModel
import ir.kurd.shareit.model.image.ImagesModel

class ImagesAdapter  ( val list : ArrayList<ImagesModel>,val callback:ImageCallback)
    : RecyclerView.Adapter<ImagesAdapter.ImageVH>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesAdapter.ImageVH{
        val binding = ItemImagesBinding.inflate(LayoutInflater.from(parent.context) , parent ,false)
        return ImageVH(binding)
    }

    override fun onBindViewHolder(holder:ImagesAdapter.ImageVH, position: Int) {

        holder.img.setImageDrawable(null)
       Glide.with(holder.itemView.context).load(list[position].imageuri).override(300 , 300).into(holder.img)

        holder.imgName.text=list[position].imageName
        holder.itemView.setOnClickListener {
            callback.onImageClicked(holder.img,list[position])
        }


    }

    override fun getItemCount(): Int = list.size

    inner class ImageVH (binding: ItemImagesBinding): RecyclerView.ViewHolder(binding.root) {

        val img = binding.ImageViewItem

    }
}

interface ImageCallback{
    fun onImageClicked(imageView:ImageView,item:ImagesModel)
}
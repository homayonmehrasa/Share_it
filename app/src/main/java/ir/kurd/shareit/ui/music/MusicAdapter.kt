package ir.kurd.shareit.ui.music

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.kurd.shareit.databinding.ItemMusicBinding
import ir.kurd.shareit.model.music.Music_model

class MusicAdapter (private val musicList : ArrayList <Music_model>):
        RecyclerView.Adapter<MusicAdapter.MusicVH>() {


    override fun getItemCount(): Int = musicList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicVH {

        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return MusicVH(binding)
    }

    override fun onBindViewHolder(holder: MusicVH, position: Int) {

        val item = musicList[position]


        holder.musicName.text=item.musicName
        holder.musicSize.text=item.musicSize.toString()

    }


    inner class MusicVH(binding: ItemMusicBinding) : RecyclerView.ViewHolder (binding.root){

        val musicImg = binding.musicImageView
        val musicName = binding.musicName
        val musicSize = binding.musicSize



    }

}
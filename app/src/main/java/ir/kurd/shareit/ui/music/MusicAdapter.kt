package ir.kurd.shareit.ui.music


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.kurd.shareit.R
import ir.kurd.shareit.databinding.ItemMusicBinding
import ir.kurd.shareit.model.music.Music_Model


class MusicAdapter (private val musicList : ArrayList <Music_Model>):
        RecyclerView.Adapter<MusicAdapter.MusicVH>() {

    lateinit var  navController : NavController

    override fun getItemCount(): Int = musicList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicVH {

        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return MusicVH(binding)
    }

    override fun onBindViewHolder(holder: MusicVH, position: Int) {

        val item = musicList[position]

        val size = if (item.musicSize / 1024 < 1000) "${item.musicSize / 1024}KB "
        else "${item.musicSize / (1024 * 1024)}MB"

        holder.musicName.text = item.musicName
        holder.musicSize.text = size


        if( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q){

          holder.musicImg.setImageResource(R.drawable.ic_music)
        }else{

           Glide.with(holder.itemView.context).asBitmap().load(item.imgMusic).error(R.drawable.ic_music).into(holder.musicImg)

        }

        holder.itemView.setOnLongClickListener {
            val bundle =  bundleOf("Uri" to item.musicUri)
           navController = Navigation.findNavController(holder.play )
             navController.navigate(R.id.action_musicFragment_to_misicPlayerFragment , bundle)

            return@setOnLongClickListener true
        }

    }

    inner class MusicVH(binding: ItemMusicBinding) : RecyclerView.ViewHolder (binding.root){

        val play = binding.playBtn
        val musicImg = binding.musicImageView
        val musicName = binding.musicName
        val musicSize = binding.musicSize

    }



}
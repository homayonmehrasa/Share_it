package ir.kurd.shareit.ui.video

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ir.kurd.shareit.R
import ir.kurd.shareit.databinding.ItemVideoBinding
import ir.kurd.shareit.model.video.Video_Model

class VideoAdapter (val videos :  ArrayList <Video_Model>)
    : RecyclerView.Adapter <VideoAdapter.VideoVH>() {

    lateinit var  navController : NavController


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.VideoVH {
       val binding = ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent , false)
        return VideoVH(binding)
    }

    override fun onBindViewHolder(holder: VideoAdapter.VideoVH, position: Int) {
        val list = videos[position]


        val sec =  (( list.durationVideo/ 1000) % 60)
        val min = ((list.durationVideo / 1000) / 60) % 60
        val h = ((list.durationVideo/ 1000) / 3600)

        if ( h ==0) {
            holder.duration.text ="$min : $sec"}
        else {
            holder.duration.text = "$h :$min : $sec"
        }

        holder.itemView.setOnLongClickListener {


            val bundle =  bundleOf("videoUri" to list.uri , "videoDuration" to list.durationVideo.toString())
            navController = Navigation.findNavController(holder.img )
            navController.navigate(R.id.action_videoFragment_to_videoPlayerFragment , bundle)

            return@setOnLongClickListener true
        }


        holder.name.text = list.videoName

        Glide
                .with(holder.itemView.context)
                .asBitmap()
                .load(list.uri)
                .into(holder.img)

    }

    override fun getItemCount(): Int =  videos.size


inner class VideoVH(binding : ItemVideoBinding) : RecyclerView.ViewHolder (binding.root){

    val img = binding.imageVideo
    val name = binding.nameVideo
    val duration = binding.durationTV

}

}
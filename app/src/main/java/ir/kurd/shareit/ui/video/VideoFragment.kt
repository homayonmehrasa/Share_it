package ir.kurd.shareit.ui.video

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.kurd.shareit.databinding.FragmentVideoBinding
import ir.kurd.shareit.model.video.Video_Model
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ir.kurd.shareit.ui.video.VideoVM as VideoVM


@Suppress("UNREACHABLE_CODE")

class VideoFragment : BaseFragment <VideoVM, FragmentVideoBinding >(){




    val videoList =  arrayListOf<Video_Model>()

    override val vm: VideoVM by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentVideoBinding.inflate(inflater)
        return binding.root
    }

    override fun init() {


        getAllVideos ()
    }


  private  fun getAllVideos (){
        val videoProjection = arrayOf(
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DURATION

        )
        val contentResolver = requireContext().contentResolver
        val cursor =contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoProjection ,
                null ,
                null ,
                null)

        cursor?.use {
            cursor ->


                val idColumn = cursor.getColumnIndexOrThrow( MediaStore.Video.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
                val sizeColumn = cursor.getColumnIndexOrThrow( MediaStore.Video.Media.SIZE)
                val durationColumn = cursor.getColumnIndexOrThrow( MediaStore.Video.Media.DURATION)


                while (cursor.moveToNext()){
                    val name =cursor.getString(nameColumn)
                    val siz = cursor.getShort(sizeColumn).toString()
                    val id = cursor.getLong(idColumn)
                    val duration = cursor.getInt(durationColumn)
                    val contentUri : Uri = ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            id
                    )

                    videoList+= Video_Model(name , siz , contentUri , duration)



                }
            binding.videoRecyclerView.adapter= VideoAdapter(videoList)



                Toast.makeText(requireContext(), "${videoList.size}", Toast.LENGTH_SHORT).show()
            }?: kotlin.run {
            Log.e("TAG", "Cursor is null!")
        }
            
            
            
        }


    }


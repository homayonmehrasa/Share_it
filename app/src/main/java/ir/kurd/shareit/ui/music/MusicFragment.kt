package ir.kurd.shareit.ui.music

import android.content.ContentUris
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.kurd.shareit.databinding.FragmentMusicBinding
import ir.kurd.shareit.model.music.Music_model
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


class MusicFragment : BaseFragment<MusicVM, FragmentMusicBinding> () {


    val mymusicList = arrayListOf<Music_model>()
    override val vm: MusicVM by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMusicBinding.inflate(inflater)
        return binding.root
    }


    override fun init() {
         getAllmusics()

    }

    private fun getAllmusics() {

        val musicProjection = arrayOf(
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media._ID
        )

     //   val musicSortOrder = "${MediaStore.Audio.Media.DISPLAY_NAME} DESC"
        val contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                musicProjection,
                null,
                null,
                null)

        cursor.use {
            it?.let {
                //cursor!!.moveToFirst()

                val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val size = it.getString(sizeColumn).toInt()
                    val contentUri: Uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                    )
                    mymusicList.add(
                            Music_model(name, size, contentUri)
                    )



                }
                binding.musicRecyclerView.adapter =MusicAdapter(mymusicList)

            }?: kotlin.run {
                Log.e("TAG", "Cursor is null!")
            }


        }

    }


}
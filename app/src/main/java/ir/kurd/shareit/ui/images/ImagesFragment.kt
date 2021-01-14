package ir.kurd.shareit.ui.images

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.os.Bundle

import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContentResolverCompat.query
import androidx.room.util.DBUtil.query
import ir.kurd.shareit.databinding.FragmentImagesBinding
import ir.kurd.shareit.model.image.ImagesModel
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

@Suppress("UNREACHABLE_CODE")
class ImagesFragment : BaseFragment<ImagesVM,FragmentImagesBinding >() {

    val imglist= arrayListOf<ImagesModel>()

    override val vm : ImagesVM by viewModel()
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(inflater: LayoutInflater
                              , container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentImagesBinding.inflate(inflater)
        return binding.root



    }

    override fun init() {
        requestReadStoragePermission(){
            queryImageStorage()
        }


    }

    private fun queryImageStorage() {
        val imageProjection = arrayOf(
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media._ID ,

        )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        val  contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI ,
                imageProjection,
                null,
                null,
                imageSortOrder)

        cursor.use {
            it?.let {


                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                val sizeColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
                val dateColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val name = it.getString(nameColumn)
                    val size = it.getString(sizeColumn).toInt()
                    val date = it.getString(dateColumn)
                    val contentUri : Uri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            id
                    )




                    imglist.add(ImagesModel(name , size , contentUri ))









                    // add the URI to the list
                    // generate the thumbnail
                    //val thumbnail =  (this as Context).contentResolver.loadThumbnail(contentUri, Size(480, 480), null)
                }
                binding.imagesRecyclerView.adapter = ImagesAdapter(imglist)
            } ?: kotlin.run {
                Log.e("TAG", "Cursor is null!")
            }
        }
    }




}
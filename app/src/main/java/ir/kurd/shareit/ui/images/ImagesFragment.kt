package ir.kurd.shareit.ui.images

import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import ir.kurd.shareit.R
import ir.kurd.shareit.databinding.FragmentImagesBinding
import ir.kurd.shareit.model.image.ImagesModel
import ir.kurd.shareit.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("UNREACHABLE_CODE")
class ImagesFragment : BaseFragment<ImagesVM, FragmentImagesBinding>(),ImageCallback {
    val animatingViews = arrayListOf<ImageView>()

    val imglist= arrayListOf<ImagesModel>()

    override val vm : ImagesVM by viewModel()
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            MediaStore.Images.Media._ID,

            )
        val imageSortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        val  contentResolver = requireContext().contentResolver
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            imageSortOrder
        )

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




                    imglist.add(ImagesModel(name, size, contentUri))









                    // add the URI to the list
                    // generate the thumbnail
                    //val thumbnail =  (this as Context).contentResolver.loadThumbnail(contentUri, Size(480, 480), null)
                }
                binding.imagesRecyclerView.adapter = ImagesAdapter(imglist, this)
            }?: kotlin.run {
                Log.e("TAG", "Cursor is null!")
            }
        }
    }

    override fun onImageClicked(imageView: ImageView, item: ImagesModel) {
            animateImage(imageView)
            sendFiles(arrayListOf(item.imageuri))
    }

    private fun animateImage(imageView: ImageView){

        // get location of the clicked view
        val fromLoc = IntArray(2)
        val fromLocWindow = IntArray(2)
        imageView.getLocationOnScreen(fromLoc)
        imageView.getLocationInWindow(fromLocWindow)

        val startX = fromLoc[0].toFloat()
        val startY = fromLoc[1].toFloat()


        val newView = ImageView(requireContext())
        newView.setImageDrawable(imageView.drawable)
        animatingViews.add(newView)

        binding.root.addView(animatingViews.last())
        val params = animatingViews.last().layoutParams
        params as ConstraintLayout.LayoutParams
        params.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        params.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        params.topMargin = startY.toInt()  -( imageView.height/2)
        params.leftMargin = startX.toInt()
        params.width = imageView.width
        params.height = imageView.height

        // get location of cart icon

        val animationToLeft = AnimationSet(true)

        animationToLeft.fillAfter = true

        val scaleToLeftAnimation = ScaleAnimation(1f, 0.5.toFloat(), 1f, 0.5.toFloat())

        scaleToLeftAnimation.interpolator = BounceInterpolator()
        scaleToLeftAnimation.duration = 1000
        scaleToLeftAnimation.fillAfter = true
        animationToLeft.addAnimation(scaleToLeftAnimation)

        val translateToLeftAnimation = TranslateAnimation(0f, 0f, 0f, binding.root.height.toFloat())
        translateToLeftAnimation.interpolator = AccelerateDecelerateInterpolator()
        translateToLeftAnimation.duration = 1000
        translateToLeftAnimation.fillAfter = true

        animationToLeft.addAnimation(translateToLeftAnimation)

        val alpha = AlphaAnimation(1f, 0.5f)
        alpha.duration = 1000
        animationToLeft.addAnimation(alpha)

        animationToLeft.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                if(animatingViews.firstOrNull()!=null) {
                    binding.root.post {
                        binding.root.removeView(animatingViews.first())
                        animatingViews.removeFirst()

                    }
                }

            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })
        animatingViews.last().post {
            animatingViews.last().startAnimation(animationToLeft)
        }
    }


    private fun sendFiles(uris:ArrayList<Uri>){
        val bundle = Bundle().apply {
            this.putParcelableArrayList("uris",uris)
        }
        navController.navigate(R.id.testP2PFragment,bundle)
    }


}
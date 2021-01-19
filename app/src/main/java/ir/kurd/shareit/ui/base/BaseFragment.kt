package ir.kurd.shareit.ui.base

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import ir.kurd.shareit.R

abstract class BaseFragment<T:BaseViewModel,B:ViewBinding>: Fragment() {

    abstract val vm :T
    lateinit var  binding:B
    var loadingView : View?=null
    lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeUiState()
        initNavController()
        init()
    }


    private fun observeUiState() {
        vm.uiStateObservable.observe(this) {
            when (it) {
                is UiState.Loading -> {
                    changeLoadingState(it.isLoading)
                }
                is UiState.Error -> {
                    changeLoadingState(false)
                    showError(it.message,it.type)
                }
            }
        }
    }

    open fun changeLoadingState(isLoading:Boolean){
        loadingView?.let {
            if(isLoading){
                it.visibility = View.VISIBLE
            }else{
                it.visibility = View.GONE
            }
        }
    }


    open fun showError(msg:String?,type:String){
        if(!msg.isNullOrBlank())
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        vm.onClear()
    }

    private fun initNavController() {
        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
    }

  abstract fun init ()



    fun requestStoragePermission(callback:(Boolean)->Unit){
        Dexter.withContext(requireContext()).withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(object:PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                callback(true)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                callback(false)
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()
    }
    fun requestReadStoragePermission(callback:(Boolean)->Unit){
        Dexter.withContext(requireContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(object:PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                callback(true)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                callback(false)
            }

            override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()
    }

    fun requestLocationPermission(callback:(Boolean)->Unit){
        Dexter.withContext(requireContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(object:PermissionListener{
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                callback(true)
            }

            override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                callback(false)
            }

            override fun onPermissionRationaleShouldBeShown(
                p0: PermissionRequest?,
                p1: PermissionToken?
            ) {
                p1?.continuePermissionRequest()
            }

        }).check()
    }

    fun showShortToast(message:String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

}
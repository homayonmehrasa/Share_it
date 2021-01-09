package ir.kurd.shareit.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
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


}
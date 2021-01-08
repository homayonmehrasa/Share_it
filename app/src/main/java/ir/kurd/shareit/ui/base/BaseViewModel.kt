package ir.kurd.shareit.ui.base

import androidx.lifecycle.ViewModel
import ir.kurd.shareit.utiles.SingleLiveEvent

abstract class BaseViewModel: ViewModel() {
    var uiStateObservable = SingleLiveEvent<UiState>()



    fun onClear(){

    }

}
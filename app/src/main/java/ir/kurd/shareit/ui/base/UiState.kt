package ir.kurd.shareit.ui.base

sealed class UiState(){
    data class Loading(var isLoading:Boolean): UiState()
    data class Error(var message:String?,var type:String): UiState()
}
package ir.kurd.shareit.ui.test

import android.net.Uri
import android.net.wifi.p2p.WifiP2pDevice
import ir.kurd.shareit.ui.base.BaseViewModel

class TestP2PVM: BaseViewModel() {
    var peers = arrayListOf<WifiP2pDevice>()
    var uris = arrayListOf<Uri>()

}
package ir.kurd.shareit.ui.test

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.Uri
import android.net.wifi.p2p.*
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.work.*
import ir.kurd.shareit.R
import ir.kurd.shareit.broadcast.WiFiDirectBroadcastReceiver
import ir.kurd.shareit.databinding.FragmentMainBinding
import ir.kurd.shareit.databinding.FragmentPrepareSendBinding
import ir.kurd.shareit.databinding.FragmentTestP2pBinding
import ir.kurd.shareit.ui.base.BaseFragment
import ir.kurd.shareit.worker.FileTransferWorker
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestP2PFragment: BaseFragment<TestP2PVM,FragmentTestP2pBinding>(),WifiP2pManager.ConnectionInfoListener {
    private var connectionInfo: WifiP2pInfo?=null
    override val vm : TestP2PVM by viewModel()
    val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        requireContext().getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    var channel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    val intentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestP2pBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        vm.uris = arguments?.getParcelableArrayList<Uri>("uris")?: arrayListOf()
        requestLocationPermission {
            prepareWifi()
        }







    }

    @SuppressLint("MissingPermission")
    private fun prepareWifi() {
        channel = manager?.initialize(requireContext(), Looper.getMainLooper(), null)
        channel?.also { channel ->
            if(manager!=null)
                receiver = WiFiDirectBroadcastReceiverTest(manager!!, channel, this)
        }


    }


    @SuppressLint("MissingPermission")
    fun p2pIsEnable(){
        manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {

            override fun onSuccess() {

            }

            override fun onFailure(reasonCode: Int) {
                Toast.makeText(requireContext(), "Wifi Preparation Failed.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        receiver?.also { receiver ->
            requireContext().registerReceiver(receiver, intentFilter)
        }

    }

    override fun onPause() {
        super.onPause()
        receiver?.also { receiver ->
            requireContext().unregisterReceiver(receiver)
        }

    }

    @SuppressLint("MissingPermission")
    fun handlePeers() {
        manager?.requestPeers(channel) { peers: WifiP2pDeviceList? ->
            if(peers==null){
                showShortToast("Peers are null")
            }else{
                vm.peers = ArrayList(peers.deviceList)
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext(), R.style.Theme_AppCompat_Light_Dialog)
                builder.setItems(peers.deviceList.map { it.deviceName }.toTypedArray()) {dialogInterface, i ->
                    sendFileTooPeer(vm.peers[i])
                }

                builder.setNegativeButton("Cancel", { dialog, which -> dialog.cancel() })
                builder.show()
                showShortToast("peers count : ${peers.deviceList.size} -> ${peers.deviceList.firstOrNull()?.deviceName}")
            }
        }

    }

    @SuppressLint("MissingPermission")
    fun sendFileTooPeer(device: WifiP2pDevice){
        val config = WifiP2pConfig()
        config.deviceAddress = device.deviceAddress
        manager?.connect(channel,config,object:WifiP2pManager.ActionListener{
            override fun onSuccess() {
                showShortToast("connection success")
                val uploadWorkRequest: WorkRequest =
                    OneTimeWorkRequestBuilder<FileTransferWorker>()
                        .setInputData(
                            workDataOf(
                                Pair(FileTransferWorker.EXTRAS_FILE_PATH,vm.uris.first().path!!),
                                Pair(FileTransferWorker.EXTRAS_TARGET_PORT,1998),
                                Pair(FileTransferWorker.EXTRAS_TARGET_HOST,connectionInfo!!.groupOwnerAddress),
                                ))
                        .build()
                val myWorkRequest = WorkManager.getInstance(requireContext()).enqueue(uploadWorkRequest)
                myWorkRequest.result.addListener({
                                                 showShortToast("ResultSuccess")
                }, ContextCompat.getMainExecutor(requireContext()))
            }

            override fun onFailure(p0: Int) {
                when(p0){
                    WifiP2pManager.BUSY->{
                        showShortToast("p2p Busy")

                        handlePeers()
                    }
                    WifiP2pManager.ERROR->{
                        showShortToast("p2p failed to get ready")
                    }
                    WifiP2pManager.P2P_UNSUPPORTED->{
                        showShortToast("Your Device doesn't support p2p")

                    }
                }

            }

        })
    }

    override fun onConnectionInfoAvailable(p0: WifiP2pInfo?) {
        this.connectionInfo = p0

    }
}
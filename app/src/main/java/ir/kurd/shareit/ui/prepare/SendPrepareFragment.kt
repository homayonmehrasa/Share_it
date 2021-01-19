package ir.kurd.shareit.ui.prepare

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import ir.kurd.shareit.databinding.FragmentPrepareSendBinding
import ir.kurd.shareit.ui.base.BaseFragment
import ir.kurd.shareit.worker.FileTransferWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*
import java.net.ServerSocket
import java.net.Socket


class SendPrepareFragment: BaseFragment<SendPrepareVM, FragmentPrepareSendBinding>(),WifiP2pManager.ConnectionInfoListener {
    private val TAG: String= "SendPrepare"
    override val vm : SendPrepareVM by viewModel()
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
        binding = FragmentPrepareSendBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun init() {
        requestLocationPermission {
            prepareWifi()
        }


    }

    @SuppressLint("MissingPermission")
    private fun prepareWifi() {
        channel = manager?.initialize(requireContext(), Looper.getMainLooper(), null)
        channel?.also { channel ->
            if(manager!=null)
                receiver = WiFiDirectBroadcastReceiverPrepare(manager!!, channel, this)
        }


    }

    @SuppressLint("MissingPermission")
    fun p2pIsEnable(){

    }

    override fun onConnectionInfoAvailable(p0: WifiP2pInfo?) {
        showShortToast("Connection Incoming.. ")
        lifecycleScope.launch(Dispatchers.IO){
            receiveSocketStream()

        }

    }


     private fun receiveSocketStream(){
        try {
            val serverSocket = ServerSocket(FileTransferWorker.PORT)
            Log.d(TAG, "Server: Socket opened")
            val client: Socket = serverSocket.accept()
            Log.d(TAG, "Server: connection done")
            val f = File(
                requireContext().getExternalFilesDir("received"),
                "wifip2pshared-" + System.currentTimeMillis()
                        + ".jpg"
            )
            val dirs = File(f.parent)
            if (!dirs.exists()) dirs.mkdirs()
            f.createNewFile()
            Log.d(TAG, "server: copying files $f")
            val inputstream: InputStream = client.getInputStream()
            copyFile(inputstream, FileOutputStream(f))
            serverSocket.close()
        } catch (e: IOException) {
            Log.e(TAG, (e.message)?:"")
        }
    }

    private fun copyFile(inputStream: InputStream, out: OutputStream): Boolean {
        val buf = ByteArray(1024)
        var len: Int
        try {
            while ((inputStream.read(buf).also { len = it }) != -1) {
                out.write(buf, 0, len)
            }
            out.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.d("asaad", e.toString())
            return false
        }
        return true
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
}
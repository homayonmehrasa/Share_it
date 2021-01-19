package ir.kurd.shareit.worker

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket


class FileTransferWorker(val context: Context, workerParameters: WorkerParameters): CoroutineWorker(
    context,
    workerParameters
) {
    companion object{
        const val TAG = "FileTransferWorker"
        const val EXTRAS_FILE_PATH = "EXTRA_FILE_PATH"
        const val EXTRAS_TARGET_HOST = "EXTRA_TARGET_HOST"
        const val EXTRAS_TARGET_PORT = "EXTRA_TARGET_PORT"
        const val PORT = 1998
        const val SOCKET_TIMEOUT = 5000
    }
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            val context = applicationContext
                val fileUri: String = inputData.getString(EXTRAS_FILE_PATH)?:""
                val host: String = inputData.getString(EXTRAS_TARGET_HOST)?:""
                val socket = Socket()
                val port: Int = inputData.getInt(EXTRAS_TARGET_PORT, 1998)
                try {
                    Log.d(TAG, "Opening client socket - ")
                    socket.bind(null)
                    socket.connect(InetSocketAddress(host, port), SOCKET_TIMEOUT)
                    Log.d(TAG, "Client socket - " + socket.isConnected)
                    val stream: OutputStream = socket.getOutputStream()
                    val cr = context.contentResolver
                    var inputStream: InputStream? = null
                    try {
                        inputStream = cr.openInputStream(Uri.parse(fileUri))
                    } catch (e: FileNotFoundException) {
                        Log.d(TAG, e.toString())
                    }
                    if (inputStream != null) {
                        copyFile(inputStream, stream)
                    }
                    Log.d(TAG, "Client: Data written")
                } catch (e: IOException) {
                    Log.e(TAG, e.message?:"")
                } finally {
                    if (socket != null) {
                        if (socket.isConnected()) {
                            try {
                                socket.close()
                            } catch (e: IOException) {
                                // Give up
                                e.printStackTrace()
                            }
                        }
                    }
                }

            return@withContext Result.success()
        }

    }

     suspend fun copyFile(inputStream:InputStream, out:OutputStream)= withContext(Dispatchers.IO){
        val buf = ByteArray(1024)
        var len: Int
        try {
            while (inputStream.read(buf).also { len = it } != -1) {
                out.write(buf, 0, len)
            }
            out.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
        }
        }

}
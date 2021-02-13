package ir.kurd.shareit.ui

import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.ContentProviderCompat.requireContext
import ir.kurd.shareit.R
import ir.kurd.shareit.ui.base.BaseActivity
import java.io.File

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
     //   val uri : Uri =  Uri.fromFile(File("/storage/emulated/0") )

        //this.contentResolver.update(uri , null, null, null)



    }
}
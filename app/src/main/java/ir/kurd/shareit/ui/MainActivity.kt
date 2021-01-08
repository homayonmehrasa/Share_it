package ir.kurd.shareit.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ir.kurd.shareit.R
import ir.kurd.shareit.ui.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}
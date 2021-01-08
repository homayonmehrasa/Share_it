package ir.kurd.shareit.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import ir.kurd.shareit.R

abstract class BaseActivity: AppCompatActivity() {
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onStart() {
        super.onStart()
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

    }
}
package com.project.dtttest.ui.activities

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.project.dtttest.R
import com.project.dtttest.databinding.ActivityMainBinding
import com.project.dtttest.repository.HouseRepository
import com.project.dtttest.ui.viewmodels.HouseViewModel
import com.project.dtttest.ui.viewmodels.HouseViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: HouseViewModel
    private lateinit var navController: NavController
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navHostFragment.findNavController())

        val repository = HouseRepository()
        val viewModelFactory = HouseViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HouseViewModel::class.java)

        hideStatusBar()
    }


    /**
     * Hide Bottom Navigation View in houseDetailFragment
     * See BaseFragment
     */
    fun hideBottomNavView(hide: Boolean) {
        binding.bottomNavView.visibility = if (hide) View.GONE else View.VISIBLE
    }

    /**
     * Hide Status Bar
     * Depending on the device's Android version use one method or the other
     */
    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}
package com.project.dtttest.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.project.dtttest.R
import com.project.dtttest.databinding.ActivityMainBinding
import com.project.dtttest.repository.Repository
import com.project.dtttest.ui.viewmodelfactory.MainViewModelFactory
import com.project.dtttest.ui.viewmodels.LocationViewModel
import com.project.dtttest.ui.viewmodels.MainViewModel
import com.project.dtttest.utils.Constants.Companion.GPS_REQUEST
import com.project.dtttest.utils.Constants.Companion.LOCATION_REQUEST_CODE
import com.project.dtttest.utils.GpsUtils

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController

    val TAG = "Coordinates"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavView.setupWithNavController(navHostFragment.findNavController())

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        hideBottomNavView(binding)
        hideStatusBar()
    }

    /**
     * Hide Bottom Navigation View in houseDetailFragment
     */
    private fun hideBottomNavView(binding: ActivityMainBinding) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.houseDetailFragment -> binding.bottomNavView.visibility = View.GONE
                R.id.overviewFragment -> binding.bottomNavView.visibility = View.VISIBLE
                R.id.informationFragment -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }
    }

    private fun hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }
}
package com.project.dtttest.ui

import android.location.Location
import com.project.dtttest.adapters.HouseAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.dtttest.R
import com.project.dtttest.databinding.ActivityMainBinding
import com.project.dtttest.repository.Repository

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        //Hide Bottom Navigation View in HouseDetailFragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.houseDetailFragment -> binding.bottomNavView.visibility = View.GONE
                R.id.overviewFragment -> binding.bottomNavView.visibility = View.VISIBLE
                R.id.informationFragment -> binding.bottomNavView.visibility = View.VISIBLE
            }
        }

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.bottomNavView.setupWithNavController(navHostFragment.findNavController())
    }
}
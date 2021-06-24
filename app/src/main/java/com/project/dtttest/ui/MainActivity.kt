package com.project.dtttest.ui

import com.project.dtttest.adapters.HouseAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.dtttest.R
import com.project.dtttest.databinding.ActivityMainBinding
import com.project.dtttest.repository.Repository

class MainActivity : AppCompatActivity()/*, OnMapReadyCallback*/ {

    lateinit var viewModel: MainViewModel
    private val houseAdapter by lazy { HouseAdapter() }
    private lateinit var navController: NavController

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //initGoogleMap()
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        // viewModel.getHouses()
        // viewModel.myResponse.observe(this, Observer { response ->
        //     if (response.isSuccessful) {
        //         response.body()?.let { houseAdapter.setData(it) }
        //     } else {
        //         Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
        //     }
        // })

        binding.bottomNavView.setupWithNavController(navHostFragment.findNavController())
    }

    // private fun initGoogleMap(){
    //     val mapFragment = supportFragmentManager
    //         .findFragmentById(R.id.frMap) as SupportMapFragment
    //     mapFragment.getMapAsync(this)
    // }
    //
    // override fun onMapReady(googleMap: GoogleMap) {
    //     map = googleMap
    //     createMarker()
    // }
    //
    // fun createMarker(){
    //     val coordinates = LatLng(28.043893, -16.539329)
    //     val marker = MarkerOptions().position(coordinates)
    //     map.addMarker(marker)
    // }
}
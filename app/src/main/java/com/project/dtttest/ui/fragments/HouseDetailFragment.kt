package com.project.dtttest.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.dtttest.R
import com.project.dtttest.databinding.FragmentHouseDetailBinding
import com.project.dtttest.ui.MainActivity
import com.project.dtttest.ui.MainViewModel
import com.project.dtttest.utils.Constants.Companion.MAPVIEW_BUNDLE_KEY


class HouseDetailFragment : Fragment()/*, OnMapReadyCallback*/ {

    private var _binding: FragmentHouseDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel

    private lateinit var map: GoogleMap

    private val args: HouseDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    // override fun onCreate(savedInstanceState: Bundle?) {
    //     super.onCreate(savedInstanceState)
    //
    //     initGoogleMap(savedInstanceState)
    // }
    //
    // private fun initGoogleMap(savedInstanceState: Bundle?) {
    //     // *** IMPORTANT ***
    //     // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
    //     // objects or sub-Bundles.
    //     var mapViewBundle: Bundle? = null
    //     if (savedInstanceState != null) {
    //         mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY)
    //     }
    //     binding.mvMap.onCreate(mapViewBundle)
    //     binding.mvMap.getMapAsync(this)
    // }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val house = args.house
        viewModel = (activity as MainActivity).viewModel
        binding.tvDescription.text = house.description
        binding.tvPriceDetail.text = "$" + house.price.toString()
        binding.tvBedroomsDetail.text = house.bedrooms.toString()
        binding.tvBathroomsDetail.text = house.bathrooms.toString()
        binding.tvSizeDetail.text = house.size.toString()
        val url: String =
            "https://intern.docker-dev.d-tt.nl" + house.image
        val glideUrl = GlideUrl(
            url,
            LazyHeaders.Builder()
                .addHeader("Access-Key", "98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
                .build()
        )
        Glide.with(this).load(glideUrl).into(binding.ivHouseDetail)
    }

    // private fun initGoogleMap(){
    //     val mapFragment = supportFragmentManager
    //         .findFragmentById(R.id.frMap) as SupportMapFragment
    //     mapFragment.getMapAsync(this)
    // }
    //
    //
    // override fun onMapReady(googleMap: GoogleMap) {
    //     map = googleMap
    //     //createMarker()
    // }

    // private fun createMarker() {
    //     val favoritePlace = LatLng(28.044195,-16.5363842)
    //     map.addMarker(MarkerOptions().position(favoritePlace).title("Mi playa favorita!"))
    //     map.animateCamera(
    //         CameraUpdateFactory.newLatLngZoom(favoritePlace, 18f),
    //         4000,
    //         null
    //     )
    // }


}
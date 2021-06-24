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
import java.text.DecimalFormat

class HouseDetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHouseDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel

    private val args: HouseDetailFragmentArgs by navArgs()

    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val house = args.house
        viewModel = (activity as MainActivity).viewModel
        binding.tvDescription.text = house.description

        val formattedPrice = DecimalFormat("#,###").format(
            house.price
        )
        binding.tvPriceDetail.text = "$" + formattedPrice

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

        initGoogleMap()
    }

    /**
     * Initializes Google Maps fragment for house location
     */
    private fun initGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.frMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
    }

    /**
     * Creates marker for exact location of house
     */
    private fun createMarker() {
        val house = args.house
        val houseLocation = LatLng(house.latitude.toDouble(), house.longitude.toDouble())
        map.addMarker(MarkerOptions().position(houseLocation))
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(houseLocation, 12f),
            1,
            null
        )
    }

    private fun calculateDistance() {

    }
}
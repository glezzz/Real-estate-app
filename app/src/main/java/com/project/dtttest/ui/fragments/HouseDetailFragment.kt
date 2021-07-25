package com.project.dtttest.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.project.dtttest.R
import com.project.dtttest.databinding.FragmentHouseDetailBinding
import com.project.dtttest.model.HouseResponse
import com.project.dtttest.ui.activities.MainActivity
import com.project.dtttest.ui.viewmodels.HouseViewModel
import com.project.dtttest.utils.Constants.Companion.LOCATION_REQUEST_CODE
import com.project.dtttest.utils.formatDistance
import com.project.dtttest.utils.formatPrice
import com.project.dtttest.utils.loadHouseImage


class HouseDetailFragment : BaseFragment(), OnMapReadyCallback, BaseFragment.HideNavigationBar {

    private var _binding: FragmentHouseDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HouseViewModel

    private val args: HouseDetailFragmentArgs by navArgs()

    private lateinit var map: GoogleMap

    private val TAG = "HouseDetailFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        val house = args.house
        bindViews(house)

        // Load house image
        Glide.with(this).load(loadHouseImage(house.image)).into(binding.ivHouseDetail)

        initGoogleMap()

        // Back icon click listener
        binding.ivBackBtn.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    /**
     * Bind views in HouseDetailFragment
     */
    private fun bindViews(house: HouseResponse) {
        binding.tvDescription.text = house.description
        binding.tvPriceDetail.text = formatPrice(house.price)
        binding.tvBedroomsDetail.text = house.bedrooms.toString()
        binding.tvBathroomsDetail.text = house.bathrooms.toString()
        binding.tvSizeDetail.text = house.size.toString()

        // Calculate distance between user location and house
        if (house.distance != null) {
            binding.tvDistanceDetail.text = formatDistance(house.distance!!)

            // Keep default textSize
            binding.tvDistanceDetail.textSize = 12.0F

        } else {

            // If permissions are not granted print string to screen & adjust textSize
            binding.tvDistanceDetail.text = getString(R.string.no_permissions)
            binding.tvDistanceDetail.textSize = 10.0F
        }
    }

    /**
     * Initializes Google Maps fragment for house location
     */
    private fun initGoogleMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.frMap) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    /**
     * Called when the map is ready to be used. Invokes createMarker() & enableUserLocation() methods
     * and sets up a clickListener that invokes onMapClickRedirectToGoogleMaps()
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        createMarker()
        enableUserLocation()
        map.setOnMapClickListener {

            val house = args.house
            onMapClickRedirectToGoogleMaps(house.latitude.toDouble(), house.longitude.toDouble())
        }
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

    /**
     * Clicking on the map component redirects to Google Maps directions, creating a route from user location
     * to house location
     *
     * @param houseLatitude the latitude of the house
     * @param houseLongitude the longitude of the house
     */
    private fun onMapClickRedirectToGoogleMaps(houseLatitude: Double, houseLongitude: Double) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?&daddr=$houseLatitude, $houseLongitude")
        )
        startActivity(intent)
    }

    /**
     * Checks if location permission has been granted
     */
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    /**
     * Enables user location to be able to navigate to Google Maps directions
     */
    private fun enableUserLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            map.isMyLocationEnabled = true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                map.isMyLocationEnabled = true
            }
        }
    }
}
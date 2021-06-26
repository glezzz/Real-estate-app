package com.project.dtttest.ui.fragments

import android.Manifest
import android.R.attr.defaultValue
import android.R.attr.key
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


open class HouseDetailFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentHouseDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    // private lateinit var maps: Maps

    private val args: HouseDetailFragmentArgs by navArgs()

    private lateinit var userCoordinates: DoubleArray

    var locationManager: LocationManager? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private val TAG = "My coordinates"

    companion object {
        const val LOCATION_REQUEST_CODE = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.getDoubleArray("userCoordinates")?.let { userCoord ->
            userCoordinates = userCoord
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val house = args.house
        viewModel = (activity as MainActivity).viewModel
        binding.tvDescription.text = house.description

        val formattedPrice = DecimalFormat("#,###").format(house.price)
        binding.tvPriceDetail.text = "$" + formattedPrice

        binding.tvBedroomsDetail.text = house.bedrooms.toString()
        binding.tvBathroomsDetail.text = house.bathrooms.toString()
        binding.tvSizeDetail.text = house.size.toString()

        // TODO Fix first time permissions allowed crashes app
        if (userCoordinates.isNotEmpty()) {
            binding.tvDistanceDetail.text = userCoordinates[0].toString()
        } else {
            binding.tvDistanceDetail.text = "Need permissions"
        }

        val url: String =
            "https://intern.docker-dev.d-tt.nl" + house.image
        val glideUrl = GlideUrl(
            url,
            LazyHeaders.Builder()
                .addHeader("Access-Key", "98bww4ezuzfePCYFxJEWyszbUXc7dxRx")
                .build()
        )
        Glide.with(this).load(glideUrl).into(binding.ivHouseDetail)

        /* maps.*/initGoogleMap()


    }


    private lateinit var map: GoogleMap

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
        enableUserLocation()
        //getLocation()
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
     * Permission to get current location
     */
    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                requireContext(),
                "Ve a ajustes y acepta los permisos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST_CODE
            )
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
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    requireContext(),
                    "Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
            }
        }
    }
}
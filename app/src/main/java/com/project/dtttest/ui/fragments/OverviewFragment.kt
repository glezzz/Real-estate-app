package com.project.dtttest.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.project.dtttest.R
import com.project.dtttest.adapters.HouseAdapter
import com.project.dtttest.databinding.FragmentOverviewBinding
import com.project.dtttest.ui.MainActivity
import com.project.dtttest.ui.MainViewModel
import com.project.dtttest.ui.fragments.HouseDetailFragment.Companion.LOCATION_REQUEST_CODE

class OverviewFragment : Fragment() {

    lateinit var viewModel: MainViewModel
    lateinit var houseAdapter: HouseAdapter
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val TAG = "My coordinates"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.getHouses()
        viewModel.myResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.let { houseAdapter.setData(it) }
            } else {
                Toast.makeText(context, response.code(), Toast.LENGTH_SHORT).show()
            }
        })
        // Send house data in bundle to HouseDetailFragment to display the clicked house
        houseAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("house", it)
            }
            findNavController().navigate(
                R.id.action_overviewFragment_to_houseDetailFragment,
                bundle
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Instantiate Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    /**
     * Request location permissions on start
     */
    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            askLocationPermission()
        }
    }

    /**
     * Request last known location of user's device,
     * which is usually equivalent to the known location of device.
     */
    var userCoordinates =  mutableListOf<Double>()
    private fun getLastLocation() {


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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    //We have a location
                    Log.d(TAG, "onSuccess: $location")

                    Log.d(TAG, "onSuccess: " + location.latitude)
                    userCoordinates.add(location.latitude)

                    Log.d(TAG, "onSuccess: " + location.longitude)
                    userCoordinates.add(location.longitude)

                    Log.d(TAG, "onSuccess: " + userCoordinates)

                } else {
                    Log.d(TAG, "onSuccess: Location was null...")
                }
            }
        return
    }

    private fun askLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...")
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                getLastLocation()
            } else {
                //Permission not granted
            }
        }
    }

    private fun setupRecyclerView() {
        houseAdapter = HouseAdapter(this)
        binding.rvHouses.apply {
            adapter = houseAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
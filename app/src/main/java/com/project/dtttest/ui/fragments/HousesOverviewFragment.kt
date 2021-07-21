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
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.project.dtttest.R
import com.project.dtttest.adapters.HouseAdapter
import com.project.dtttest.databinding.FragmentOverviewBinding
import com.project.dtttest.model.HouseResponse
import com.project.dtttest.ui.activities.MainActivity
import com.project.dtttest.ui.viewmodels.MainViewModel
import com.project.dtttest.utils.Constants.Companion.LOCATION_REQUEST_CODE

class OverviewFragment : BaseFragment() {

    lateinit var viewModel: MainViewModel
    lateinit var houseAdapter: HouseAdapter
    private var _binding: FragmentOverviewBinding? = null
    val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

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

        viewModel.allHouses.observe(viewLifecycleOwner, Observer { list ->
            Log.d("OverviewFragment", "viewModel.allHouses.observe() list: $list")
            houseAdapter.setData(list)
        })
        viewModel.networkStatus.observe(viewLifecycleOwner, Observer { status ->

        })
        viewModel.error.observe(viewLifecycleOwner, Observer { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                viewModel.error.value = null
            }
        })


        viewModel.getHouses()

        // Send house data in bundle to HouseDetailFragment to display the clicked house
        houseAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("house", it)

                // // Bundle with user location to display in detail fragment
                // putDoubleArray("userCoordinates", userCoordinates.toDoubleArray())
            }
            findNavController().navigate(
                R.id.action_overviewFragment_to_houseDetailFragment,
                bundle
            )
        }

        // Listen to text changes in search bar & perform filtering
        binding.tietSearch.doOnTextChanged { text, _, _, _ ->
            run {

                houseAdapter.filter.filter(text)
            }
        }

        binding.tietSearch.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tiSearch.startIconDrawable = null
            } else {
                binding.tiSearch.setStartIconDrawable(R.drawable.ic_search)
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
     * which is usually equivalent to the current location of device.
     */
    private fun getLastLocation() {

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
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("OverviewFragment", "getLastLocation() location: $location")

                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    viewModel.userLocation = location
                    //
                    // //We have a location
                    // // Log.d(TAG, "onSuccess: $location")
                    // //
                    // // Log.d(TAG, "onSuccess: " + location.latitude)
                    // userCoordinates.add(location.latitude)
                    //
                    // // Log.d(TAG, "onSuccess: " + location.longitude)
                    // userCoordinates.add(location.longitude)
                    //
                    // // Log.d(TAG, "onSuccess: " + userCoordinates)
                    // houseAdapter.notifyDataSetChanged()

                } /*else {
                    Log.d(TAG, "onSuccess: Location was null...")

                }*/
            }
        return
    }

    private fun askLocationPermission() {
        if (DEBUG) {
            Log.d(TAG, "askLocationPermission()")
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (DEBUG) {
                Log.d(TAG, "askLocationPermission() ACCESS_FINE_GRAINED not granted")
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                if (DEBUG) {
                    Log.d(TAG, "askLocationPermission() shouldShowRequestPermissionRationale")
                }

                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            } else {
                if (DEBUG) {
                    Log.d(TAG, "askLocationPermission() DO NOT shouldShowRequestPermissionRationale")
                }
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
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

        if (DEBUG) {
            Log.d(TAG, "onRequestPermissionsResult() requestCode: $requestCode, results: $permissions, grantResults: $grantResults")
        }

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted
                // Log.d(TAG, "permission granted")
                getLastLocation()
                // Log.d(TAG, "get last location done")


            } else {
                //Permission not granted
            }
        }
    }

    companion object {
        private val TAG = "OverviewFragment"
        private const val DEBUG = true
    }
}
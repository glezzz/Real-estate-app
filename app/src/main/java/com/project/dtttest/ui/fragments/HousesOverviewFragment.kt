package com.project.dtttest.ui.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.project.dtttest.R
import com.project.dtttest.adapters.HouseAdapter
import com.project.dtttest.databinding.FragmentHousesOverviewBinding
import com.project.dtttest.ui.activities.MainActivity
import com.project.dtttest.ui.viewmodels.HouseViewModel
import com.project.dtttest.utils.Constants.Companion.LOCATION_REQUEST_CODE

class HousesOverviewFragment : BaseFragment() {

    lateinit var viewModel: HouseViewModel
    lateinit var houseAdapter: HouseAdapter
    private var _binding: FragmentHousesOverviewBinding? = null
    val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHousesOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()

        viewModel.allHouses.observe(viewLifecycleOwner, Observer { list ->

            houseAdapter.setData(list)
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

        // Hide ic_search to the left in search bar when typing
        binding.tietSearch.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.tiSearch.startIconDrawable = null
            } else {
                binding.tiSearch.setStartIconDrawable(R.drawable.ic_search)
            }
        }

    }

    /**
     * Initializes houseAdapter and sets up the adapter of recyclerview to houseAdapter
     */
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
     * onStart, if permissions have been granted before, get last location, if not, ask permissions again
     */
    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            // If permissions are granted, get last known location
            getLastLocation()

        } else {

            // Else, request permissions them each time onStart() gets called in HousesOverviewFragment
            askLocationPermission()
        }
    }

    /**
     * Get last known location of user's device,
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

        // If permission are granted, get last location
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

                // Initialize userLocation with lastLocation
                if (location != null) {
                    viewModel.userLocation = location
                }
            }
        return
    }

    /**
     *
     */
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

                // I realize this is method deprecated. See my motivation letter
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            } else {

                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    /**
     * Callback for the result from requesting permissions.
     *
     * @param requestCode the request code passed in requestPermissions()
     * @param permissions String: The requested permissions. Never null
     * @param grantResults The grant results for the corresponding permissions which is either PackageManager.PERMISSION_GRANTED or PackageManager.PERMISSION_DENIED. Never null.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Permission granted
                getLastLocation()
            }
        }
    }
}
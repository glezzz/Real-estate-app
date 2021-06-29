package com.project.dtttest.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.project.dtttest.R
import com.project.dtttest.adapters.HouseAdapter
import com.project.dtttest.databinding.ActivityMainBinding
import com.project.dtttest.repository.Repository
import com.project.dtttest.ui.fragments.OverviewFragment

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    private lateinit var navController: NavController
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var houseAdapter: HouseAdapter = HouseAdapter(OverviewFragment(), this)


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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val item = menu?.findItem(R.id.searchView_MenuMain)
        val searchView: SearchView = item?.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                houseAdapter.filter.filter(newText)
                return false
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return true
    }
}
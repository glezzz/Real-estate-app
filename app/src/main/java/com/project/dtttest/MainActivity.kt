package com.project.dtttest

import HouseAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.dtttest.databinding.ActivityMainBinding
import com.project.dtttest.repository.Repository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    lateinit var houseAdapter: HouseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getHouses()
        // viewModel.myResponse.observe(this, Observer { response ->
        //     Log.d("Response", response.id.toString())
        //     Log.d("Response", response.price.toString())
        //     Log.d("Response", response.bedrooms.toString())
        //     Log.d("Response", response.bathrooms.toString())
        // })
    }

    fun initRecyclerView() {
        binding.rvHouses.layoutManager = LinearLayoutManager(this)
        houseAdapter = HouseAdapter()
    }
}
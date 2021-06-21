package com.project.dtttest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.dtttest.R
import com.project.dtttest.adapters.HouseAdapter
import com.project.dtttest.databinding.FragmentOverviewBinding
import com.project.dtttest.ui.MainActivity
import com.project.dtttest.ui.MainViewModel


class OverviewFragment : Fragment(R.layout.fragment_overview) {

    lateinit var viewModel: MainViewModel
    lateinit var houseAdapter: HouseAdapter
    // Redo this the right way VIEW BINDING inside Fragments
    private var binding: FragmentOverviewBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentOverviewBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()


            // findNavController().navigate(
            //     R.id.action_breakingsNewsFragment_to_articleFragment,
            //     bundle
            // )
        }

    private fun setupRecyclerView() {
        houseAdapter = HouseAdapter()
        binding?.rvHouses?.apply {
            adapter = houseAdapter
            layoutManager = LinearLayoutManager(activity)

        }

        // binding.rvHouses.adapter = houseAdapter
        // binding.rvHouses.layoutManager = LinearLayoutManager(this)
    }
}
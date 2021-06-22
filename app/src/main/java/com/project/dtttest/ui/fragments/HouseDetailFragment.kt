package com.project.dtttest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.project.dtttest.R
import com.project.dtttest.databinding.FragmentHouseDetailBinding
import com.project.dtttest.databinding.FragmentOverviewBinding
import com.project.dtttest.ui.MainActivity
import com.project.dtttest.ui.MainViewModel

class HouseDetailFragment : Fragment() {

    private var _binding: FragmentHouseDetailBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: MainViewModel

    val args: HouseDetailFragmentArgs by navArgs()


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
        binding.tvDescription.text = house.description
    }
}
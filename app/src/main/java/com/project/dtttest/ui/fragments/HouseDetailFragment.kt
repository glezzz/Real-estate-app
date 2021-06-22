package com.project.dtttest.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.project.dtttest.R
import com.project.dtttest.databinding.FragmentHouseDetailBinding
import com.project.dtttest.databinding.FragmentOverviewBinding

class HouseDetailFragment : Fragment() {


    private var _binding: FragmentHouseDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHouseDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


}
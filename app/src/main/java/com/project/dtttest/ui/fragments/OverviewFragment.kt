package com.project.dtttest.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.dtttest.R
import com.project.dtttest.adapters.HouseAdapter
import com.project.dtttest.databinding.FragmentOverviewBinding
import com.project.dtttest.ui.MainActivity
import com.project.dtttest.ui.MainViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

// try to load without r.layout
class OverviewFragment : Fragment(R.layout.fragment_overview) {

    lateinit var viewModel: MainViewModel
    lateinit var houseAdapter: HouseAdapter
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!

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

        houseAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("house", it)
            }
            findNavController().navigate(
                R.id.action_overviewFragment_to_houseDetailFragment,
                bundle
            )
        }

        // var job: Job? = null
        // binding.etSearch.addTextChangedListener { editable ->
        //     job?.cancel()
        //     job = MainScope().launch {
        //         editable.let {
        //             if (editable.toString().isNotEmpty()) {
        //                 viewModel.getHouses()
        //             }
        //         }
        //     }
        // }
        //}

        // override fun onCreate(savedInstanceState: Bundle?) {
        //     super.onCreate(savedInstanceState)
        //     binding.etSearch.addTextChangedListener(object : TextWatcher {
        //         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //             TODO("Not yet implemented")
        //         }
        //
        //         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //             TODO("Not yet implemented")
        //         }
        //
        //         override fun afterTextChanged(s: Editable?) {
        //             TODO("Not yet implemented")
        //         }
        //     })


        // val editText: EditText = findViewById(R.id.edittext)
        // editText.addTextChangedListener(object : TextWatcher {
        //     override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        //     override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        //     override fun afterTextChanged(s: Editable) {
        //         filter(s.toString())
        //     }
        // })

    }

    // private fun filter(text: String) {
    //     val filteredList: ArrayList<ExampleItem> = ArrayList()
    // }

    private fun setupRecyclerView() {
        houseAdapter = HouseAdapter()
        binding.rvHouses.apply {
            adapter = houseAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}
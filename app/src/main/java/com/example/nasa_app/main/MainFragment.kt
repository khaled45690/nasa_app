package com.example.nasa_app.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.nasa_app.R
import com.example.nasa_app.databinding.FragmentMainBinding



class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    lateinit var binding :FragmentMainBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel.getToDayImage(binding)
        viewModel.getCachedData(binding)
        viewModel.getNextSevenDaysData(binding)
        binding.viewModel = viewModel

        val adapter = AsteroidAdapter()
        adapter.data = viewModel.nasaRecordedList.value!!
        binding.asteroidRecycler.adapter = adapter

        viewModel.nasaRecordedList.observe(viewLifecycleOwner , Observer {
            adapter.data = it
        })
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu,  inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.filterData(binding , item.itemId)
        return true
    }
}

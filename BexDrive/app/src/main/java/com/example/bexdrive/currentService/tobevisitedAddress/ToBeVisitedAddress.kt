package com.example.bexdrive.currentService.tobevisitedAddress

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bexdrive.DaggerClass

import com.example.bexdrive.R
import com.example.bexdrive.databinding.ToBeVisitedAddressFragmentBinding
import com.example.bexdrive.entity.Service
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.ServiceAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.to_be_visited_address_fragment.*

@AndroidEntryPoint
class ToBeVisitedAddress : Fragment() {

    private val viewModel: ToBeVisitedAddressViewModel by viewModels()
    private lateinit var list: List<Service>
    //lateinit var ToBeVisited_recycler_view : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ToBeVisitedAddressFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences

        binding.btnCheckCurrentService.setOnClickListener {
            binding.ToBeVisitedRecyclerView.visibility = VISIBLE
            binding.btnCheckCurrentService.visibility = GONE
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.serviceListLiveData().observe(viewLifecycleOwner){
            list = it
            DaggerClass.service = list[0]
            val viewManager = LinearLayoutManager(requireContext())
            val serviceAdapter = ServiceAdapter(list[0].Addresses)
            ToBeVisited_recycler_view.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = serviceAdapter
            }
        }

        viewModel.getServiceData()
    }
}

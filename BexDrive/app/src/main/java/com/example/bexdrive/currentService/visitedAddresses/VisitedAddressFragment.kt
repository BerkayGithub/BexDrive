package com.example.bexdrive.currentService.visitedAddresses

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.bexdrive.R
import com.example.bexdrive.databinding.VisitedAddressFragmentBinding
import com.example.bexdrive.entity.Address
import com.example.bexdrive.util.VisitedServiceAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.to_be_visited_address_fragment.*
import kotlinx.android.synthetic.main.visited_address_fragment.*

@AndroidEntryPoint
class VisitedAddressFragment : Fragment() {

    private val viewModel: VisitedAddressViewModel by viewModels()
    private var list: ArrayList<Address> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = VisitedAddressFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.addressListLiveData().observe(viewLifecycleOwner){
            list.clear()
            for(x in it){
                if(x.IsVisited){
                    list.add(x)
                }
            }
            val viewManager = LinearLayoutManager(requireContext())
            val visServiceAdapter = VisitedServiceAdapter(list)
            Visited_recycler_view.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = visServiceAdapter
            }
        }

        viewModel.getServiceAddresses()

        viewModel.errorMessageLiveData().observe(viewLifecycleOwner){
            val mySnackBar = Snackbar.make(layout_visited, it, Snackbar.LENGTH_INDEFINITE)
            mySnackBar.setAction("OK") { mySnackBar.dismiss() }
            mySnackBar.show()
        }
    }

}

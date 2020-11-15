package com.example.bexdrive.currentService.serviceaddressdetail

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs

import com.example.bexdrive.R
import dagger.hilt.android.AndroidEntryPoint
import com.example.bexdrive.databinding.ServiceAddressDetailPageFragmentBinding
import com.example.bexdrive.entity.Address

@AndroidEntryPoint
class ServiceAddressDetailPage : Fragment() {

    private val viewModel: ServiceAddressDetailPageViewModel by viewModels()
    lateinit var address: Address
    //private val args by navArgs<ServiceAddressDetailPageArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ServiceAddressDetailPageFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        //address = args.Address
        //viewModel.addressName = address.Address
        //viewModel.fullAddress = address.PointName
        //viewModel.deliveryDate = "Tahmini teslim tarihi ${address.EstimatedDateDelivered}"

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}

package com.example.bexdrive.currentService.serviceaddressdetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.bexdrive.repository.ProxyRepository

class ServiceAddressDetailPageViewModel @ViewModelInject constructor(
    val repository: ProxyRepository
) : ViewModel() {

    lateinit var addressName: String
    lateinit var fullAddress: String
    lateinit var deliveryDate: String

    fun deliverPackages(){

    }
}

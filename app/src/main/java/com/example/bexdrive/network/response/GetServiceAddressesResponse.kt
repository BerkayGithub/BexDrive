package com.example.bexdrive.network.response

import com.example.bexdrive.entity.Address

data class GetServiceAddressesResponse (
    val ServiceAddresses : List<Address>,
    val Result : Boolean,
    val Message : String,
    val Code : String
)
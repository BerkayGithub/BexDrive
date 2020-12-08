package com.example.bexdrive.network.response

import com.example.bexdrive.entity.Carrier
import com.example.bexdrive.entity.Package

data class GetPackagesWhichDeliverToAddressResponse (
    val Packages: List<Package>,
    val Carriers: List<Carrier>,
    val Result: Boolean,
    val Message: String,
    val Code: String
)
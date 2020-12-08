package com.example.bexdrive.network.response

data class AuthResponse (
    val BasicProxyToken : String?,
    val ProxyServiceUrl : String?,
    val Status : Int?,
    val VehicleID : String?,
    val VehiclePlate : String?,
    val CorporationName : String?,
    val ID : String?,
    val Result : Boolean?,
    val Code : String?,
    val Message : String?
)
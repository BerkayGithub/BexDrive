package com.example.bexdrive.network.response

import com.example.bexdrive.entity.Service

data class GetServiceResponse (
    val Services : List<Service>,
    val Result : Boolean,
    val Message : String,
    val Code : String
    )
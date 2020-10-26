package com.example.bexdrive.network.response

data class CenterTokenResponse (
    val access_token : String,
    val expires_in : Int,
    val token_type : String,
    val Code : String,
    val Message : String
)
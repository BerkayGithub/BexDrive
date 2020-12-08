package com.example.bexdrive.repository

import com.example.bexdrive.network.CenterAPI
import com.example.bexdrive.network.response.AuthResponse
import com.example.bexdrive.network.response.CenterTokenResponse
import retrofit2.Response
import javax.inject.Inject

class CenterRepository @Inject constructor(){

    suspend fun userLogin(Authorization: String, UUID : String, Cordova: String, Manufacturer : String, Platform : String, Serial: String, Version: String) : Response<AuthResponse> {
        return CenterAPI().userLogin(Authorization, UUID, Cordova, Manufacturer, Platform, Serial, Version)
    }

    suspend fun getToken(AuthorizationKey : String) : Response<CenterTokenResponse> {
        return CenterAPI().getToken(AuthorizationKey)
    }
}
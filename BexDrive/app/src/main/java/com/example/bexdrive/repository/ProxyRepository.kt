package com.example.bexdrive.repository

import com.example.bexdrive.network.ProxyAPI
import com.example.bexdrive.network.response.CenterTokenResponse
import com.example.bexdrive.network.response.LoginResponse
import retrofit2.Response
import javax.inject.Inject

class ProxyRepository @Inject constructor() {

    suspend fun userLogin(Authorization : String, UserName : String, Password : String) : Response<LoginResponse>{
        return ProxyAPI().userLogin(Authorization, UserName, Password)
    }

    suspend fun getToken(AuthorizationKey : String) : Response<CenterTokenResponse> {
        return ProxyAPI().getToken(AuthorizationKey)
    }
}
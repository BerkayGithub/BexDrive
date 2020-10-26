package com.example.bexdrive.network

import com.example.bexdrive.network.response.CenterTokenResponse
import com.example.bexdrive.network.response.LoginResponse
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface ProxyAPI {

    @POST("api/auth/token/")
    suspend fun getToken(
        @Header("Authorization") Authorization : String
    ) : Response<CenterTokenResponse>

    @FormUrlEncoded
    @POST("api/authorization/login/")
    suspend fun userLogin(
        @Header("Authorization") Authorization: String,
        @Field("UserName") UserName : String,
        @Field("Password") Password : String
    ) : Response<LoginResponse>

    companion object{
        operator fun invoke() : ProxyAPI{
            val gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl("http://bex.drive.proxyapi.bexfa.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ProxyAPI::class.java)
        }
    }
}
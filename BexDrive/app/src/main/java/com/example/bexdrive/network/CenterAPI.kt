package com.example.bexdrive.network

import com.example.bexdrive.network.response.AuthResponse
import com.example.bexdrive.network.response.CenterTokenResponse
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface CenterAPI {

    @FormUrlEncoded
    @POST("api/device/checkDeviceRegistration/")
    suspend fun userLogin(
        @Header("Authorization") Authorization: String,
        @Field("UUID") UUID : String,
        @Field("Cordova") Cordova : String,
        @Field("Manufacturer") Manufacturer : String,
        @Field("Platform") Platform : String,
        @Field("Serial") Serial : String,
        @Field("Version") Version : String
    ) : Response<AuthResponse>


    @POST("api/auth/token/")
    suspend fun getToken(
        @Header("Authorization") Authorization : String
    ) : Response<CenterTokenResponse>

    companion object{
        operator fun invoke() : CenterAPI{
            val gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl("http://bex.drive.centerapi.bexfa.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(CenterAPI::class.java)
        }
    }
}
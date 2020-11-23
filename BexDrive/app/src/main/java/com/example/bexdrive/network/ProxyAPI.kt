package com.example.bexdrive.network

import com.example.bexdrive.entity.Location
import com.example.bexdrive.network.response.*
import com.example.bexdrive.util.DateDeserializer
import com.example.bexdrive.util.Interceptors
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*
import java.util.concurrent.TimeUnit


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

    @FormUrlEncoded
    @POST("api/tms/getservices/")
    suspend fun getServices(
        @Header("Authorization") Authorization: String,
        @Field("VehicleID") VehicleID : String,
        @Field("OnlyCurrentService") OnlyCurrentService : Boolean
    ) : Response<GetServiceResponse>

    @FormUrlEncoded
    @POST("api/tms/checkservicepackageandconfirmassignment")
    suspend fun checkservicepackageandconfirmassignment(
        @Header("Authorization") Authorization: String,
        @Field("PackageCode") PackageCode : String,
        @Field("ServiceID") ServiceID : String
    ) : Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/tms/getservicepackages")
    suspend fun getServicePackages(
        @Header("Authorization") Authorization: String,
        @Field("ServiceID") ServiceID: String
    ) : Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/tms/getserviceaddresses")
    suspend fun getServiceAddresses(
        @Header("Authorization") Authorization: String,
        @Field("ServiceID") ServiceID: String
    ) : Response<GetServiceAddressesResponse>

    @FormUrlEncoded
    @POST("api/tms/startservice")
    suspend fun startService(
        @Header("Authorization") Authorization: String,
        @Field("ServiceID") ServiceID: String,
        @Field("CurrentLocation") CurrentLocation: Location
    ) : Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/tms/endservice")
    suspend fun endService(
        @Header("Authorization") Authorization: String,
        @Field("ServiceID") ServiceID: String,
        @Field("CurrentLocation") CurrentLocation: Location
    ) : Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/tms/deliverpackagestoadress")
    suspend fun deliverPackagesToAddress(
        @Header("Authorization") Authorization: String,
        @Field("ServiceID") ServiceID: String,
        @Field("AddressID") AddressID: String,
        @Field("CurrentLocation") CurrentLocation: Location
    ) : Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/tms/getpackageswhichdelivertoadress")
    suspend fun getPackagesWhichDeliverToAddress(
        @Header("Authorization") Authorization: String,
        @Field("ServiceID") ServiceID: String,
        @Field("AddressID") AddressID: String
    ) : Response<GetPackagesWhichDeliverToAddressResponse>

    @FormUrlEncoded
    @POST("api/tms/recalculateroute")
    suspend fun recalculateroute(
        @Header("Authorization") Authorization: String,
        @Field("ServiceID") ServiceID: String,
        @Field("CurrentLocation") CurrentLocation: Location
    ) : Response<SimpleResponse>

    @FormUrlEncoded
    @POST("api/tms/searchpoints")
    suspend fun searchPoints(
        @Header("Authorization") Authorization: String,
        @Field("Name") Name: String
    ) : Response<SearchPointsResponse>

    @FormUrlEncoded
    @POST("api/tms/updatepointlocation")
    suspend fun updatePointLocation(
        @Header("Authorization") Authorization: String,
        @Field("PointID") PointID: String,
        @Field("Location") Location: Location
    ) : Response<SimpleResponse>

    companion object{
        operator fun invoke() : ProxyAPI{
            val gsonBuilder = GsonBuilder()
            gsonBuilder.registerTypeAdapter(Date::class.java, DateDeserializer())
            val gson = gsonBuilder.setLenient().create()

            val headerInterceptor = Interceptors()
            val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .callTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(headerInterceptor)
                .addInterceptor(logger)

            return Retrofit.Builder()
                .baseUrl("http://bex.drive.proxyapi.bexfa.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build())
                .build()
                .create(ProxyAPI::class.java)
        }
    }
}
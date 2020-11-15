package com.example.bexdrive.repository

import com.example.bexdrive.entity.Location
import com.example.bexdrive.network.ProxyAPI
import com.example.bexdrive.network.response.*
import retrofit2.Response
import javax.inject.Inject

class ProxyRepository @Inject constructor() {

    suspend fun userLogin(Authorization : String, UserName : String, Password : String) : Response<LoginResponse>{
        return ProxyAPI().userLogin(Authorization, UserName, Password)
    }

    suspend fun getToken(AuthorizationKey : String) : Response<CenterTokenResponse> {
        return ProxyAPI().getToken(AuthorizationKey)
    }

    suspend fun getServices(AuthorizationKey: String, VehicleID: String, OnlyCurrentService: Boolean): Response<GetServiceResponse> {
        return ProxyAPI().getServices(AuthorizationKey, VehicleID, OnlyCurrentService)
    }

    suspend fun checkServicePackageAndConfirmAssignment(AuthorizationKey: String, PackageCode: String, ServiceID: String): Response<SimpleResponse>{
        return ProxyAPI().checkservicepackageandconfirmassignment(AuthorizationKey, PackageCode, ServiceID)
    }

    suspend fun getServicePackages(AuthorizationKey: String, ServiceID: String) : Response<SimpleResponse>{
        return ProxyAPI().getServicePackages(AuthorizationKey, ServiceID)
    }

    suspend fun getServiceAddresses(AuthorizationKey: String, ServiceID: String) : Response<GetServiceAddressesResponse>{
        return ProxyAPI().getServiceAddresses(AuthorizationKey, ServiceID)
    }

    suspend fun startService(AuthorizationKey: String, ServiceID: String, CurrentLocation: Location) : Response<SimpleResponse>{
        return ProxyAPI().startService(AuthorizationKey, ServiceID, CurrentLocation)
    }

    suspend fun endService(AuthorizationKey: String, ServiceID: String, CurrentLocation: Location) : Response<SimpleResponse>{
        return ProxyAPI().endService(AuthorizationKey, ServiceID, CurrentLocation)
    }

    suspend fun deliverPackagesToAddress(AuthorizationKey: String, ServiceID: String, AddressID: String, CurrentLocation: Location) : Response<SimpleResponse>{
        return ProxyAPI().deliverPackagesToAddress(AuthorizationKey, ServiceID, AddressID, CurrentLocation)
    }

    suspend fun getPackagesWhichDeliverToAddress(AuthorizationKey: String, ServiceID: String, AddressID: String): Response<GetPackagesWhichDeliverToAddressResponse>{
        return ProxyAPI().getPackagesWhichDeliverToAddress(AuthorizationKey, ServiceID, AddressID)
    }

    suspend fun reCalculateRoute(AuthorizationKey: String, ServiceID: String, CurrentLocation: Location): Response<SimpleResponse>{
        return ProxyAPI().recalculateroute(AuthorizationKey, ServiceID, CurrentLocation)
    }

    suspend fun searchPoints(AuthorizationKey: String, Name: String): Response<SearchPointsResponse>{
        return ProxyAPI().searchPoints(AuthorizationKey, Name)
    }

    suspend fun updatePointLocation(AuthorizationKey: String, PointID: String, Location: Location): Response<SimpleResponse>{
        return ProxyAPI().updatePointLocation(AuthorizationKey, PointID, Location)
    }
}
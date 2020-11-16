package com.example.bexdrive

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import com.example.bexdrive.entity.Service

@HiltAndroidApp
class DaggerClass : Application(){

    companion object{
        var vehicleID: String? = null
        var vehiclePlate: String? = null
        var deviceID: String? = null
        var corporationName: String? = null
        var employeeID: Int? = null
        var employeeName: String? = null
        var service: List<Service>? = null
    }
}
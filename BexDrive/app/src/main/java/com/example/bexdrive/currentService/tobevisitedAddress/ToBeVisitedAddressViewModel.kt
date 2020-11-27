package com.example.bexdrive.currentService.tobevisitedAddress

import android.content.SharedPreferences
import android.icu.text.TimeZoneFormat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.entity.Location
import com.example.bexdrive.entity.Service
import com.example.bexdrive.repository.ProxyRepository
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat

class ToBeVisitedAddressViewModel @ViewModelInject constructor(
    var repository : ProxyRepository
) : ViewModel() {

    lateinit var sharedPreferences: SharedPreferences
    var responseMessage : MutableLiveData<String> = MutableLiveData("")
    var estimatedStartTime : MutableLiveData<String> = MutableLiveData("")
    var estimatedEndTime: MutableLiveData<String> = MutableLiveData("")
    var serviceStateMessage: MutableLiveData<String> = MutableLiveData()

    private var serviceList: MutableLiveData<List<Service>> = MutableLiveData()

    fun serviceListLiveData(): LiveData<List<Service>> = serviceList
    fun serviceStateMessageLiveData(): LiveData<String> = serviceStateMessage

    fun getServiceData(){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }
        var serviceValues : List<Service>//list of services will be taken from the getservices

        viewModelScope.launch {
            //send request to the web service
            if (DaggerClass.service != null){
                serviceList.postValue(DaggerClass.service)
                val startdate = DaggerClass.service!![0].EstimatedTimeStarts
                val enddate = DaggerClass.service!![0].EstimatedTimeEnds
                val estStartDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(startdate)
                val estEndDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(enddate)
                estimatedStartTime.postValue("Tahmini başlama tarihi: $estStartDate")
                estimatedEndTime.postValue("Tahmini bitiş tarihi: $estEndDate")
            }
            else {
                val getServiceResponse = repository.getServices(
                    "Bearer $bearerToken",
                    DaggerClass.vehicleID.toString(),
                    true
                )
                if (getServiceResponse.isSuccessful) {
                    if (getServiceResponse.body()!!.Result) {
                        responseMessage.postValue(getServiceResponse.body()!!.Message)
                        serviceValues = getServiceResponse.body()!!.Services
                        serviceList.postValue(serviceValues)
                        val date = serviceValues[0].EstimatedTimeStarts
                        val enddate = serviceValues[0].EstimatedTimeEnds
                        val estStartDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(date)
                        val estEndDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(enddate)
                        estimatedStartTime.postValue("Tahmini başlama tarihi: $estStartDate")
                        estimatedEndTime.postValue("Tahmini bitiş tarihi: $estEndDate")
                    } else {
                        responseMessage.postValue(getServiceResponse.body()!!.Message)
                    }
                }
            }
        }
    }

    fun startService(){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }

        viewModelScope.launch {
            val startServiceResponse = repository.startService("Bearer $bearerToken", DaggerClass.service!![0].ServiceID, DaggerClass.location!!)
            if (startServiceResponse.isSuccessful){
                if (startServiceResponse.body()!!.Result){
                    serviceStateMessage.postValue("Servis Başlatıldı.")

                    val serviceValues : List<Service>

                    val getServiceResponse = repository.getServices(
                        "Bearer $bearerToken",
                        DaggerClass.vehicleID.toString(),
                        true
                    )
                    if (getServiceResponse.isSuccessful) {
                        if (getServiceResponse.body()!!.Result) {
                            responseMessage.postValue(getServiceResponse.body()!!.Message)
                            serviceValues = getServiceResponse.body()!!.Services
                            serviceList.postValue(serviceValues)
                            val date = serviceValues[0].EstimatedTimeStarts
                            val enddate = serviceValues[0].EstimatedTimeEnds
                            val estStartDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(date)
                            val estEndDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(enddate)
                            estimatedStartTime.postValue("Tahmini başlama tarihi: $estStartDate")
                            estimatedEndTime.postValue("Tahmini bitiş tarihi: $estEndDate")
                        } else {
                            responseMessage.postValue(getServiceResponse.body()!!.Message)
                        }
                    }

                }else{
                    serviceStateMessage.postValue(startServiceResponse.body()!!.Message)
                }
            }else{
                serviceStateMessage.postValue("Error : ${startServiceResponse.code()} \n Servise ulaşırken hata oluştu!")
            }
        }
    }

    fun endService(){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }

        viewModelScope.launch {
            val endServiceResponse = repository.endService("Bearer $bearerToken", DaggerClass.service!![0].ServiceID, DaggerClass.location!!)
            if(endServiceResponse.isSuccessful){
                if (endServiceResponse.body()!!.Result){
                    serviceStateMessage.postValue("Servis Bitirildi.")

                    val serviceValues : List<Service>

                    val getServiceResponse = repository.getServices(
                        "Bearer $bearerToken",
                        DaggerClass.vehicleID.toString(),
                        true
                    )
                    if (getServiceResponse.isSuccessful) {
                        if (getServiceResponse.body()!!.Result) {
                            responseMessage.postValue(getServiceResponse.body()!!.Message)
                            serviceValues = getServiceResponse.body()!!.Services
                            serviceList.postValue(serviceValues)
                            val date = serviceValues[0].EstimatedTimeStarts
                            val enddate = serviceValues[0].EstimatedTimeEnds
                            val estStartDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(date)
                            val estEndDate = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(enddate)
                            estimatedStartTime.postValue("Tahmini başlama tarihi: $estStartDate")
                            estimatedEndTime.postValue("Tahmini bitiş tarihi: $estEndDate")
                        } else {
                            responseMessage.postValue(getServiceResponse.body()!!.Message)
                        }
                    }

                }else {
                    serviceStateMessage.postValue(endServiceResponse.body()!!.Message)
                }
            }else {
                serviceStateMessage.postValue("Error : ${endServiceResponse.code()} \n Servise ulaşırken hata oluştu!")
            }
        }
    }
}

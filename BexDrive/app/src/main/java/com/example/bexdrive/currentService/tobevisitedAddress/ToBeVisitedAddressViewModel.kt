package com.example.bexdrive.currentService.tobevisitedAddress

import android.content.SharedPreferences
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

class ToBeVisitedAddressViewModel @ViewModelInject constructor(
    var repository : ProxyRepository
) : ViewModel() {

    lateinit var sharedPreferences: SharedPreferences
    var responseMessage : MutableLiveData<String> = MutableLiveData("")
    var estimatedStartTime : MutableLiveData<String> = MutableLiveData("")

    private var serviceList: MutableLiveData<List<Service>> = MutableLiveData()

    fun serviceListLiveData(): LiveData<List<Service>> = serviceList

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
                val estStartDate = DaggerClass.service!![0].EstimatedTimeStarts.toLocaleString()
                estimatedStartTime.postValue("Tahmini başlama tarihi: $estStartDate")
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
                        val estStartDate = serviceValues[0].EstimatedTimeStarts.toLocaleString()
                        estimatedStartTime.postValue("Tahmini başlama tarihi: $estStartDate")
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
    }
}

package com.example.bexdrive.currentService.detail

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.entity.Service
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.SingleLiveEvent
import kotlinx.coroutines.launch
import java.text.DateFormat

class DetailViewModel @ViewModelInject constructor(
    var repository: ProxyRepository
): ViewModel() {

    lateinit var sharedPreferences: SharedPreferences
    var estStartDate : MutableLiveData<String> = MutableLiveData("")
    var startDate : MutableLiveData<String> = MutableLiveData("")
    var estEndDate : MutableLiveData<String> = MutableLiveData("")
    var endDate : MutableLiveData<String> = MutableLiveData("")
    var numOfPackages : MutableLiveData<String> = MutableLiveData("")
    var numOfPoints : MutableLiveData<String> = MutableLiveData("")

    private var _errorMessage : MutableLiveData<String> = MutableLiveData()
    private var _progressBarMessage : SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun errorMessageLiveData() : LiveData<String> = _errorMessage
    fun progressBarLiveData() : LiveData<Boolean> = _progressBarMessage

    fun getService(){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }

        var serviceValues : List<Service>

        viewModelScope.launch {
            val response = repository.getServices(
                "Bearer $bearerToken",
                DaggerClass.vehicleID.toString(),
                true)
            if(response.isSuccessful){
                if (response.body()!!.Result){
                    serviceValues = response.body()!!.Services
                    DaggerClass.service = serviceValues
                    _progressBarMessage.postValue(true)
                    estStartDate.postValue(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(serviceValues[0].EstimatedTimeStarts))
                    startDate.postValue(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(serviceValues[0].TimeStarts))
                    estEndDate.postValue(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(serviceValues[0].EstimatedTimeEnds))
                    endDate.postValue(DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(serviceValues[0].TimeEnds))
                    numOfPackages.postValue(serviceValues[0].TotalPackageCount.toString())
                    numOfPoints.postValue(serviceValues[0].PointCount.toString())
                }
            }else{
                _errorMessage.postValue("Sunucuya bağlanırken hata oluştu!\nError Code : ${response.code()} ")
            }
        }
    }

}

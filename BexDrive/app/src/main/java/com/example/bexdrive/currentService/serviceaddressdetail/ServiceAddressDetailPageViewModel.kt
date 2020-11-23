package com.example.bexdrive.currentService.serviceaddressdetail

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.entity.Location
import com.example.bexdrive.entity.Package
import com.example.bexdrive.repository.ProxyRepository
import kotlinx.coroutines.launch

class ServiceAddressDetailPageViewModel @ViewModelInject constructor(
    val repository: ProxyRepository
) : ViewModel() {

    lateinit var addressName: String
    lateinit var fullAddress: String
    lateinit var deliveryDate: String

    lateinit var ServiceID: String
    lateinit var AddressID: String
    lateinit var CurrentLocation: Location

    lateinit var sharedPreferences: SharedPreferences

    private var _packageList: MutableLiveData<List<Package>> = MutableLiveData()
    private val _errorMessage: MutableLiveData<String> = MutableLiveData()
    private val _successMessage: MutableLiveData<String> = MutableLiveData()

    fun packageListLiveData(): LiveData<List<Package>> = _packageList
    fun errorMessageLiveData(): LiveData<String> = _errorMessage
    fun successMessageLiveData(): LiveData<String> = _successMessage

    fun getPackagesWhichDeliverToAddress(ServiceID: String, AddressID: String){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }

        viewModelScope.launch {
            val response = repository.getPackagesWhichDeliverToAddress("Bearer $bearerToken", ServiceID, AddressID)
            if(response.isSuccessful){
                if(response.body()!!.Result){
                    _packageList.postValue(response.body()!!.Packages)
                }else{
                    _errorMessage.postValue(response.body()!!.Message)
                }
            }else{
                _errorMessage.postValue("Error code: ${response.code()} \n Servise bağlanırken bir hata oluştu!")
            }
        }
    }

    fun deliverPackagesToAddress(){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }

        viewModelScope.launch {
            val response = repository.deliverPackagesToAddress("Bearer $bearerToken", ServiceID, AddressID, CurrentLocation)
            if (response.isSuccessful){
                if (response.body()!!.Result){
                    _successMessage.postValue("Paketler teslim edildi.")
                }else {
                    _errorMessage.postValue(response.body()!!.Message)
                }
            }else{
                _errorMessage.postValue("Error code: ${response.code()} \n Servise bağlanırken bir hata oluştu!")
            }
        }
    }
}

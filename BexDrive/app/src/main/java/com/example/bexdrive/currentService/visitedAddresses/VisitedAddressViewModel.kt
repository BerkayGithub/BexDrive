package com.example.bexdrive.currentService.visitedAddresses

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.entity.Address
import com.example.bexdrive.repository.ProxyRepository
import kotlinx.coroutines.launch

class VisitedAddressViewModel @ViewModelInject constructor(
    var repository: ProxyRepository
): ViewModel() {

    lateinit var sharedPreferences: SharedPreferences

    private var addressList: MutableLiveData<List<Address>> = MutableLiveData()

    private var _errorMessage: MutableLiveData<String> = MutableLiveData()

    fun addressListLiveData() : LiveData<List<Address>> = addressList
    fun errorMessageLiveData() : LiveData<String> = _errorMessage

    fun getServiceAddresses(){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }

        if(DaggerClass.service != null){
            addressList.postValue(DaggerClass.service!![0].Addresses)
        }else{
            _errorMessage.postValue("Mevcut Servisiniz bulunmamaktadır.")
        }

        /*viewModelScope.launch {
            val response = repository.getServiceAddresses("Bearer $bearerToken", DaggerClass.service!![0].ServiceID)
            if (response.isSuccessful){
                if (response.body()!!.Result){
                    addressList.postValue(response.body()!!.ServiceAddresses)
                }else{
                    _errorMessage.postValue(response.body()!!.Message)
                }
            }else{
                _errorMessage.postValue("Error : ${response.code()} \n Servise ulasırken bir hata oluştu!")
            }
        }*/
    }
}

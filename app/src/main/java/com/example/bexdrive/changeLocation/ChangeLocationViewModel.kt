package com.example.bexdrive.changeLocation

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.entity.Point
import com.example.bexdrive.repository.ProxyRepository
import kotlinx.coroutines.launch

class ChangeLocationViewModel @ViewModelInject constructor(
    val repository: ProxyRepository
) : ViewModel() {

    var name : String = ""
    private val pointList : MutableLiveData<List<Point>> = MutableLiveData()

    lateinit var sharedPreferences: SharedPreferences

    private val _errorMessage : MutableLiveData<String> = MutableLiveData()

    fun errorMessageLiveData() = _errorMessage
    fun pointListLiveData() = pointList

    fun searchPoints(){
        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            return
        }

        if (name.isNullOrEmpty()){
            _errorMessage.postValue("You must enter a name!")
            return
        }

        var pointValues : List<Point>

        viewModelScope.launch {
            val searchPointResponse = repository.searchPoints("Bearer $bearerToken", name)
            if (searchPointResponse.isSuccessful){
                if (searchPointResponse.body()!!.Result){
                    pointValues = searchPointResponse.body()!!.Points
                    pointList.postValue(pointValues)
                }else {
                    _errorMessage.postValue("Hata : ${searchPointResponse.body()!!.Message}")
                }
            }else {
                _errorMessage.postValue("Error code : ${searchPointResponse.body()!!.Code} \n Servise bağlanırken hata oluştu.")
            }
        }
    }

}

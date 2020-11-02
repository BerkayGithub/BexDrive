package com.example.bexdrive.register

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.listener.RegisterListener
import com.example.bexdrive.network.response.AuthResponse
import com.example.bexdrive.network.response.CenterTokenResponse
import com.example.bexdrive.network.response.LoginResponse
import com.example.bexdrive.repository.CenterRepository
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.Coroutine
import com.example.bexdrive.util.SingleLiveEvent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.nio.charset.StandardCharsets
import java.util.*

class RegisterViewModel @ViewModelInject constructor(
    @ApplicationContext private val context: Context,
    private val repository: CenterRepository
) : ViewModel() {

    var username : String? = null
    var password : String? = null
    var data : String? = null
    var activity : Activity? = null
    var basicProxyToken = String()
    var status = -1

    private val _successLiveEvent: MutableLiveData<String> = MutableLiveData("")
    private val _message: MutableLiveData<String> = MutableLiveData("")
    private val _navigateLoginPageLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun successLiveData(): LiveData<String> = _successLiveEvent
    fun navigateLoginPageLiveData(): LiveData<Boolean> = _navigateLoginPageLiveEvent
    fun messageLiveData(): LiveData<String> = _message

//    fun openLoginFragment(){
//        _navigateLoginPageLiveEvent.postValue(true)
//    }


    fun onRegisterButtonClicked(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            _successLiveEvent.postValue("Invalid username or password!")
            return
        }

        viewModelScope.launch {
            //First get the arguments to give to the services
            val text = "$username:$password"
            val base64Str : String

            val UUID = Build.ID
            val manufacturer = Build.MANUFACTURER
            var serial = ""
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                serial = Build.getSerial()
                val bytes = text.toByteArray()
                base64Str = String(Base64.getEncoder().encode(bytes))
            } else{
                serial = Build.SERIAL
                val data = text.toByteArray(StandardCharsets.UTF_8)
                base64Str = android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP)
            }

            //Now call the centerApi/token and centerApi/checkRegisterDevice services
            var registerResponse: Response<AuthResponse>? = null
            withContext(Dispatchers.IO) {
                //Get the token from centerApi
                val getTokenResponse = repository.getToken("Basic $base64Str")
                if (getTokenResponse.isSuccessful) {
                    val accessToken = getTokenResponse.body()!!.access_token
                    //Start working the checkDeviceRegistration service
                    registerResponse = repository.userLogin("Bearer $accessToken", UUID, "", manufacturer, "Android", serial, "1")
                } else {
                    _successLiveEvent.postValue("Error code : ${getTokenResponse.code()}")
                }
            }

            registerResponse?.let {registerResponse ->
                if (registerResponse.isSuccessful){
                    registerResponse.body()?.apply {
                        if (Result == true){
                            status = this.Status!!
                            if(status == 2){//passive
                                _message.postValue("Bu cihaz pasiftir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                                Log.e("Register Response : " , "Bu cihaz pasiftir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n" +
                                        " Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                            }
                            else if(status == 1){//active
                                DaggerClass.vehicleID = this.VehicleID
                                DaggerClass.vehiclePlate = this.VehiclePlate
                                DaggerClass.deviceID = this.ID
                                DaggerClass.corporationName = this.CorporationName
                                basicProxyToken = this.BasicProxyToken.toString()
                                _navigateLoginPageLiveEvent.postValue(true)
                            }
                            else if(status == 0){//created
                                _message.postValue("Cihaz sisteme eklenmiştir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n" +
                                        " Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                                Log.e("Register Response : " , "Cihaz sisteme eklenmiştir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n" +
                                        " Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                            }
                            //Register başarıyla bittiyse proxyAPI'dan token almak için api/auth/token servisini çalıştır.
                        }else{
                            _successLiveEvent.postValue("Cihaz kayıt edilemedi!")
                            print(registerResponse.body()!!.Message)
                        }
                    }

                }else{
                    _successLiveEvent.postValue("Error code : ${registerResponse.code()}")
                }
            }
        }
    }

    /*fun TokenDeneme() {
        viewModelScope.launch {
            val text = "$username:$password"
            val base64Str : String

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val bytes = text.toByteArray()
                base64Str = String(Base64.getEncoder().encode(bytes))
            } else{
                val data = text.toByteArray(StandardCharsets.UTF_8)
                base64Str = android.util.Base64.encodeToString(data, android.util.Base64.NO_WRAP)
            }

            withContext(Dispatchers.IO) {
                //Get the token from centerApi
                val getTokenResponse = repository.getToken("Basic $base64Str")
                if (getTokenResponse.isSuccessful) {
                    val accessToken = getTokenResponse.body()!!.access_token
                    _successLiveEvent.postValue("Api Key : $accessToken")
                } else {
                    _successLiveEvent.postValue("Error code : ${getTokenResponse.code()}")
                }
            }
        }
    }*/
}
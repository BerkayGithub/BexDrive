package com.example.bexdrive.register

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.BuildConfig
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.network.response.AuthResponse
import com.example.bexdrive.repository.CenterRepository
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

    var username : String = ""
    var password : String = ""
    var data : String = ""
    var basicProxyToken : String = ""
    private var status = -1

    var inputName : String = ""
    var inputPassword : String = ""

    lateinit var sharedPreferences: SharedPreferences

    var message = ""

    private val _successLiveEvent: MutableLiveData<String> = MutableLiveData()
    private val _message: MutableLiveData<String> = MutableLiveData()
    private val _snackbarLiveEvent: MutableLiveData<String> = MutableLiveData()
    private val _navigateLoginPageLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val _registerProgress: MutableLiveData<Int> = MutableLiveData()

    fun successLiveData(): LiveData<String> = _successLiveEvent
    fun navigateLoginPageLiveData(): LiveData<Boolean> = _navigateLoginPageLiveEvent
    fun messageLiveData(): LiveData<String> = _message
    fun snackbarLiveData(): LiveData<String> = _snackbarLiveEvent
    fun registerProgressLiveData(): LiveData<Int> = _registerProgress

    fun onRegisterButtonClicked(){
        if(username.isEmpty() || password.isEmpty()){
            _successLiveEvent.postValue("Username and Password can't be empty!")
            return
        }

        inputName = username
        inputPassword = password
        checkDeviceRegister()
    }

    fun checkDeviceRegister(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        viewModelScope.launch {
            _registerProgress.postValue(1)
            //First get the arguments to give to the services
            val text = "$inputName:$inputPassword"
            val base64Str : String

            val UUID = Build.ID
            val manufacturer = Build.MANUFACTURER
            val version = BuildConfig.VERSION_NAME
            val serial : String
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
                    registerResponse = repository.userLogin("Bearer $accessToken", UUID, "", manufacturer, "Android", serial, version)
                } else {
                    _registerProgress.postValue(0)
                    _snackbarLiveEvent.postValue("Error code : ${getTokenResponse.code()}  \n" +
                            "Servise bağlanırken hata oluştu!")
                }
            }

            registerResponse?.let {registerResponse ->
                if (registerResponse.isSuccessful){
                    registerResponse.body()?.apply {
                        if (Result == true){
                            status = this.Status!!
                            when (status) {
                                2 -> {//passive
                                    _registerProgress.postValue(0)
                                    _message.postValue("Bu cihaz pasiftir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                                    Log.e("Register Response : " , "Bu cihaz pasiftir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n" +
                                            " Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                                }
                                1 -> {//active
                                    DaggerClass.vehicleID = this.VehicleID
                                    DaggerClass.vehiclePlate = this.VehiclePlate
                                    DaggerClass.deviceID = this.ID
                                    DaggerClass.corporationName = this.CorporationName
                                    basicProxyToken = this.BasicProxyToken.toString()

                                    val editor : SharedPreferences.Editor = sharedPreferences.edit()
                                    editor.putString("DeviceName", inputName)
                                    editor.putString("DevicePassword", inputPassword)
                                    editor.apply()

                                    _navigateLoginPageLiveEvent.postValue(true)
                                }
                                0 -> {//created
                                    _registerProgress.postValue(0)
                                    _message.postValue("Cihaz sisteme eklenmiştir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n" +
                                            " Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                                    Log.e("Register Response : " , "Cihaz sisteme eklenmiştir. Lütfen sistem yöneticisinden cihazı aktif cihazlar listesine eklemesini söyleyiniz : Serial : $serial.\n" +
                                            " Tanımlama işlemi tamamlandıktan sonra cihazı kapatıp açınız.")
                                }
                                //Register başarıyla bittiyse proxyAPI'dan token almak için api/auth/token servisini çalıştır.
                            }
                            //Register başarıyla bittiyse proxyAPI'dan token almak için api/auth/token servisini çalıştır.
                        }else{
                            _registerProgress.postValue(0)
                            _snackbarLiveEvent.postValue("Cihaz kayıt edilemedi!")
                        }
                    }

                }else{
                    _registerProgress.postValue(0)
                    _snackbarLiveEvent.postValue("Error code : ${registerResponse.code()} \nServise bağlanırken hata oluştu!")
                }
            }
        }
    }
}
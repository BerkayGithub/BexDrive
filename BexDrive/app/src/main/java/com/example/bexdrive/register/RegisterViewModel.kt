package com.example.bexdrive.register

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.example.bexdrive.R
import com.example.bexdrive.listener.RegisterListener
import com.example.bexdrive.login.LoginFragment
import com.example.bexdrive.repository.CenterRepository
import com.example.bexdrive.util.Coroutine
import kotlinx.android.synthetic.main.register_fragment.*
import java.util.*
import javax.inject.Inject

class RegisterViewModel @ViewModelInject constructor(
    application: Application,
    private val repository: CenterRepository
) : AndroidViewModel(application) {

    lateinit var registerListener: RegisterListener
    var username : String? = null
    var password : String? = null
    var data : String? = null
    var activity : Activity? = null
    var context : Context? = null
    var manufacturer : String? = null
    var serial : String? = null
    var UUID : String? = null

    val varNav1 = MutableLiveData<Boolean>()

    init{
        varNav1.value = true
    }

    fun changNavValue() : LiveData<Boolean> {
        if(varNav1.value == true)
            varNav1.value = false
        else
            varNav1.value = true
        return varNav1
    }

    fun openLoginFragment(view: View){
        changNavValue()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onRegisterButtonClicked(view: View){

        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            registerListener.onFailure("Invalid username or password!")
            return
        }

        Coroutine.main {
            val text = "$username:$password"
            val bytes = text.toByteArray()
            val base64Str = String(Base64.getEncoder().encode(bytes))

            //Get the token from centerApi
            val getTokenResponse = repository.getToken("Basic $base64Str")
            if(getTokenResponse.isSuccessful){
                val accessToken = getTokenResponse.body()!!.access_token
                registerListener.onSuccess("Api Key $accessToken")

                //Start working the checkDeviceRegistration service
                val registerResponse = repository.userLogin("Bearer $accessToken", UUID!!, "", manufacturer!!, "Android", serial!!, "1")
                if (registerResponse.isSuccessful){
                    if (registerResponse.body()?.Result == true){
                        registerListener.onSuccess("Cihaz başarıyla kayıt edildi.")
                        println(registerResponse.body()!!.BasicProxyToken)
                        println(registerResponse.body()!!.ProxyServiceUrl)
                        println(registerResponse.body()!!.Status)
                        println(registerResponse.body()!!.VehicleID)
                        println(registerResponse.body()!!.VehiclePlate)
                        println(registerResponse.body()!!.CorporationName)
                        println(registerResponse.body()!!.ID)
                        println(registerResponse.body()!!.Result)
                        println(registerResponse.body()!!.Code)
                        println(registerResponse.body()!!.Message)

                    }else{
                        registerListener.onFailure("Cihaz kayıt edilemedi!")
                        print(registerResponse.body()!!.Message)
                    }
                }else{
                    registerListener.onFailure("Error code : ${registerResponse.code()}")
                }

            }else{
                registerListener.onFailure("Error code : ${getTokenResponse.code()}")
            }
        }
    }

}
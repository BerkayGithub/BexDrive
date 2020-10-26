package com.example.bexdrive.login

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.bexdrive.listener.RegisterListener
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.Coroutine
import java.util.*

class LoginViewModel @ViewModelInject constructor(
    application: Application,
    private val repository : ProxyRepository
) : ViewModel() {

    lateinit var registerListener: RegisterListener
    var username : String? = null
    var password : String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun onLoginButtonClicked(){
        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            registerListener.onFailure("Invalid username or password!")
            return
        }

        Coroutine.main{
            val text = "$username:$password"
            val bytes = text.toByteArray()
            val base64Str = String(Base64.getEncoder().encode(bytes))

            //Get the token from centerApi
            val getTokenResponse = repository.getToken("Basic $base64Str")
            if(getTokenResponse.isSuccessful){
                val accessToken = getTokenResponse.body()!!.access_token
                registerListener.onSuccess("Api Key $accessToken")

                //Start working the authorization service
                val loginResponse = repository.userLogin("Bearer $accessToken", username!!, password!!)
                if (loginResponse.isSuccessful){
                    if (loginResponse.body()?.Result == true){
                        registerListener.onSuccess("Giriş başarılı.")

                    }else{
                        registerListener.onFailure("Giriş başarısız!")
                        println(loginResponse.body()!!.Message)
                    }
                }else{
                    registerListener.onFailure("Error code : ${loginResponse.code()}")
                }

            }else{
                registerListener.onFailure("Error code : ${getTokenResponse.code()}")
            }
        }
    }
}

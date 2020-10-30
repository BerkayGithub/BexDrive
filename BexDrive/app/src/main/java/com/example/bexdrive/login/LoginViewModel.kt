package com.example.bexdrive.login

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.listener.RegisterListener
import com.example.bexdrive.network.response.CenterTokenResponse
import com.example.bexdrive.network.response.LoginResponse
import com.example.bexdrive.repository.CenterRepository
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.Coroutine
import com.example.bexdrive.util.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

class LoginViewModel @ViewModelInject constructor(
    private val repository : ProxyRepository
) : ViewModel() {

    lateinit var registerListener: RegisterListener
    var username : String? = null
    var password : String? = null
    var basicProxyToken : String? = null

    private val _successLiveEvent: MutableLiveData<String> = MutableLiveData("")
    private val _navigateMainPageLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun successLiveData(): LiveData<String> = _successLiveEvent
    fun navigateMainPageLiveData(): LiveData<Boolean> = _navigateMainPageLiveEvent

    @RequiresApi(Build.VERSION_CODES.O)
    fun onLoginButtonClicked(){
        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            registerListener.onFailure("Invalid username or password!")
            return
        }

        viewModelScope.launch {
            var loginResponse : Response<LoginResponse>? = null
            val tokenResponse: Response<CenterTokenResponse> = repository.getToken("Basic $basicProxyToken")
            if (tokenResponse.isSuccessful){
                val accessToken = tokenResponse.body()?.access_token
                loginResponse = repository.userLogin("Bearer $accessToken", username!!, password!!)
            }else{
                registerListener.onFailure("Error code : ${tokenResponse.code()} while getting second token")
            }

            loginResponse?.let {
                if (it.isSuccessful){
                    it.body()?.apply {
                        if(Result == true){
                            DaggerClass.employeeID = EmployeeID
                            DaggerClass.employeeName = EmployeeName
                            _successLiveEvent.postValue("$EmployeeName giriş yaptı!")
                            _navigateMainPageLiveEvent.postValue(true)
                        }
                    }
                }else{
                    _successLiveEvent.postValue("Error code : ${loginResponse.code()} during login service!")
                }
            }
        }
    }
}

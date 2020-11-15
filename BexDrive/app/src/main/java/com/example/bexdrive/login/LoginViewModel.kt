package com.example.bexdrive.login

import android.content.SharedPreferences
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.network.response.CenterTokenResponse
import com.example.bexdrive.network.response.LoginResponse
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.SingleLiveEvent
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel @ViewModelInject constructor(
    private val repository : ProxyRepository
) : ViewModel() {

    var username : String = ""
    var password : String = ""
    var basicProxyToken : String = ""

    private val _successLiveEvent: MutableLiveData<String> = MutableLiveData()
    private val _navigateMainPageLiveEvent: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun successLiveData(): LiveData<String> = _successLiveEvent
    fun navigateMainPageLiveData(): LiveData<Boolean> = _navigateMainPageLiveEvent

    lateinit var sharedPreferences: SharedPreferences

    fun onLoginButtonClicked(){
        if(username.isEmpty() || password.isEmpty()){
            _successLiveEvent.postValue("Invalid username or password!")
            return
        }

        viewModelScope.launch {
            var loginResponse : Response<LoginResponse>? = null
            val tokenResponse: Response<CenterTokenResponse> = repository.getToken("Basic $basicProxyToken")
            if (tokenResponse.isSuccessful){
                val accessToken = tokenResponse.body()?.access_token

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("BearerToken", accessToken)
                editor.apply()

                loginResponse = repository.userLogin("Bearer $accessToken", username, password)
            }else{
                _successLiveEvent.postValue("Error code : ${tokenResponse.code()} while getting second token")
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

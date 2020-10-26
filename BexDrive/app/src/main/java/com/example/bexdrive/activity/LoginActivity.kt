package com.example.bexdrive.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bexdrive.R
import com.example.bexdrive.login.LoginFragment
import com.example.bexdrive.register.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //supportFragmentManager.beginTransaction().replace(R.id.login_frame, RegisterFragment()).commit()
    }
}

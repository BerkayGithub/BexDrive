package com.example.bexdrive.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.replace
import com.example.bexdrive.R
import com.example.bexdrive.currentService.serviceaddressdetail.ServiceAddressDetailPage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_service_address_detail.*

@AndroidEntryPoint
class ServiceAddressDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_address_detail)

        actionBar?.title = "Adres Detayı"
        supportActionBar?.setTitle("Adres Detayı")
        supportFragmentManager.beginTransaction().replace(R.id.fragment_address_detail_activity, ServiceAddressDetailPage()).commit()

    }
}

package com.example.bexdrive.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bexdrive.R
import com.example.bexdrive.entity.Address
import com.example.bexdrive.entity.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_show_address_on_map.*

class ShowAddressOnMap : AppCompatActivity() {

    var latitude : Double = 0.0
    var longitude : Double = 0.0
    lateinit var supportMapFragment : SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_address_on_map)
        getAddressFromBundle()

        supportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(
            OnMapReadyCallback {
                val latLng = LatLng(latitude, longitude)

                val options = MarkerOptions().position(latLng).title("Service Address")
                it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10F))
                it.addMarker(options)
            }
        )
    }

    fun getAddressFromBundle(){
        val extras = intent.extras
        if(extras != null){
            val address = intent.extras!!.get("Address") as Address
            latitude = address.Latitude
            longitude = address.Longitude
        }else{
            finish()
        }
    }
}

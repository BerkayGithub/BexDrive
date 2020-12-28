package com.example.bexdrive.currentService.map

import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bexdrive.entity.Address
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.DirectionParser
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MapViewModel @ViewModelInject constructor(
    val repository: ProxyRepository
): ViewModel() {

}

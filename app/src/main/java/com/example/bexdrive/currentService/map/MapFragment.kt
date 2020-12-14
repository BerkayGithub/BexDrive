package com.example.bexdrive.currentService.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.R
import com.example.bexdrive.databinding.MapFragmentBinding
import com.example.bexdrive.entity.Address
import com.example.bexdrive.util.DirectionParser
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: MapViewModel by viewModels()

    private lateinit var googleMap : GoogleMap
    private lateinit var myLocation : Location
    private var locationPermission : Boolean = false
    private lateinit var addressList : List<Address>
    private var markers : ArrayList<MarkerOptions> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : MapFragmentBinding = MapFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        //binding.mapsView1.onCreate(null)
        //binding.mapsView1.onResume()
        //binding.mapsView1.getMapAsync(this)

        if (DaggerClass.service != null) {
            addressList = DaggerClass.service!![0].Addresses
        }

        //val place1 = MarkerOptions().position(LatLng(40.986371, 29.131858)).title("Location1")
        //val place2 = MarkerOptions().position(LatLng(40.985273, 29.132687)).title("Location2")
        //markers.add(place1)
        //markers.add(place2)

        return binding.root
    }

    private fun getUrl(markers: ArrayList<MarkerOptions>, s: String): String {

        val origin = "origin=" + markers[0].position.latitude + "," + markers[0].position.longitude

        val dest = "destination=" + markers[1].position.latitude + "," + markers[1].position.longitude

        val sensor = "sensor=false"

        val mode = "mode=$s"

        val parameters = "$origin&$dest&$sensor&$mode"

        val output = "json"

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.map_key)
    }

    private fun checkPermission(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationPermission = true
        getMyLocation()
    }

    private fun getMyLocation(){
        if (locationPermission) {
            googleMap.isMyLocationEnabled = true
            googleMap.setOnMyLocationChangeListener {
                myLocation = it
                val latLng = LatLng(it.latitude, it.longitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    latLng, 16f
                )
                googleMap.animateCamera(cameraUpdate)
            }
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        if (p0 != null) {
            googleMap = p0
            checkPermission()
            googleMap.addMarker(markers[0])
            googleMap.addMarker(markers[1])
            //TaskDirectionRequest(googleMap, requireContext()).execute(getUrl(markers, "driving")) this function currently gives an error.
        }
    }

    class TaskDirectionRequest constructor(private val googleMap: GoogleMap, val context: Context) : AsyncTask<String, Void, String>() {

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val parseResult = TaskParseDirection(googleMap, context)
            parseResult.execute(result)
        }

        override fun doInBackground(vararg p0: String?): String {
            var responseString = ""
            try {
                responseString = requestDirection(p0[0]!!)
            } catch (e : java.lang.Exception) {
                e.printStackTrace()
            }
            return responseString
        }

        private fun requestDirection(requestedUrl : String) : String{
            var responseString = ""
            var inputStream : InputStream? = null
            var httpURLConnection : HttpURLConnection? = null
            try {
                val url = URL(requestedUrl)
                httpURLConnection = url.openConnection() as HttpURLConnection?
                httpURLConnection!!.connect()

                inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream!!)
                val bufferedReader = BufferedReader(inputStreamReader)

                val stringBuffer = StringBuffer()
                var line = ""
                while (bufferedReader.readLine() != null) {
                    line = bufferedReader.readLine()
                    stringBuffer.append(line)
                }
                responseString = stringBuffer.toString()
                bufferedReader.close()
                inputStreamReader.close()

            }catch (e : Exception){
                e.printStackTrace()
            }finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            httpURLConnection?.disconnect()
            return responseString
        }
    }

    class TaskParseDirection constructor(private val googleMap: GoogleMap, val context: Context): AsyncTask<String, Void, List<List<HashMap<String, String>>>>(){

        override fun doInBackground(vararg p0: String?): List<List<HashMap<String, String>>>? {
            var routes : List<List<HashMap<String, String>>>? = null
            var jsonObject : JSONObject? = null

            try {
                jsonObject = JSONObject(p0[0]!!)
                val parser = DirectionParser()
                routes = parser.parse(jsonObject)
            } catch (e : JSONException) {
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            super.onPostExecute(result)
            var points: ArrayList<LatLng>? = null
            var polylineOptions: PolylineOptions? = null
            for (path in result!!)
            {
                points = ArrayList()
                polylineOptions = PolylineOptions()
                for (point in path)
                {
                    val lat = java.lang.Double.parseDouble(point["lat"]!!)
                    val lon = java.lang.Double.parseDouble(point["lng"]!!)
                    points.add(LatLng(lat, lon))
                }
                polylineOptions.addAll(points)
                polylineOptions.width(15f)
                polylineOptions.color(Color.BLUE)
                polylineOptions.geodesic(true)
            }
            if (polylineOptions != null)
            {
                googleMap.addPolyline(polylineOptions)
            }
            else
            {
                Toast.makeText(context, "Direction not found", Toast.LENGTH_LONG).show()
            }
        }
    }
}

package com.example.bexdrive.currentService.tobevisitedAddress

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.view.get
import androidx.core.view.size
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bexdrive.DaggerClass

import com.example.bexdrive.R
import com.example.bexdrive.databinding.ToBeVisitedAddressFragmentBinding
import com.example.bexdrive.entity.Location
import com.example.bexdrive.entity.Service
import com.example.bexdrive.repository.ProxyRepository
import com.example.bexdrive.util.ServiceAdapter
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.detail_fragment.*
import kotlinx.android.synthetic.main.register_fragment.*
import kotlinx.android.synthetic.main.service_address_detail_page_fragment.view.*
import kotlinx.android.synthetic.main.to_be_visited_address_fragment.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ToBeVisitedAddress : Fragment() {

    private val viewModel: ToBeVisitedAddressViewModel by viewModels()
    private lateinit var list: List<Service>
    private var addressList: ArrayList<com.example.bexdrive.entity.Address> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ToBeVisitedAddressFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences

        binding.btnCheckCurrentService.setOnClickListener {
            binding.ToBeVisitedRecyclerView.visibility = VISIBLE
            binding.btnCheckCurrentService.visibility = GONE
        }

        if(DaggerClass.location == null){
            if(ActivityCompat.checkSelfPermission(requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                getLocation()
            }else{
                ActivityCompat.requestPermissions(requireActivity(),
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 44)
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.serviceStateMessageLiveData().observe(viewLifecycleOwner){
            val mySnackBar = Snackbar.make(layout_to_be_visited, it, Snackbar.LENGTH_LONG)
            mySnackBar.setAction("OK") {
                mySnackBar.dismiss()
            }
            mySnackBar.show()
        }

        viewModel.serviceListLiveData().observe(viewLifecycleOwner){
            list = it
            DaggerClass.service = list

            if(DaggerClass.service!![0].Status == 2){
                relLayout_main.visibility = GONE
                relLayout2_main.visibility = VISIBLE
                ToBeVisited_recycler_view.visibility = VISIBLE
                btn_checkCurrentService.visibility = GONE
            }

            addressList.clear()
            for (x in list[0].Addresses){
                if (!x.IsVisited){
                    addressList.add(x)
                }
            }
            val viewManager = LinearLayoutManager(requireContext())
            val serviceAdapter = ServiceAdapter(addressList, requireActivity())
            ToBeVisited_recycler_view.apply {
                setHasFixedSize(true)
                layoutManager = viewManager
                adapter = serviceAdapter
            }

            ToBeVisited_progress.visibility = GONE
            lin_Layout_main.visibility = VISIBLE
        }

        viewModel.getServiceData()
    }

    fun getLocation(){
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient!!.lastLocation.addOnCompleteListener { task ->
            val location = task.result
            if(location != null){
                val geoCoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    val address: List<Address> = geoCoder.getFromLocation(location.latitude,location.longitude,1)
                    val currentLocation = Location(address[0].latitude, address[0].longitude)
                    DaggerClass.location = currentLocation
                }catch (e : IOException){
                    e.printStackTrace()
                }
            }else{

            }
        }
    }
}

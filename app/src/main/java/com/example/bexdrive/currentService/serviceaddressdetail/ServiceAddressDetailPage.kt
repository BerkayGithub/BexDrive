package com.example.bexdrive.currentService.serviceaddressdetail

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.R
import com.example.bexdrive.activity.MainActivity
import com.example.bexdrive.databinding.ServiceAddressDetailPageFragmentBinding
import com.example.bexdrive.entity.Address
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.service_address_detail_page_fragment.*
import java.text.DateFormat

@AndroidEntryPoint
class ServiceAddressDetailPage : Fragment() {

    private val viewModel: ServiceAddressDetailPageViewModel by viewModels()
    lateinit var address: Address
    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = ServiceAddressDetailPageFragmentBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        val sharedPreferences = requireActivity().getSharedPreferences("SHARED_PREF", Context.MODE_PRIVATE)
        viewModel.sharedPreferences = sharedPreferences

        val args = requireActivity().intent.extras

        if (args != null) {
            address = args.get("Address") as Address
        }
        viewModel.addressName = address.Address
        viewModel.fullAddress = address.PointName
        val deliveryDateTime : String
        if(address.IsVisited){
            binding.btnDeliverAddressDetail.visibility = GONE
            deliveryDateTime = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(address.DateDelivered)
            viewModel.deliveryDate = "Teslim tarihi: $deliveryDateTime"
        }else{
            deliveryDateTime = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT).format(address.EstimatedDateDelivered)
            viewModel.deliveryDate = "Tahmini teslim tarihi $deliveryDateTime"
        }

        viewModel.ServiceID = DaggerClass.service!![0].ServiceID
        viewModel.AddressID = address.AddressID
        viewModel.CurrentLocation = DaggerClass.location!!

        if (DaggerClass.service != null && DaggerClass.service!![0].Status == 0){
            binding.btnDeliverAddressDetail.visibility = GONE
        }

        progressDialog = ProgressDialog(requireContext(), R.style.ProgressDialogStyle)
        progressDialog.setMessage("Lütfen Bekeyiniz...")

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.successMessageLiveData().observe(viewLifecycleOwner){
            val builder = AlertDialog.Builder(requireActivity(), R.style.CustomDialogTheme)
            builder.setMessage(it).setPositiveButton("OK") { _: DialogInterface, _: Int ->
                val intent = Intent(requireActivity(), MainActivity::class.java)
                startActivity(intent)
            }
            builder.show()
        }

        viewModel.errorMessageLiveData().observe(viewLifecycleOwner){
            val snackBar = Snackbar.make(address_detail_layout, it, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction("OK") { snackBar.dismiss() }
            snackBar.show()
        }

        viewModel.packageListLiveData().observe(viewLifecycleOwner){
            packages_progressB.visibility = GONE
            val x = it.size - 1
            for (i in 0..x){
                val textView = TextView(requireContext())
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                layoutParams.setMargins(0,0,0,10)
                textView.text = "${it[i].Name} nolu paket"
                textView.setPadding(5,5,5,5)
                textView.layoutParams = layoutParams
                textView.setBackgroundResource(R.drawable.package_address_bg)

                packages_layout.addView(textView)
            }
        }

        viewModel.deliverProgressLiveData().observe(viewLifecycleOwner){
            if(it == 1){
                progressDialog.show()
            }
            if (it == 0){
                progressDialog.dismiss()
            }
        }

        val serviceID = DaggerClass.service!![0].ServiceID
        viewModel.getPackagesWhichDeliverToAddress(serviceID, address.AddressID)
    }

}

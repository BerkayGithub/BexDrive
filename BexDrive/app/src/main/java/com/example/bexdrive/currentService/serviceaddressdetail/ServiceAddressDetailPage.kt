package com.example.bexdrive.currentService.serviceaddressdetail

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.R
import com.example.bexdrive.databinding.ServiceAddressDetailPageFragmentBinding
import com.example.bexdrive.entity.Address
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.service_address_detail_page_fragment.*

@AndroidEntryPoint
class ServiceAddressDetailPage : Fragment() {

    private val viewModel: ServiceAddressDetailPageViewModel by viewModels()
    lateinit var address: Address

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
        viewModel.deliveryDate = "Tahmini teslim tarihi ${address.EstimatedDateDelivered.toLocaleString()}"

        viewModel.ServiceID = DaggerClass.service!![0].ServiceID
        viewModel.AddressID = address.AddressID
        viewModel.CurrentLocation = DaggerClass.location!!

        if (DaggerClass.service != null && DaggerClass.service!![0].Status == 0){
            binding.btnDeliverAddressDetail.visibility = GONE
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.successMessageLiveData().observe(viewLifecycleOwner){
            val builder = AlertDialog.Builder(requireActivity(), R.style.CustomDialogTheme)
            builder.setTitle("Success").setMessage(it).setPositiveButton("OK") { _: DialogInterface, _: Int ->
                requireActivity().finish()
            }
            builder.show()
        }

        viewModel.errorMessageLiveData().observe(viewLifecycleOwner){
            val snackbar = Snackbar.make(address_detail_layout, it, Snackbar.LENGTH_INDEFINITE)
            snackbar.setAction("OK") { snackbar.dismiss() }
            snackbar.show()
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

        val serviceID = DaggerClass.service!![0].ServiceID
        viewModel.getPackagesWhichDeliverToAddress(serviceID, address.AddressID)
    }

}

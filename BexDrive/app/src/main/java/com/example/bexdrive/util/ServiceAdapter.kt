package com.example.bexdrive.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.bexdrive.R
import com.example.bexdrive.currentService.tobevisitedAddress.ToBeVisitedAddress
import com.example.bexdrive.currentService.tobevisitedAddress.ToBeVisitedAddressDirections
import com.example.bexdrive.databinding.ServiceRowBinding
import com.example.bexdrive.entity.Address
import com.example.bexdrive.entity.Service
import kotlinx.android.synthetic.main.service_row.view.*

class ServiceAdapter (
    private val addressList : List<Address>
) : RecyclerView.Adapter<ServiceAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.service_row,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.serviceRowBinding.address = addressList[position]

        holder.itemView.btn_deliver.setOnClickListener {
            val action = ToBeVisitedAddressDirections.actionNavToBeVisitedAddressToServiceAddressDetailPage(addressList[position])
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.btn_show_packages.setOnClickListener {
            val action = ToBeVisitedAddressDirections.actionNavToBeVisitedAddressToServiceAddressDetailPage(addressList[position])
            holder.itemView.findNavController().navigate(action)
        }
    }

    class ViewHolder(
        val serviceRowBinding: ServiceRowBinding
    ) : RecyclerView.ViewHolder(serviceRowBinding.root)

}
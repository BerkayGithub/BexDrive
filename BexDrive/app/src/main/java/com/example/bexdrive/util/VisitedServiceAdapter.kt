package com.example.bexdrive.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bexdrive.R
import com.example.bexdrive.databinding.ServiceRowVisitedBinding
import com.example.bexdrive.entity.Address

class VisitedServiceAdapter(
    private val addressList: List<Address>
) : RecyclerView.Adapter<VisitedServiceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.service_row_visited,
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        return addressList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.serviceRowVisitedBinding.address = addressList[position]

    }

    class ViewHolder(
        val serviceRowVisitedBinding: ServiceRowVisitedBinding
    ) : RecyclerView.ViewHolder(serviceRowVisitedBinding.root)
}
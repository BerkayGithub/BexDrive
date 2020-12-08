package com.example.bexdrive.util

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.R
import com.example.bexdrive.activity.ServiceAddressDetailActivity
import com.example.bexdrive.databinding.ServiceRowBinding
import com.example.bexdrive.entity.Address
import com.example.bexdrive.repository.ProxyRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.service_row.view.*

class ServiceAdapter (
    private val addressList : List<Address>,
    private val activity : Activity
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
            val intent = Intent(activity, ServiceAddressDetailActivity::class.java).apply {
                putExtra("Address", addressList[position])
            }
            activity.startActivity(intent)
        }

        holder.itemView.btn_show_packages.setOnClickListener {
            val intent = Intent(activity, ServiceAddressDetailActivity::class.java).apply {
                putExtra("Address", addressList[position])
            }
            activity.startActivity(intent)
        }

        holder.itemView.btn_show_In_map.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val latitude = addressList[position].Latitude
            val longitude = addressList[position].Longitude
            val label = addressList[position].PointName
            intent.data = Uri.parse("geo:0,0?z=17&q=$latitude,$longitude($label)")
            if(intent.resolveActivity(activity.packageManager) != null){
                activity.startActivity(intent)
            }
        }

        if(DaggerClass.service != null && DaggerClass.service!![0].Status == 0){
            holder.itemView.btn_deliver.isEnabled = false
            holder.itemView.btn_show_In_map.isEnabled = false
        }
    }

    class ViewHolder(
        val serviceRowBinding: ServiceRowBinding
    ) : RecyclerView.ViewHolder(serviceRowBinding.root)

}
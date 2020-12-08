package com.example.bexdrive.util

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.bexdrive.DaggerClass
import com.example.bexdrive.R
import com.example.bexdrive.activity.ChangeLocationActivity
import com.example.bexdrive.databinding.LocationRowBinding
import com.example.bexdrive.entity.Location
import com.example.bexdrive.entity.Point
import com.example.bexdrive.repository.ProxyRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.location_row.view.*
import kotlinx.android.synthetic.main.service_address_detail_page_fragment.*

class PointAdapter(
    private val pointList : List<Point>,
    private val sharedPreferences: SharedPreferences,
    private val activity: Activity,
    private val viewS: View
) : RecyclerView.Adapter<PointAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder (
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.location_row,
                parent,
                false
            )
    )

    override fun getItemCount(): Int {
        return pointList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.locationRowBinding.point = pointList[position]


        holder.itemView.btn_update_address.setOnClickListener {
            updatePointLocation(pointList[position].Name)
        }
    }

    private fun updatePointLocation(name: String){
        val progressDialog = ProgressDialog(activity, R.style.ProgressDialogStyle)
        progressDialog.setMessage("Lütfen Bekleyiniz...")
        progressDialog.show()

        val repository = ProxyRepository()

        val bearerToken = sharedPreferences.getString("BearerToken", "")
        if (bearerToken.isNullOrEmpty()){
            progressDialog.dismiss()
            return
        }
        Coroutine.main {
            val updatePointResponse = repository.updatePointLocation("Bearer $bearerToken", name, DaggerClass.location!!)
            if (updatePointResponse.isSuccessful){
                if (updatePointResponse.body()!!.Result){
                    progressDialog.dismiss()
                    val builder = AlertDialog.Builder(activity, R.style.CustomDialogTheme)
                    builder.setMessage("Konum Güncelleme Tamamlandı!").setPositiveButton("OK"){ _: DialogInterface, _:Int ->
                        val intent = Intent(activity, ChangeLocationActivity::class.java)
                        activity.startActivity(intent)
                    }
                }else{
                    progressDialog.dismiss()
                    val snackBar = Snackbar.make(viewS, "Hata : ${updatePointResponse.body()!!.Message}", Snackbar.LENGTH_INDEFINITE)
                    snackBar.setAction("OK") { snackBar.dismiss() }
                    snackBar.show()
                }
            } else {
                progressDialog.dismiss()
                val snackBar = Snackbar.make(viewS, "Hata : ${updatePointResponse.body()!!.Message}", Snackbar.LENGTH_INDEFINITE)
                snackBar.setAction("OK") { snackBar.dismiss() }
                snackBar.show()
            }
        }

    }

    class ViewHolder (
        val locationRowBinding: LocationRowBinding
    ) : RecyclerView.ViewHolder(locationRowBinding.root)
}
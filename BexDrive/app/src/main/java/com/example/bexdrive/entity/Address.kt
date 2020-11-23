package com.example.bexdrive.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Address (
    val AddressID : String,
    val Sort : Int,
    val IsVisited : Boolean,
    val Address : String,
    val PointName : String,
    val Latitude : Double,
    val Longitude : Double,
    val EstimatedDateDelivered : Date,
    val DateDelivered : Date
) : Parcelable
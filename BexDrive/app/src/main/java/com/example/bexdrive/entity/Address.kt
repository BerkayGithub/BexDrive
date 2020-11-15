package com.example.bexdrive.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Address (
    val AddressID : String,
    val Sort : Int,
    val isVisited : Boolean,
    val Address : String,
    val PointName : String,
    val Latitude : Int,
    val Longitude : Int,
    val EstimatedDateDelivered : Date,
    val DateDelivered : Date
) : Parcelable
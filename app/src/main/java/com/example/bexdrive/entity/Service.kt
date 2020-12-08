package com.example.bexdrive.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Service (
    val ServiceID : String,
    val Status : Int,
    val TimeStarts : Date,
    val TimeEnds : Date,
    val EstimatedTimeStarts : Date,
    val EstimatedTimeEnds: Date,
    val PointCount : Int,
    val TotalPackageCount : Int,
    val Addresses : List<Address>,
    val StartLocation : Location,
    val EndLocation : Location
) : Parcelable
package com.example.bexdrive.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Location (
    val Latitude : Double,
    val Longitude : Double
) : Parcelable
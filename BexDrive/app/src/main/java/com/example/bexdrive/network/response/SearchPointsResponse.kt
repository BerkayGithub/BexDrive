package com.example.bexdrive.network.response

import com.example.bexdrive.entity.Point

data class SearchPointsResponse (
    val Points: List<Point>,
    val Result: Boolean,
    val Message: String,
    val Code: String
)
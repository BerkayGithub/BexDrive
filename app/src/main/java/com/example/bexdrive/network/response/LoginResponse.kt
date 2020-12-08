package com.example.bexdrive.network.response

data class LoginResponse (
    val EmployeeID : Int?,
    val EmployeeName : String?,
    val Result : Boolean?,
    val Code : String?,
    val Message : String?
)
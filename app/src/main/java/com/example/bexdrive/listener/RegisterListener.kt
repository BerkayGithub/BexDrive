package com.example.bexdrive.listener

interface RegisterListener {

    fun onStarted()
    fun onSuccess(message: String)
    fun onFailure(message : String)

}
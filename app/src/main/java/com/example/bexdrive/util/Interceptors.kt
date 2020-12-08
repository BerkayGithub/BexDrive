package com.example.bexdrive.util

import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*

class Interceptors : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        request = request.newBuilder()
            .addHeader("x-device-type", Build.DEVICE)
            .addHeader("Accept-Language", Locale.getDefault().language)
            .build()

        val response = chain.proceed(request)
        return response
    }

}
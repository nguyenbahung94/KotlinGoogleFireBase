package com.example.asus.myapplication

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by asus on 9/7/17.
 */
object Utils {
    fun isNetWorkConnnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val acNetworkInfo = cm.activeNetworkInfo
        return acNetworkInfo != null && acNetworkInfo.isConnectedOrConnecting
    }
}
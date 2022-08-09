package com.ebenezer.gana.fcsibbul.data.network

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject

class NetworkStatusChecker @Inject constructor(private val connectivityManager: ConnectivityManager?) {

    @RequiresApi(Build.VERSION_CODES.M)
    inline fun performIfConnectedToInternetOrNot(action:() ->Unit, onNoInternet:()-> Unit){
        if(hasInternetConnection()){
            action()
        }else{
            onNoInternet()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun hasInternetConnection():Boolean {
        val network = connectivityManager?.activeNetwork?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network)?: return false

        //check if there's an active network
        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN)
    }
}
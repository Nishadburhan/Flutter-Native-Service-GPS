package com.beegains.flutter_services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat


/**
 * Created by shiva on 8/4/17.
 */
class GPSTracker(context: Context?) : LocationListener {
    var context: Context? = null

    // e.printStackTrace();
    val location: Location?
        get() {
            if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.e("fist", "error")
                return null
            }
            try {
                val lm = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGPSEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (isGPSEnabled) {
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000, 10f, this)
                    return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } else {
                    Log.e("sec", "errpr")
                }
            } catch (e: Exception) {
                // e.printStackTrace();
            }
            return null
        }

    override fun onLocationChanged(location: Location) {}
    override fun onStatusChanged(s: String, i: Int, bundle: Bundle) {}
    override fun onProviderEnabled(s: String) {}
    override fun onProviderDisabled(s: String) {}

    init {
        var context = context
        context = context
    }
}


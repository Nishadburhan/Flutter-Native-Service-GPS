package com.beegains.flutter_services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit


class LocationWorker(context: Context, workerParams: WorkerParameters?) : Worker(context, workerParams!!) {
    var ctx = context;
    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

    // GPSTracker class
    var gps: GPSTracker? = null
    var gps1: Gps? = null
    val REQUEST_LOCATION = 1
    var locationManager: LocationManager? = null
    var gps_enabled = false
    var network_enabled = false


    override fun doWork(): Result {
        return try {
            Log.e("name",MainActivity.name);
            locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                // Do something for lollipop and above versions
                getLocationforaboveaAndroid5();
            } else{
                // do something for phones running an SDK before lollipop
                getLocationbelowAndroid5();
            }

            StartNewRequest()
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            StartNewRequest()
            Log.d("WorkerTest", "Error Occured")
            Result.failure()
        }
    }

    private fun StartNewRequest() {
        val track_market = OneTimeWorkRequest.Builder(LocationWorker::class.java).setInitialDelay(3, TimeUnit.SECONDS).addTag("Stock_Market").build()
        WorkManager.getInstance().enqueue(track_market)
    }


    fun getLocationforaboveaAndroid5() {
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((ctx as Activity), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION)
        } else {
            val location: Location? = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null) {
                val latti: Double = location.latitude
                val longi: Double = location.longitude
                Log.e("Lattiude", latti.toString())
                var model = CoordinatesModel(latti.toString(), longi.toString())
                // Write a message to the database

                // Write a message to the database
                val rnds = (0..10).random()
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("Location $rnds")

                myRef.setValue(model)

            } else {
            }
        }
    }
    fun getLocationbelowAndroid5() {
        // create class object
        gps1 = Gps(ctx)

        // check if GPS enabled
        if (gps1!!.canGetLocation()) {
            val latitude = gps1!!.getLatitude()
            val longitude = gps1!!.getLongitude()
            println("Lattitude$latitude")
            var model = CoordinatesModel(latitude.toString(), longitude.toString())
            // Write a message to the database

            // Write a message to the database
            val rnds = (0..10).random()
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference("Location $rnds")

            myRef.setValue(model)


        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps1!!.showSettingsAlert()
        }
    }


}
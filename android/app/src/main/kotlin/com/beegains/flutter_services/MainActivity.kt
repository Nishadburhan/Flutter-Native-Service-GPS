package com.beegains.flutter_services

import android.util.Log
import androidx.annotation.NonNull
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import java.util.concurrent.TimeUnit


class MainActivity: FlutterActivity() {
     val CHANNEL = "flutter.native/helper"
    companion object{
        var name = ""
    }
    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if (call.method == "startwork") {
                Log.e("Native", "Hi from android")
                print("Hi from android")
                startWork()



            } else {
                stopWork()
               // result.notImplemented()
            }
        }
    }

    fun startWork(){
        name = "ramzan"
        val locationWorker = OneTimeWorkRequest.Builder(LocationWorker::class.java).setInitialDelay(3, TimeUnit.SECONDS).addTag("LocationFetching").build()
        WorkManager.getInstance().enqueue(locationWorker)

    }
    fun stopWork(){
        WorkManager.getInstance().cancelAllWork();
    }
}

package com.shakilpatel.bytebuddy.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    private var context: Context? = null

    //    val ONESIGNAL_APP_ID = "7e70f898-3b02-4461-b6d3-c6388da1c10b"
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        val db = FirebaseFirestore.getInstance()

        val settings = FirebaseFirestoreSettings.Builder()
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings


        val scoresRef = Firebase.database.reference
        scoresRef.keepSynced(true)

        //set below condition that the block can execute only if the release version of app was

//        if (!BuildConfig.DEBUG) {
            setupActivityListener()
//        }
    }

    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                /*TODO MAKE THIS FLAGS ENABLE TO SECURE APP FROM TAKING SCREENSHOT AND SCREENCAST*/
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }

            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}
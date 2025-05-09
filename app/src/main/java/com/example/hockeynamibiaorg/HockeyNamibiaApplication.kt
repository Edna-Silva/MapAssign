package com.example.hockeynamibiaorg


import android.app.Application
import com.example.hockeynamibiaorg.data.repositories.SessionManager
import com.google.firebase.FirebaseApp

class HockeyNamibiaApplication : Application() {
    // Lazy initialization of SessionManager
    val sessionManager: SessionManager by lazy {
        SessionManager(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Any other initialization code
    }
}
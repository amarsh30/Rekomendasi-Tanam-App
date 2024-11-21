package com.amr.rekomendasitanam.view

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class Firebase : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}
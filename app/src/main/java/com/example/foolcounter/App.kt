package com.example.foolcounter

import android.app.Application
import com.example.foolcounter.data.database.PLayersDB

class App : Application() {
    val db: PLayersDB by lazy {
        PLayersDB.getDB(this)
    }
}

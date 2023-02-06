package com.example.foolcounter.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [PlayerEntity::class])
abstract class PLayersDB : RoomDatabase() {
    abstract fun playersDao(): PlayersDAO

    companion object {
        @Volatile
        private var INSTANCE: PLayersDB? = null

        fun getDB(context: Context): PLayersDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context = context,
                    klass = PLayersDB::class.java,
                    name = "players_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}

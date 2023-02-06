package com.example.foolcounter.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayersDAO {
    @Query("SELECT * FROM PlayerEntity")
    fun getAllPlayers(): Flow<List<PlayerEntity>>

    @Insert
    suspend fun addPlayer(playerEntity: PlayerEntity)

    @Update
    suspend fun update(playerEntity: PlayerEntity)

    @Query("DELETE FROM PlayerEntity")
    suspend fun deleteAll()
}

package com.example.foolcounter.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foolcounter.model.Player

@Entity
data class PlayerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val count: Int = 0
) {
    fun toPlayer(): Player = Player(
        id = id,
        name = name,
        count = count
    )

    companion object {
        fun toPlayerEntity(player: Player): PlayerEntity = PlayerEntity(
            id = player.id,
            name = player.name,
            count = player.count
        )
    }
}

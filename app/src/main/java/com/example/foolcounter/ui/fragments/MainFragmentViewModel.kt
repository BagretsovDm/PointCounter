package com.example.foolcounter.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foolcounter.data.database.PlayerEntity
import com.example.foolcounter.data.database.PlayersDAO
import com.example.foolcounter.model.Player
import kotlinx.coroutines.flow.Flow

class MainFragmentViewModel(private val playersDAO: PlayersDAO) : ViewModel() {

    private val _visible = MutableLiveData(true)
    val visible: LiveData<Boolean> = _visible

    fun setVisible(flag: Boolean) {
        _visible.value = flag
    }

    fun getAllPlayers(): Flow<List<PlayerEntity>> =
        playersDAO.getAllPlayers()

    suspend fun addPlayer(player: Player) {
        playersDAO.addPlayer(PlayerEntity.toPlayerEntity(player))
    }

    suspend fun update(player: Player) {
        playersDAO.update(PlayerEntity.toPlayerEntity(player))
    }

    suspend fun deleteAll() = playersDAO.deleteAll()
}

class MainFragmentViewModelFactory(
    private val playersDAO: PlayersDAO
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java))
            return MainFragmentViewModel(playersDAO) as T
        else
            throw IllegalArgumentException()
    }
}

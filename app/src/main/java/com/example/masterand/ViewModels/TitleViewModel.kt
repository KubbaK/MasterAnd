package com.example.masterand.ViewModels

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.masterand.Entities.Player
import com.example.masterand.Repos.PlayersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TitleViewModel @Inject constructor(private val playersRepository: PlayersRepository) : ViewModel()
{
    val playerId = mutableLongStateOf(0L)
    val name = mutableStateOf("")
    val email = mutableStateOf("")

    suspend fun savePlayer() {
        var player = playersRepository.getPlayerByEmail(email.value)
        if (player == null) {
            player = Player(0L, name.value, email.value)
            playerId.longValue = playersRepository.insertPlayer(player)
        } else {
            player = Player(player.id, name.value, email.value)
            playersRepository.updatePlayer(player)
            playerId.longValue = player.id
        }
        println("player = $player")
        println("playerId.longValue = ${playerId.longValue}")
    }
}
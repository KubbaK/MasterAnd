package com.example.masterand.Repos

import com.example.masterand.Entities.Player
import kotlinx.coroutines.flow.Flow

interface PlayersRepository {
    fun getAllPlayersStream(): Flow<List<Player>>
    fun getPlayerStream(id: Int): Flow<Player?>
    suspend fun getPlayerByEmail(email: String): Player?
    suspend fun insertPlayer(player: Player) : Long
    suspend fun updatePlayer(player: Player)
}
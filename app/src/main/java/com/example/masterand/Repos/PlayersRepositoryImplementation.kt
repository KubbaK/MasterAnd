package com.example.masterand.Repos

import com.example.masterand.Daos.PlayerDao
import com.example.masterand.Entities.Player
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlayersRepositoryImplementation @Inject constructor(private val playerDao: PlayerDao) : PlayersRepository {
    override fun getAllPlayersStream(): Flow<List<Player>> = playerDao.getAllPlayersStream()

    override fun getPlayerStream(id: Int): Flow<Player?> = playerDao.getPlayerStream(id)

    override suspend fun getPlayerByEmail(email: String): Player =
        playerDao.getPlayerByEmail(email)

    override suspend fun insertPlayer(player: Player): Long = playerDao.insert(player)
    override suspend fun updatePlayer(player: Player) = playerDao.update(player)
}
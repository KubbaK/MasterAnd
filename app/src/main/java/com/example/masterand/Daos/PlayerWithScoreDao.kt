package com.example.masterand.Daos

import androidx.room.Dao
import androidx.room.Query
import com.example.masterand.Entities.PlayerWithScore
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerWithScoreDao {
    @Query(
        "SELECT players.id AS playerId, scores.id AS scoreId " +
                "FROM players, scores WHERE players.id = scores.playerId"
    )
    fun loadPlayerScores(): Flow<List<PlayerWithScore>>

    @Query("SELECT players.id AS playerId, scores.id AS scoreId " +
            "FROM players, scores WHERE players.id = scores.playerId AND players.Id = :playerId")
    fun getPlayerScoresByPlayerId(playerId: Long): Flow<List<PlayerWithScore>>
}
package com.example.masterand.Repos

import com.example.masterand.Entities.Score
import kotlinx.coroutines.flow.Flow

interface ScoresRepository {
    fun getScoreStream(id: Long): Flow<Score?>
    fun getAllScoresStream(): Flow<List<Score>>
    fun getScoresByPlayerId(playerId: Long): Flow<List<Score>>
    suspend fun insertScore(score: Score) : Long
}
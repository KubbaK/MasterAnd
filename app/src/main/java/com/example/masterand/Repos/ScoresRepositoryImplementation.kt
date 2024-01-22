package com.example.masterand.Repos

import com.example.masterand.Daos.PlayerWithScoreDao
import com.example.masterand.Daos.ScoreDao
import com.example.masterand.Entities.Score
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import javax.inject.Inject

class ScoresRepositoryImplementation @Inject constructor(private val scoreDao: ScoreDao, private val playerScoreDao: PlayerWithScoreDao) : ScoresRepository {
    override fun getScoreStream(id: Long): Flow<Score?> = scoreDao.getScoreStream(id)
    override fun getAllScoresStream(): Flow<List<Score>> = scoreDao.getAllScoresStream()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getScoresByPlayerId(playerId: Long): Flow<List<Score>> {
        return playerScoreDao.getPlayerScoresByPlayerId(playerId).flatMapMerge { playerScores ->
            val playerScoresIds = playerScores.map { it.scoreId }
            scoreDao.getScoresStream(playerScoresIds)
        }
    }

    override suspend fun insertScore(score: Score): Long = scoreDao.insert(score)
}
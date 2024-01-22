package com.example.masterand.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.masterand.Entities.Score
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(score: Score): Long

    @Query("SELECT * from scores WHERE id = :id")
    fun getScoreStream(id: Long): Flow<Score>

    @Query("SELECT * from scores WHERE id = (:idList)")
    fun getScoresStream(idList: List<Long>): Flow<List<Score>>

    @Query("SELECT * FROM scores")
    fun getAllScoresStream() : Flow<List<Score>>
}
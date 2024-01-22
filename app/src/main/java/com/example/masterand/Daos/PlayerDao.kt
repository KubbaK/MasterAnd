package com.example.masterand.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.masterand.Entities.Player
import kotlinx.coroutines.flow.Flow

@Dao
interface PlayerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(player: Player): Long

    @Query("SELECT * from players WHERE id = :id")
    fun getPlayerStream(id: Int): Flow<Player>

    @Query("SELECT * from players")
    fun getAllPlayersStream(): Flow<List<Player>>

    @Query("SELECT * from players WHERE email = :email")
    suspend fun getPlayerByEmail(email: String): Player

    @Update
    suspend fun update(player: Player)
}
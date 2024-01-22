package com.example.masterand.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.masterand.Daos.PlayerDao
import com.example.masterand.Daos.PlayerWithScoreDao
import com.example.masterand.Daos.ScoreDao
import com.example.masterand.Entities.Player
import com.example.masterand.Entities.Score

@Database(
    entities = [Score::class, Player::class],
    version = 1,
    exportSchema = false
)
abstract class HighScoreDatabase : RoomDatabase() {
    abstract fun playerDao(): PlayerDao
    abstract fun scoreDao(): ScoreDao
    abstract fun playerScoreDao(): PlayerWithScoreDao

    companion object {
        @Volatile
        private var Instance: HighScoreDatabase? = null
        fun getDatabase(context: Context): HighScoreDatabase {
            return Room.databaseBuilder(
                context,
                HighScoreDatabase::class.java,
                "highscore_database"
            )
                .build().also { Instance = it }
        }
    }
}
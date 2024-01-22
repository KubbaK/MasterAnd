package com.example.masterand

import android.content.Context
import com.example.masterand.Daos.PlayerDao
import com.example.masterand.Daos.PlayerWithScoreDao
import com.example.masterand.Daos.ScoreDao
import com.example.masterand.Database.HighScoreDatabase
import com.google.android.datatransport.runtime.dagger.Module
import com.google.android.datatransport.runtime.dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun providesScoreDao(highScoreDatabase: HighScoreDatabase): ScoreDao {
        return highScoreDatabase.scoreDao()
    }

    @Provides
    fun providesPlayerDao(highScoreDatabase: HighScoreDatabase): PlayerDao {
        return highScoreDatabase.playerDao()
    }

    @Provides
    fun providesPlayerScoreDao(highScoreDatabase: HighScoreDatabase): PlayerWithScoreDao {
        return highScoreDatabase.playerScoreDao()
    }

    @Provides
    @Singleton
    fun provideHighScoreDatabase(
        @ApplicationContext applicationContext: Context
    ): HighScoreDatabase {
        return HighScoreDatabase.getDatabase(applicationContext)
    }
}
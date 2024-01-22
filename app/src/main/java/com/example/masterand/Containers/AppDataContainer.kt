package com.example.masterand.Containers

import android.content.Context
import com.example.masterand.Database.HighScoreDatabase
import com.example.masterand.Repos.PlayersRepository
import com.example.masterand.Repos.PlayersRepositoryImplementation
import com.example.masterand.Repos.ScoresRepository
import com.example.masterand.Repos.ScoresRepositoryImplementation

class AppDataContainer(private val context: Context) : AppContainer {
    override val playersRepository: PlayersRepository by lazy {
        PlayersRepositoryImplementation(HighScoreDatabase.getDatabase(context).playerDao())
    }

    override val scoresRepository: ScoresRepository by lazy {
        val db = HighScoreDatabase.getDatabase(context)
        ScoresRepositoryImplementation(db.scoreDao(), db.playerScoreDao())
    }
}
package com.example.masterand.Containers

import com.example.masterand.Repos.PlayersRepository
import com.example.masterand.Repos.ScoresRepository

interface AppContainer {
    val playersRepository: PlayersRepository
    val scoresRepository: ScoresRepository
}
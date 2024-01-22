package com.example.masterand.ViewModels

import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import com.example.masterand.Entities.Score
import com.example.masterand.Repos.ScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(private val scoresRepository: ScoresRepository) : ViewModel() {
    var score = mutableIntStateOf(1)

    suspend fun submitScore(playerId: Long) {
        scoresRepository.insertScore(Score(0, playerId, score.intValue))
    }
}
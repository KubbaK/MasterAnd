package com.example.masterand.ViewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.masterand.Repos.PlayersRepository
import com.example.masterand.Repos.ScoresRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ScoresViewModel @Inject constructor(
    private val scoresRepository: ScoresRepository,
    private val playersRepository: PlayersRepository
) : ViewModel() {
    var results = mutableStateOf<List<Pair<String, Int>>?>(null)

    suspend fun downloadResults() {
        val scores = scoresRepository.getAllScoresStream()
        val players = playersRepository.getAllPlayersStream()

        players.collect { playersList ->
            scores.collect { scoresList ->
                results.value = scoresList
                    .sortedBy { score -> score.score }
                    .map { score ->
                        val playerName =
                            playersList.first { player -> player.id == score.playerId }.name
                        Pair(playerName, score.score)
                    }
            }
        }
    }
}
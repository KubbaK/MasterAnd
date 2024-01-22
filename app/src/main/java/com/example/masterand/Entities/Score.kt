package com.example.masterand.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val playerId: Long,
    val score: Int
)
package com.example.masterand.Modules

import com.example.masterand.Repos.PlayersRepository
import com.example.masterand.Repos.PlayersRepositoryImplementation
import com.google.android.datatransport.runtime.dagger.Binds
import com.google.android.datatransport.runtime.dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class) //wstrzykiwanie do view modeli
abstract class PlayersModule {
    @Binds //tworzy metodę łączącą z interfejsem
    abstract fun bindScoresRepository(playersRepositoryImpl: PlayersRepositoryImplementation): PlayersRepository
}
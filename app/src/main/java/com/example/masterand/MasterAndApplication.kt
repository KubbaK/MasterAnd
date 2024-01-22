package com.example.masterand

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MasterAndApplication : Application() {
//    object AppViewModelProvider {
//        val Factory = viewModelFactory {
//            initializer {
//                ProfileViewModel(masterAndApplication().container.playersRepository)
//            }
//            initializer {
//                val container = masterAndApplication().container
//                HighScoresViewModel(container.scoresRepository, container.playersRepository)
//            }
//            initializer {
//                GameViewModel(masterAndApplication().container.scoresRepository)
//            }
//        }
//
//        fun CreationExtras.masterAndApplication(): MasterAndApplication =
//            (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as
//                    MasterAndApplication)
//    }
//
//    private lateinit var container: AppContainer
//
//    override fun onCreate() {
//        super.onCreate()
//        container = AppDataContainer(this)
//    }
}
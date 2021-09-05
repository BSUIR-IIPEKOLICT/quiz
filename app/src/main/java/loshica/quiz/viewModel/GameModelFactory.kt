package loshica.quiz.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameModelFactory(private val playerName: String, val app: Application) :
    ViewModelProvider.AndroidViewModelFactory(app) {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameModel(playerName, app) as T
    }
}
package loshica.quiz.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import loshica.quiz.R
import loshica.quiz.model.Player
import java.text.MessageFormat

class GameModel(playerName: String, val app: Application) : AndroidViewModel(app) {

    private val scoreWeight = 10
    private val inProcess = MutableLiveData<Boolean>()
    private val name = MutableLiveData<String>()
    val counter = MutableLiveData<Int>()

    init {
        inProcess.value = true
        name.value = playerName
        counter.value = 0
    }

    fun calcScore(isCorrect: Boolean) { if (isCorrect) counter.value = counter.value?.plus(scoreWeight) }

    fun finishText(): String {
        return MessageFormat.format(app.getString(R.string.finish_text), name.value, counter.value)
    }

    fun getPlayer(): Player = Player(name.value!!, counter.value!!)

    fun inProcess(): Boolean = inProcess.value!!

    fun cancel() { inProcess.value = false }
}
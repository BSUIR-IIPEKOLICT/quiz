package loshica.quiz.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import loshica.quiz.model.Player

class GameModel : ViewModel() {

    private val scoreWeight = 10
    val inProcess = MutableLiveData<Boolean>()
    private val name = MutableLiveData<String>()
    val counter = MutableLiveData<Int>()

    init {
        inProcess.value = true
        counter.value = 0
    }

    fun setName(playerName: String) { name.value = playerName }

    fun calcScore(isCorrect: Boolean) { if (isCorrect) counter.value = counter.value?.plus(scoreWeight) }

    fun finishText(): String {
        return "Поздравляем, игрок ${name.value}!\nВы заработали ${counter.value} очков."
    }

    fun getPlayer(): Player = Player(name.value!!, counter.value!!)

    fun inProcess(): Boolean = inProcess.value!!

    fun cancel() { inProcess.value = false }
}
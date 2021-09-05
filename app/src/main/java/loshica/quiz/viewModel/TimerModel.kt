package loshica.quiz.viewModel

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TimerModel : ViewModel() {

    val counter = MutableLiveData<Int>()
    var timer: CountDownTimer? = null

    init {
        timer = object : CountDownTimer(15000, 1000) {
            override fun onTick(v: Long) { counter.value = (v / 1000).toInt() }

            override fun onFinish() { counter.value = -1 }
        }.start()
    }

    fun reset() { counter.value = 0 }

    fun isZero() = counter.value == 0

    fun cancel() { timer?.cancel() }
}
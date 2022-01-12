package loshica.quiz.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HelpModel : ViewModel() {

    val counter = MutableLiveData<Int>()

    init {
        counter.value = 3
    }

    fun use() { counter.value = counter.value?.minus(1) }

    fun text(): String = "50/50 (${counter.value})"
}
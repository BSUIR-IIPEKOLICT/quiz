package loshica.quiz.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import loshica.quiz.R
import java.text.MessageFormat

class HelpModel(val app: Application) : AndroidViewModel(app) {

    val counter = MutableLiveData<Int>()

    init {
        counter.value = 3
    }

    fun use() { counter.value = counter.value?.minus(1) }

    fun text(): String = MessageFormat.format(app.getString(R.string.question_help), counter.value)
}
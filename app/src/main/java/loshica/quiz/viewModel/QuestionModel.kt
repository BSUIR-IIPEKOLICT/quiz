package loshica.quiz.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import loshica.quiz.R
import loshica.quiz.model.Question

class QuestionModel(private val app: Application) : AndroidViewModel(app) {

    private val data = MutableLiveData<Array<Question>>()
    val counter = MutableLiveData<Int>()

    init {
        Question.counter = 0
        data.value = arrayOf(
            Question(R.drawable.kotlin, R.array.q1),
            Question(R.drawable.java, R.array.q2),
            Question(R.drawable.ts, R.array.q3),
            Question(R.drawable.kotlin, R.array.q4),
            Question(R.drawable.architecture, R.array.q5),
            Question(R.drawable.angular, R.array.q6),
            Question(R.drawable.nest, R.array.q7),
            Question(R.drawable.question, R.array.q8),
            Question(R.drawable.question, R.array.q9),
            Question(R.drawable.architecture, R.array.q10)
        )
        counter.value = 0
    }

    fun getData(): Array<Question> = data.value!!

    fun incCounter() { counter.value = counter.value?.plus(1) }

    fun checkCounter(pos: Int) { if (pos > counter.value!!) incCounter() }

    fun setChoose(pos: Int, choose: Int) { data.value?.get(pos)!!.choose = choose }

    fun getChoose(pos: Int): Int = data.value?.get(pos)!!.choose

    fun isLast(pos: Int): Boolean = counter.value!! == pos

    fun isCorrect(pos: Int): Boolean {
        return app.resources.getStringArray(data.value?.get(pos)!!.stringsId)[5].toInt() == getChoose(pos)
    }

    fun toastText(pos: Int): String {
        return if (isCorrect(pos)) app.getString(R.string.question_right) else app.getString(R.string.question_wrong)
    }
}
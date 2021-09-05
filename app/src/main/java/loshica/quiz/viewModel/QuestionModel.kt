package loshica.quiz.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import loshica.quiz.R
import loshica.quiz.model.Question

class QuestionModel(private val app: Application) : AndroidViewModel(app) {

    private val data = MutableLiveData<Array<Question>>()
    private val counter = MutableLiveData<Int>()

    init {
        data.value = arrayOf(
            Question(R.drawable.shava, R.array.q1),
            Question(R.drawable.war_and_peace, R.array.q2),
            Question(R.drawable.paris, R.array.q3),
            Question(R.drawable.genii, R.array.q4),
            Question(R.drawable.capuchino, R.array.q5),
            Question(R.drawable.ice_cream, R.array.q6),
            Question(R.drawable.borshc, R.array.q7),
            Question(R.drawable.java, R.array.q8),
            Question(R.drawable.math, R.array.q9),
            Question(R.drawable.mountains, R.array.q10)
        )
        counter.value = 0
    }

    fun getQuestions(): Array<Question> = data.value!!

    fun setChoose(pos: Int, choose: Int) { data.value?.get(pos)!!.choose = choose }

    fun getChoose(pos: Int): Int = data.value?.get(pos)!!.choose

    fun setCounter(pos: Int) { counter.value = pos }

    fun isLeftSwipe(pos: Int): Boolean = counter.value!! < pos

    fun isNotLast(pos: Int): Boolean = isLeftSwipe(pos) || isPassed(pos)

    fun isPassed(pos: Int): Boolean = counter.value!! > pos

    fun isCorrect(pos: Int): Boolean {
        return app.resources.getStringArray(data.value?.get(pos)!!.stringsId)[5].toInt() == getChoose(pos)
    }

    fun toastText(pos: Int): String {
        return if (isCorrect(pos)) app.getString(R.string.question_right)
            else app.getString(R.string.question_wrong)
    }
}
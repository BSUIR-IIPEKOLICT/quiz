package loshica.quiz.viewModel

import loshica.quiz.R

data class Question(var img: Int, var stringsId: Int) {

    var strings: Array<String> = AppState.res()!!.getStringArray(stringsId)
    var pos: Int = counter++
    var choose: Int = -1

    companion object {
        private var counter: Int = 0

        // TODO: Questions data
        val questions = arrayOf(
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
        //
    }
}
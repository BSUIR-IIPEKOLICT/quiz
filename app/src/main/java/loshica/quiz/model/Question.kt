package loshica.quiz.model

data class Question(var img: Int, var stringsId: Int) {

    var pos: Int = counter++
    var choose: Int = -1

    companion object {
        var counter: Int = 0
    }
}
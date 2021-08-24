package loshica.quiz.viewModel;

import loshica.quiz.R;

public class Question {

    // TODO: Questions data
    public static final Question[] questions = {
        new Question(R.drawable.shava, R.array.q1),
        new Question(R.drawable.war_and_peace, R.array.q2),
        new Question(R.drawable.paris, R.array.q3),
        new Question(R.drawable.genii, R.array.q4),
        new Question(R.drawable.capuchino, R.array.q5),
        new Question(R.drawable.ice_cream, R.array.q6),
        new Question(R.drawable.borshc, R.array.q7),
        new Question(R.drawable.java, R.array.q8),
        new Question(R.drawable.math, R.array.q9),
        new Question(R.drawable.mountains, R.array.q10)
    };
    //

    public String[] strings;
    public int img;

    public Question(int img, int strings) {
        this.img = img;
        this.strings = Quiz.res().getStringArray(strings);
    }
}

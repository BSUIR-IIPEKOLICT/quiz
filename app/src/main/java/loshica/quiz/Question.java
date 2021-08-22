package loshica.quiz;

public class Question {
    // TODO: Questions data
    public static final Question[] questions = {
        new Question(R.drawable.omon, R.array.q1),
        new Question(R.drawable.nazi, R.array.q2),
        new Question(R.drawable.elections, R.array.q3),
        new Question(R.drawable.gachi, R.array.q4),
        new Question(R.drawable.question, R.array.q5),
        new Question(R.drawable.autozac, R.array.q6),
        new Question(R.drawable.borshc, R.array.q7),
        new Question(R.drawable.loshica, R.array.q8),
        new Question(R.drawable.crimea, R.array.q9),
        new Question(R.drawable.java, R.array.q10)
    };
    //

    public String[] strings;
    public int img;

    public Question(int img, int strings) {
        this.img = img;
        this.strings = Coordinator.res().getStringArray(strings);
    }
}

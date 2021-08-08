package loshica.quiz;

public class Question {
    // TODO: Questions data
    public static final Question[] questions = {
        new Question(R.array.q1_strings, R.array.q1_int)
    };
    //

    public String[] strings;
    public int[] numbers;

    public Question(int strings, int numbers) {
        this.strings = App.res().getStringArray(strings);
        this.numbers = App.res().getIntArray(numbers);
    }
}

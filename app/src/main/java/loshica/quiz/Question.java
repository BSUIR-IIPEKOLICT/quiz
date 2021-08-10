package loshica.quiz;

public class Question {
    // TODO: Questions data
    public static final Question[] questions = {
        new Question(R.array.q1_strings, R.drawable.omon, R.array.q1_numbers)
    };
    //

    public String[] strings;
    public int img;
    public int[] numbers;

    public Question(int strings, int img, int numbers) {
        this.strings = App.res().getStringArray(strings);
        this.img = img;
        this.numbers = App.res().getIntArray(numbers);
    }
}

package loshica.quiz;

public class Question {

    public static final Question[] questions = {
        new Question(
            "Кто изображен на картинке?",
            "Гитлер",
            "Парень кафтанчиковой",
            "Пидорас",
            "Воин света",
            0,
            2
        )
    };

    public String text;
    public String btn0;
    public String btn1;
    public String btn2;
    public String btn3;

    public int img;
    public int right;

    public Question(
        String text, String btn0, String btn1, String btn2, String btn3,
        int img, int right
    ) {
        this.text = text;
        this.btn0 = btn0;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.img = img;
        this.right = right;
    }
}

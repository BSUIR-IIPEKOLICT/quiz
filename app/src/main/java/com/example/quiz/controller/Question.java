package com.example.quiz.controller;

public class Question {

    // TODO: Вопросы квиза
    public static final Question[] questions = {
        new Question(
            "В какой стране придумали шаурму?", // вопрос
            "В России", // 1 вариант ответа
            "В Германии", // 2 вариант ответа
            "В Лошице", // 3 вариант ответа
            "В Китае", // 4 вариант ответа
            1 // номер правильного варианта ответа (отсчет начинается с 0)
        ),
        new Question(
            "Кто написал книгу \"Война и мир\"?",
            "Достоевский",
            "Сладкий",
            "Толстой",
            "Горький",
            2
        ),
        new Question(
            "Откуда родом мороженное?",
            "Из Китая",
            "Из Англии",
            "Из Бразилии",
            "Из Италии",
            0
        ),
        new Question(
            "Какой язык более предпочтителен для Android-разработки?",
            "Assembler",
            "Kotlin",
            "C",
            "Java",
            1
        )
    };
    //

    public String question;
    public String btn1;
    public String btn2;
    public String btn3;
    public String btn4;
    public int right;

    public Question(String question, String btn1, String btn2, String btn3, String btn4, int right) {
        this.question = question;
        this.btn1 = btn1;
        this.btn2 = btn2;
        this.btn3 = btn3;
        this.btn4 = btn4;
        this.right = right;
    }
}

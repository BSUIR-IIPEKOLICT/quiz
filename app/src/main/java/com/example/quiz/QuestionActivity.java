package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import java.util.Objects;

import com.example.quiz.controller.Quiz;
import com.example.quiz.view.FinishFragment;
import com.example.quiz.view.QuestionAdapter;
import com.example.quiz.view.QuestionFragment;

public class QuestionActivity extends AppCompatActivity implements
    QuestionFragment.QuestionFragmentListener,
    FinishFragment.FinishFragmentListener {

    ViewPager2 qp;
    QuestionAdapter qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // QuestionPager
        qp = findViewById(R.id.question_pager); // находим вьюпейджер
        qa = new QuestionAdapter(this); // создаем для него адаптер
        qp.setAdapter(qa); // устанавливаем вьюпейджеру адаптер
        qp.setCurrentItem(0); // стартовая страничка - первая
        qp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            // слушатель показываемого фрагмента вьюпейджера
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                // обработчик выбранной странички
                if (Quiz.inProcess && position == qa.getItemCount() - 1) {
                    // если последняя:
                    Quiz.check(); // вызвать метод check из Quiz класса
                }
            }
        });
        //
    }

    // обработчик для кнопки назад в finish fragment
    @Override
    public void finish() {
        Quiz.resetScore(); // сбросить очки и имя игрока
        startActivity(new Intent(this, MainActivity.class)); // перейти в маин активити
    }
    //

    // событие при нажатии кнопки-варианта ответа в question fragment
    @Override
    public void next(boolean isCorrect) {
        Quiz.calcScore(isCorrect); // рассчет кол-ва баллов за вопрос
        qp.setCurrentItem(qp.getCurrentItem() + 1, true); // программно перелистнуть на след фрагмент
    }
    //
}
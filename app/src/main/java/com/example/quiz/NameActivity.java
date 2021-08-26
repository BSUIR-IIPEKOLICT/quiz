package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.quiz.controller.Quiz;

import java.util.Objects;

public class NameActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        et = findViewById(R.id.edit_player_name); // найти поле ввода текста
        btn = findViewById(R.id.name_ok); // найти кнопку ок

        btn.setOnClickListener(this); // повесить на кнопку ок слушатель клика
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.name_ok && !et.getText().toString().equals("")) {
            // если нажата кнопка ок и поле ввода не пустое
            Quiz.startGame(et.getText().toString()); // начать игру, установив имя игрока из поля ввода
            Quiz.updateMaps(); // обновить мапы
            startActivity(new Intent(this, QuestionActivity.class)); // перейти на активити с вопросами
        }
    }
}
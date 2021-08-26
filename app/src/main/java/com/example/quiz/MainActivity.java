package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button play;
    Button leaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.main_play); // найти кнопку играть
        leaderboard = findViewById(R.id.main_leaderboard); // найти кнопку рейтинг

        play.setOnClickListener(this); // обработчики клика по кнопкам
        leaderboard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_play) {
            // если нажата кнопка играть - открыть активити ввода имени игрока
            startActivity(new Intent(this, NameActivity.class));
        } else {
            // если нажата кнопка рейтинг - открыть leaderboard активити
            startActivity(new Intent(this, LeaderboardActivity.class));
        }
    }
}
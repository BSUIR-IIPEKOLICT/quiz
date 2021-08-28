package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.quiz.controller.Quiz;
import com.example.quiz.view.LeaderboardAdapter;

import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    RecyclerView rv;
    LeaderboardAdapter la;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        btn = findViewById(R.id.leaderboard_back); // найти кнопку назад
        btn.setOnClickListener(this); // повесить на нее слушатель клика

        rv = findViewById(R.id.recycler_view); // найти ресуклер вью
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext())); // суем туда лм
        la = new LeaderboardAdapter(Quiz.players); // создаем адаптер для этого списка
        rv.setAdapter(la); // устанавливаем адаптер
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.leaderboard_back) {
            // если нажата кнопка назад - запустить маин активити
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    // Событие показа активити
    @Override
    public void onResume() {
        super.onResume();

        if (la.getItemCount() != Quiz.players.size()) {
            // если число игроков в сете игроков и в таблице рейтинга не совпадает - пересоздать активити
            recreate();
        }

        if (Quiz.updateLeaderboard) {
            // если в Quiz классе активен флаг обновления лидербоарда - обновляем список игроков
            la.notifyDataSetChanged();
            Quiz.finishLeaderboardUpdate(); // и сбрасываем флаг
        }
    }
    //
}
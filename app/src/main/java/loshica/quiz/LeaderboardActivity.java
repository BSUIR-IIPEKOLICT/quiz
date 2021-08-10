package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

public class LeaderboardActivity extends AppCompatActivity implements
    View.OnClickListener {

    RecyclerView rv;
    LeaderboardAdapter la;
    Button btn;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        new Theme(this).set();
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        rv = findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        la = new LeaderboardAdapter(App.usersJava);
        rv.setAdapter(la);

        btn = findViewById(R.id.leaderboard_back);
        btn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.updateLeaderboard) {
            la.notifyDataSetChanged();
            App.updateLeaderboard = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.leaderboard_back) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
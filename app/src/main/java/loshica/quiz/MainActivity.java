package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements
    View.OnClickListener,
    NameDialog.NameDialogListener {

    Button play;
    Button leaderboard;
    NameDialog dialog;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        new Theme(this).set();
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = findViewById(R.id.main_play);
        leaderboard = findViewById(R.id.main_leaderboard);

        play.setOnClickListener(this);
        leaderboard.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity((item.getItemId() == R.id.action_settings) ?
            new Intent(this, SettingsActivity.class) : null);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_play) {
            dialog = new NameDialog();
            dialog.show(getSupportFragmentManager(), null);
        } else {
            startActivity(new Intent(this, LeaderboardActivity.class));
        }
    }

    @Override
    public void name(String username) {
        App.name = username;
        App.score = 0;
        App.inProgress = true;
        startActivity(new Intent(this, QuestionActivity.class));
    }
}
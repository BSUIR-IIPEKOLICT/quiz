package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Objects;

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
        Context context = getApplicationContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String currentAC = Objects.requireNonNull(settings.getString("AccentColor", "Stock"));
        boolean currentDM = settings.getBoolean("DarkMode", true);
        Theme.set(this, currentDM, currentAC);
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        play = (Button) findViewById(R.id.main_play);
        leaderboard = (Button) findViewById(R.id.main_leaderboard);

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
        Intent target = (item.getItemId() == R.id.action_settings) ?
            new Intent(this, SettingsActivity.class) :
            new Intent(this, AboutActivity.class);
        startActivity(target);
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
        Data.user.name = username;
        Data.user.score = 0;
        Data.inProgress = true;
        startActivity(new Intent(this, QuestionActivity.class));
    }
}
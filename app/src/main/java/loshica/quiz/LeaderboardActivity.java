package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashSet;

public class LeaderboardActivity extends AppCompatActivity implements
    View.OnClickListener {

    RecyclerView rv;
    LeaderboardAdapter la;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        new Theme(this).set();
        //

        Data.users = getSharedPreferences(Data.USERS, Context.MODE_PRIVATE);
        Data.usersJson = Data.users.getStringSet(Data.USERS, new HashSet<String>());
        for (String itemJson : Data.usersJson) {
            User itemJava = Data.json.fromJson(itemJson, User.class);
            Data.usersJava.add(itemJava);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        rv = (RecyclerView) findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(this));
        la = new LeaderboardAdapter(Data.usersJava);
        rv.setAdapter(la);

        btn = (Button) findViewById(R.id.leaderboard_back);
        btn.setOnClickListener(this);
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
    protected void onStart() {
        super.onStart();
        if (Data.updateLeaderboard) {
            recreate();
            Data.updateLeaderboard = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.leaderboard_back) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
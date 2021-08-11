package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

public class QuestionActivity extends AppCompatActivity implements
    QuestionFragment.QuestionFragmentListener,
    FinishFragment.FinishFragmentListener {

    ViewPager2 qp;
    QuestionAdapter qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        new Theme(this).set();
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // QuestionPager
        qp = findViewById(R.id.question_pager);
        qa = new QuestionAdapter(this);
        qp.setAdapter(qa);
        qp.setCurrentItem(0);
        qp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (App.inProcess && position == qa.getItemCount() - 1) {
                    checkSet();
                    App.inProcess = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        qp.setPageTransformer(new QuestionTransformer());
        //
    }

    @Override
    public void finish() {
        App.score = 0;
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void next(boolean isCorrect) {
        if (App.inProcess && isCorrect) { App.score += 5; }
        qp.setCurrentItem(qp.getCurrentItem() + 1, true);
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
        if ((qp.getCurrentItem() > 0)) { qp.setCurrentItem(qp.getCurrentItem() - 1, true); }
        else { startActivity(new Intent(this, MainActivity.class)); }
    }

    public void checkSet() {
        User user = new User(App.name, App.score);
        boolean exists = false;

        if (!App.usersJava.contains(user)) {
            for (User item : App.usersJava) {
                if (item.name.equals(user.name)) {
                    exists = true;
                    if (item.score < user.score) {
                        App.usersJava.remove(item);
                        App.usersJson.remove(App.json.toJson(item, User.class));
                        save(user);
                    }
                }
            }
            if (!exists) { save(user); }
        }
    }

    public void save(User user) {
        App.usersJava.add(user);
        App.usersJson.add(App.json.toJson(user, User.class));
        SharedPreferences.Editor editor = App.users.edit();
        editor.remove(App.USERS);
        editor.apply();
        editor.putStringSet(App.USERS, App.usersJson);
        editor.apply();
        App.updateLeaderboard = true;
    }
}
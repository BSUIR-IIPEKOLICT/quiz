package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.HashSet;
import java.util.Objects;

public class QuestionActivity extends AppCompatActivity implements
    QuestionFragment.QuestionFragmentListener,
    FinishFragment.FinishFragmentListener {

    ViewPager qp;
    QuestionAdapter qa;
    QuestionFragment q;
//    public static final Question[] questions = new Question[]{
////        new Question(App.res().getStringArray(R.array.q1_strings), 0, 2)
//        new Question(
//            new String[]{
//                "Кто изображен на картинке?",
//                "Гитлер",
//                "Парень кафтанчиковой",
//                "Пидорас",
//                "Воин света"
//            },
//            0,
//            2
//        )
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        Context context = getApplicationContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String currentAC = Objects.requireNonNull(settings.getString("AccentColor", "Stock"));
        boolean currentDM = settings.getBoolean("DarkMode", true);
        Theme.set(this, currentDM, currentAC);
        //

        Data.users = getSharedPreferences(Data.USERS, Context.MODE_PRIVATE);
        Data.usersJson = Data.users.getStringSet(Data.USERS, new HashSet<String>());
        for (String itemJson : Data.usersJson) {
            User itemJava = Data.json.fromJson(itemJson, User.class);
            Data.usersJava.add(itemJava);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // QuestionPager
        qp = (ViewPager) findViewById(R.id.questionPager);
        qa = new QuestionAdapter(getSupportFragmentManager());
        qp.setAdapter(qa);
        qp.setCurrentItem(0);
        qp.setPageTransformer(false, (page, position) -> {
            final float opacity = Math.abs(Math.abs(position) - 1);
            page.setAlpha(opacity);
        });
        //
    }

    @Override
    public void save() {
        Data.user.score = Data.score;
        if (!Data.usersJava.contains(Data.user)) {
            Data.usersJava.add(Data.user);
            Data.usersJson.add(Data.json.toJson(Data.user, User.class));
            SharedPreferences.Editor editor = Data.users.edit();
            editor.remove(Data.USERS);
            editor.apply();
            editor.putStringSet(Data.USERS, Data.usersJson);
            editor.apply();
            Data.inProgress = false;
        }
    }

    @Override
    public void finish() {
        Data.score = 0;
        Data.user.name = "";
        Data.user.score = 0;
        Data.updateLeaderboard = true;
        reloadQuestions();
    }

    @Override
    public void back() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void next(boolean isCorrect) {
        if (Data.inProgress && isCorrect) { Data.score += 5; }
        if (qp.getCurrentItem() == qa.getCount() - 2) {
            for (int i = 0; i < qa.getCount() - 1; i++) {
                q = (QuestionFragment) getSupportFragmentManager()
                    .findFragmentByTag("android:switcher:" + R.id.questionPager + ":" + i);
                assert q != null;
                q.rg.clearCheck();
            }
            Data.inProgress = false;
        }
        qp.setCurrentItem(qp.getCurrentItem() + 1, true);
        if (qp.getCurrentItem() == qa.getCount() - 1) {
            recreate();
        }
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

    public void reloadQuestions() {
        for (int i = 0; i < qa.getCount() - 1; i++) {
            q = (QuestionFragment) getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.questionPager + ":" + i);
            assert q != null;
            q.isChecked = false;
            q.rg.clearCheck();
            for (int j = 0; j < q.rg.getChildCount(); j++) {
                q.rg.getChildAt(j).setClickable(true);
                q.rg.getChildAt(j).setAlpha(1);
            }
        }
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
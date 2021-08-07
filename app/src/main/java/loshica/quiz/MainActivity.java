package loshica.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements
    LeaderboardFragment.LeaderboardFragmentListener,
    MainFragment.MainFragmentListener,
    QuestionFragment.QuestionFragmentListener,
    FinishFragment.FinishFragmentListener,
    NameDialog.NameDialogListener {

    //Toast.makeText(getApplicationContext(), users.getStringSet(USERS, new HashSet<String>()).toString(), Toast.LENGTH_LONG).show();

    public static final String USERS = "users";

    public static Gson gson = new Gson();
    public static SharedPreferences users;
    public static Set<String> usersJson = new HashSet<String>();
    public static Set<User> usersJava = new HashSet<User>();

    ViewPager vp;
    VpAdapter adapter;
    public static int score;
    public static String name;
    public static User user;
    public static Boolean inProgress = false;

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

        users = getSharedPreferences(USERS, Context.MODE_PRIVATE);
        usersJson = users.getStringSet(USERS, new HashSet<String>());
        for (String itemJson : usersJson) {
            User itemJava = gson.fromJson(itemJson, User.class);
            usersJava.add(itemJava);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ViewPager
        vp = (ViewPager) findViewById(R.id.viewPager);
        adapter = new VpAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        vp.setCurrentItem(1);
        vp.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                final float opacity = Math.abs(Math.abs(position) - 1);
                page.setAlpha(opacity);
            }
        });
        //
    }

    @Override
    public void back() { vp.setCurrentItem(1, true); }

    @Override
    public void updateLeaderboard() {
        LeaderboardFragment l = (LeaderboardFragment) getSupportFragmentManager()
            .findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 0);
        assert l != null;
        l.adapter.notifyDataSetChanged();
    }

    @Override
    public void save() {
        if (!usersJava.contains(user)) {
            usersJava.add(user);
            usersJson.add(gson.toJson(user));
            SharedPreferences.Editor editor = users.edit();
            editor.remove(USERS);
            editor.apply();
            editor.putStringSet(USERS, usersJson);
            editor.apply();
            inProgress = false;
        }
    }

    @Override
    public void name(String username) {
        user = new User(username, 0);
        name = username;
        score = 0;
        vp.setCurrentItem(2, true);
        inProgress = true;
        reloadQuestions();
    }

    @Override
    public void reset() {
        if (!inProgress) {
            name = null;
            score = 0;
        }
    }

    @Override
    public void leaderboard() { vp.setCurrentItem(0, true); }

    @Override
    public void play() {
        NameDialog dialog = new NameDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public void next(boolean isCorrect) {
        if (inProgress && isCorrect) { user.setScore(score += 5); }
        FinishFragment finish = (FinishFragment) getSupportFragmentManager()
            .findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + 3);
        assert finish != null;
        finish.reloadText();
        if (vp.getCurrentItem() == adapter.getCount() - 2) {
            save();
            updateLeaderboard();
            for (int i = 2; i < 3; i++) {
                QuestionFragment q = (QuestionFragment) getSupportFragmentManager()
                    .findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + i);
                assert q != null;
                q.rg.clearCheck();
            }
            inProgress = false;
        }
        vp.setCurrentItem(vp.getCurrentItem() + 1, true);
    }

    @Override
    public void reloadQuestions() {
        for (int i = 2; i < 3; i++) {
            QuestionFragment q = (QuestionFragment) getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.viewPager + ":" + i);
            assert q != null;
            q.isChecked = false;
            q.rg.clearCheck();
            for (int j = 0; j < q.rg.getChildCount(); j++) {
                q.rg.getChildAt(j).setClickable(true);
                q.rg.getChildAt(j).setAlpha(1);
            }
        }
    }

    public static class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new LeaderboardFragment();
                case 1: return new MainFragment();
                case 2: return QuestionFragment.newInstance(Question.questions[0]);
                default: return new FinishFragment();
            }
        }

        @Override
        public int getCount() { return 4; }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.action_about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
        }

        return super.onOptionsItemSelected(item);
    }
}
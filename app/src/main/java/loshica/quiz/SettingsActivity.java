package loshica.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

public class SettingsActivity extends AppCompatActivity implements
    View.OnClickListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        Theme theme = new Theme(this);
        theme.set();
        SharedPreferences settings = theme.getSettings();
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.settings, new SettingsFragment())
            .commit();

        settings.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View v) {}

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        // TODO Проверять общие настройки, ключевые параметры и изменять UI
        // или поведение приложения, если потребуется.

        if (key.equals("AccentColor")) recreate();
        else if (key.equals("DarkMode")) recreate();
    }

    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}

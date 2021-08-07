package loshica.quiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String currentAC = Objects.requireNonNull(settings.getString("AccentColor", "Stock"));
        boolean currentDM = settings.getBoolean("DarkMode", true);
        Theme.set(this, currentDM, currentAC);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.settings, new SettingsFragment())
            .commit();

        settings.registerOnSharedPreferenceChangeListener(this);
    }

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
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
    }
}

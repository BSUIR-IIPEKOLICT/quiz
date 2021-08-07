package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        String currentAC = Objects.requireNonNull(settings.getString("AccentColor", "Stock"));
        boolean currentDM = settings.getBoolean("DarkMode", true);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Theme.set(this, currentDM, currentAC);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onBackPressed() {
        Intent back = new Intent(this, MainActivity.class);
        startActivity(back);
    }
}
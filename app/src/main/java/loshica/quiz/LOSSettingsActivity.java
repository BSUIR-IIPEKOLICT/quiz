package loshica.quiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;

import androidx.appcompat.app.AppCompatActivity;

public class LOSSettingsActivity extends AppCompatActivity implements
    View.OnClickListener,
    LOSRadioButtonDialog.RadioButtonDialogListener,
    LOSAccentDialog.LOSAccentDialogListener {

    LOSAccentDialog accentDialog;
    LOSRadioButtonDialog themeDialog;

    CheckedTextView accentView, darkView, aboutView;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // LOSTheme
        LOSTheme theme = new LOSTheme(this);
        settings = theme.settings;
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        accentView = findViewById(R.id.accent);
        darkView = findViewById(R.id.dark);
        aboutView = findViewById(R.id.about);

        accentView.setOnClickListener(this);
        darkView.setOnClickListener(this);
        aboutView.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accent:
                accentDialog = new LOSAccentDialog();
                accentDialog.show(getSupportFragmentManager(), null);
                break;
            case R.id.dark:
                themeDialog = LOSRadioButtonDialog.newInstance(
                    getResources().getIntArray(R.array.theme_values),
                    getResources().getStringArray(R.array.theme_labels),
                    LOSTheme.THEME_KEY, LOSTheme.SETTINGS, LOSTheme.THEME_DEFAULT,
                    R.string.theme_section,
                    settings.getInt(LOSTheme.THEME_KEY, LOSTheme.THEME_DEFAULT)
                );
                themeDialog.show(getSupportFragmentManager(), null);
                break;
            default:
                new LOSAboutDialog().show(getSupportFragmentManager(), null);
                break;
        }
    }

    public void onBackPressed() { startActivity(new Intent(this, MainActivity.class)); }

    @Override
    public void change(String key, int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
        themeDialog.dismiss();
        recreate();
    }

    @Override
    public void setAccent(int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(LOSTheme.ACCENT_KEY, value);
        editor.apply();
        accentDialog.dismiss();
        recreate();
    }
}

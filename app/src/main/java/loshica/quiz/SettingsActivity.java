package loshica.quiz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity implements
    View.OnClickListener,
    RadioButtonDialog.RadioButtonDialogListener {

    CheckedTextView accentView, darkView, aboutView;
    RadioButtonDialog rbd;
    AboutDialog ad;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Theme
        Theme theme = new Theme(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.accent:
                rbd = RadioButtonDialog.newInstance(
                    getResources().getIntArray(R.array.accent_values),
                    getResources().getStringArray(R.array.accent_entries),
                    Theme.ACCENT_KEY, Theme.SETTINGS, Theme.ACCENT_DEFAULT,
                    R.string.accentColor_header_text,
                    settings.getInt(Theme.ACCENT_KEY, Theme.ACCENT_DEFAULT)
                );
                break;
            case R.id.dark:
                rbd = RadioButtonDialog.newInstance(
                    getResources().getIntArray(R.array.theme_values),
                    getResources().getStringArray(R.array.theme_labels),
                    Theme.THEME_KEY, Theme.SETTINGS, Theme.THEME_DEFAULT,
                    R.string.darkMode_text,
                    settings.getInt(Theme.THEME_KEY, Theme.THEME_DEFAULT)
                );
                break;
            default:
                ad = new AboutDialog();
                break;
        }
        if (rbd != null) rbd.show(getSupportFragmentManager(), null);
        else ad.show(getSupportFragmentManager(), null);
    }

    public void onBackPressed() { startActivity(new Intent(this, MainActivity.class)); }

    @Override
    public void change(String key, int value) {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.apply();
        rbd.dismiss();
        recreate();
    }
}

package loshica.quiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Theme {

    private final String ACCENT_KEY = "AccentColor";
    private final String DARK_KEY = "DarkMode";
    private final String ACCENT_DEFAULT = "Stock";
    private final boolean DARK_DEFAULT = true;

    private Context context;
    private SharedPreferences settings;
    private String accent;
    private boolean dark;
    private Activity activity;

    Theme(Activity activity) {
        this.context = App.context().getApplicationContext();
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
        this.accent = settings.getString(ACCENT_KEY, ACCENT_DEFAULT);
        this.dark = settings.getBoolean(DARK_KEY, DARK_DEFAULT);
        this.activity = activity;
    }

    public void set() {
        if (dark)
            switch (accent) {
                case "Brown":
                    activity.setTheme(R.style.Theme_BlackBrown);
                    break;
                case "Orange":
                    activity.setTheme(R.style.Theme_BlackOrange);
                    break;
                case "LGreen":
                    activity.setTheme(R.style.Theme_BlackLGreen);
                    break;
                case "Violet":
                    activity.setTheme(R.style.Theme_BlackViolet);
                    break;
                case "Cyan":
                    activity.setTheme(R.style.Theme_BlackCyan);
                    break;
                case "Pink":
                    activity.setTheme(R.style.Theme_BlackPink);
                    break;
                case "Aosp":
                    activity.setTheme(R.style.Theme_BlackAosp);
                    break;
                case "Red":
                    activity.setTheme(R.style.Theme_BlackRed);
                    break;
                case "DBlue":
                    activity.setTheme(R.style.Theme_BlackDBlue);
                    break;
                case "DGreen":
                    activity.setTheme(R.style.Theme_BlackDGreen);
                    break;
                case "BlackWhite":
                    activity.setTheme(R.style.Theme_BlackWhite);
                    break;
                default:
                    activity.setTheme(R.style.Theme_BlackStock);
                    break;
            }
        else
            switch (accent) {
                case "Brown":
                    activity.setTheme(R.style.Theme_LightBrown);
                    break;
                case "Orange":
                    activity.setTheme(R.style.Theme_LightOrange);
                    break;
                case "LGreen":
                    activity.setTheme(R.style.Theme_LightLGreen);
                    break;
                case "Violet":
                    activity.setTheme(R.style.Theme_LightViolet);
                    break;
                case "Cyan":
                    activity.setTheme(R.style.Theme_LightCyan);
                    break;
                case "Pink":
                    activity.setTheme(R.style.Theme_LightPink);
                    break;
                case "Aosp":
                    activity.setTheme(R.style.Theme_LightAosp);
                    break;
                case "Red":
                    activity.setTheme(R.style.Theme_LightRed);
                    break;
                case "DBlue":
                    activity.setTheme(R.style.Theme_LightDBlue);
                    break;
                case "DGreen":
                    activity.setTheme(R.style.Theme_LightDGreen);
                    break;
                case "BlackWhite":
                    activity.setTheme(R.style.Theme_LightBlack);
                    break;
                default:
                    activity.setTheme(R.style.Theme_LightStock);
                    break;
            }
    }

    public SharedPreferences getSettings() {
        return this.settings;
    }


/*
    public static void oneuiMode(Activity activity, boolean mode) {
        if (mode) activity.setTheme(R.style.Theme_oneui);
    }

    public static void oneuiMode(ActionBar actionBar, boolean mode) {
        if (mode) actionBar.setElevation(-100);
    }

    public static void oneuiMode(ActionBar actionBar, boolean mode) {
        if (mode) actionBar.setElevation(-100);
    }
*/
}


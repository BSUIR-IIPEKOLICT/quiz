package loshica.quiz;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class LOSTheme {

    public static final String SETTINGS = "settings";
    public static final String ACCENT_KEY = "accent";
    public static final String THEME_KEY = "theme";
    public static final int ACCENT_DEFAULT = 2;
    public static final int THEME_DEFAULT = 0;

    public static final int[] coloredBgs = new int[]{
        R.drawable.colored_btn_bg_oxygen,
        R.drawable.colored_btn_bg_violet,
        R.drawable.colored_btn_bg_red,
        R.drawable.colored_btn_bg_brown,
        R.drawable.colored_btn_bg_cyan,
        R.drawable.colored_btn_bg_dblue,
        R.drawable.colored_btn_bg_orange,
        R.drawable.colored_btn_bg_pink,
        R.drawable.colored_btn_bg_dgreen,
        R.drawable.colored_btn_bg_lgreen,
        R.drawable.colored_btn_bg_aosp,
        R.drawable.colored_btn_bg_black,
        R.drawable.colored_btn_bg_white
    };

    private final int[] darkThemes = new int[]{
        R.style.Theme_BlackStock,
        R.style.Theme_BlackViolet,
        R.style.Theme_BlackRed,
        R.style.Theme_BlackBrown,
        R.style.Theme_BlackCyan,
        R.style.Theme_BlackDBlue,
        R.style.Theme_BlackOrange,
        R.style.Theme_BlackPink,
        R.style.Theme_BlackDGreen,
        R.style.Theme_BlackLGreen,
        R.style.Theme_BlackAosp,
        R.style.Theme_BlackWhite
    };

    private final int[] lightThemes = new int[]{
        R.style.Theme_LightStock,
        R.style.Theme_LightViolet,
        R.style.Theme_LightRed,
        R.style.Theme_LightBrown,
        R.style.Theme_LightCyan,
        R.style.Theme_LightDBlue,
        R.style.Theme_LightOrange,
        R.style.Theme_LightPink,
        R.style.Theme_LightDGreen,
        R.style.Theme_LightLGreen,
        R.style.Theme_LightAosp,
        R.style.Theme_LightBlack
    };

    public SharedPreferences settings;
    public int current;
    public static boolean isSystemDark;

    LOSTheme(Activity activity) {
        this.settings = activity.getSharedPreferences(SETTINGS, Context.MODE_PRIVATE);

        int theme = settings.getInt(THEME_KEY, THEME_DEFAULT);
        int accent = settings.getInt(ACCENT_KEY, ACCENT_DEFAULT);

        this.current = set(theme, accent, activity);
        activity.setTheme(current);
    }

    private int set(int theme, int accent, Activity activity) {
        isSystemDark = activity.getResources().getConfiguration().uiMode > 24;
        switch (theme) {
            case 0: return (isSystemDark) ? darkThemes[accent] : lightThemes[accent];
            case 1: return darkThemes[accent];
            default: return lightThemes[accent];
        }
    }
}


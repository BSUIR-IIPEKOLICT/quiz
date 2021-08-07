package loshica.quiz;

import android.app.Activity;

public class Theme {

    public static void set(Activity activity, boolean darkMode, String accentColor) {
        if (darkMode)
            switch (accentColor) {
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
                case "Yellow":
                    activity.setTheme(R.style.Theme_BlackYellow);
                    break;
                default:
                    activity.setTheme(R.style.Theme_BlackStock);
                    break;
            }
        else
            switch (accentColor) {
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
                case "Yellow":
                    activity.setTheme(R.style.Theme_LightYellow);
                    break;
                default:
                    activity.setTheme(R.style.Theme_LightStock);
                    break;
            }
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


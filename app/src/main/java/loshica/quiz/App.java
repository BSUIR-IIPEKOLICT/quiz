package loshica.quiz;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Resources res;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        res = getResources();
    }

    public static Context context() { return mContext; }

    public static Resources res() { return res; }
}

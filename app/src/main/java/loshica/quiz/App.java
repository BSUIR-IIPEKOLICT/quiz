package loshica.quiz;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class App extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Resources res;

    public static final String USERS = "users";

    public static Gson json = new Gson();
    public static SharedPreferences users;
    public static Set<String> usersJson = new HashSet<>();
    public static Set<User> usersJava = new HashSet<>();

    public static String name = "";
    public static int score = 0;
    public static int help = 3;
    public static boolean inProgress = false;
    public static boolean updateLeaderboard = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        res = getResources();

        users = getSharedPreferences(USERS, Context.MODE_PRIVATE);
        usersJson = users.getStringSet(USERS, new HashSet<>());
        for (String itemJson : usersJson) {
            User itemJava = json.fromJson(itemJson, User.class);
            usersJava.add(itemJava);
        }
    }

    public static Context context() { return mContext; }

    public static Resources res() { return res; }
}

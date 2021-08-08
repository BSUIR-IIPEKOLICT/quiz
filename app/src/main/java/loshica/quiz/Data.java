package loshica.quiz;

import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Set;

public class Data {
    public static final String USERS = "users";

    public static Gson json = new Gson();
    public static SharedPreferences users;
    public static Set<String> usersJson = new HashSet<String>();
    public static Set<User> usersJava = new HashSet<User>();

    public static User user = new User("", 0);
    public static int score = 0;
    public static boolean inProgress = false;
    public static boolean updateLeaderboard = false;
}

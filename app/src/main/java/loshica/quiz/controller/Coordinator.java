package loshica.quiz.controller;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.mongodb.Credentials;
import loshica.quiz.model.Player;
import loshica.quiz.model.Question;
import loshica.quiz.model.Database;

public class Coordinator extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Resources res;

    public static final String PLAYERS = "players";

    public static Gson json = new Gson();
    public static SharedPreferences players;
    public static Set<String> playersJson = new HashSet<>();
    public static Set<Player> playersJava = new HashSet<>();

    public static String name = "";
    public static int score = 0;
    public static int help = 3;
    public static boolean inProcess = false;
    public static boolean updateLeaderboard = false;
    public static boolean online = false;

    public static Map<Integer, Boolean> isChecked = new HashMap<>();
    public static Map<Integer, Integer> choose = new HashMap<>();

    static Player newPlayer;
    static Player existsPlayer;
    static boolean exists;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        res = getResources();

        players = getSharedPreferences(PLAYERS, Context.MODE_PRIVATE);

        // Connect to Mongo
        Realm.init(this);
        Database.app.loginAsync(Credentials.anonymous(), result -> {
            if (result.isSuccess()) {
                online = true;
                Database.init();
                playersJava = Database.getPlayers();
                for (Player playerJava : playersJava) {
                    playersJson.add(json.toJson(playerJava, Player.class));
                }
                updateLeaderboard = true;
            }
        });
        //

        if (!online) {
            playersJson = players.getStringSet(PLAYERS, new HashSet<>());
            for (String playerJson : playersJson) {
                playersJava.add(json.fromJson(playerJson, Player.class));
            }
        }

        updateMaps();
    }

    public static Context context() { return mContext; }

    public static Resources res() { return res; }

    public static void startGame(String playerName) {
        name = playerName;
        score = 0;
        inProcess = true;
    }

    public static void updateMaps() {
        for (int i = 0; i < Question.questions.length; i++) {
            isChecked.put(i, false);
            choose.put(i, -1);
        }
    }

    public static void resetScore() { score = 0; }

    public static void calcScore(boolean isCorrect) { if (inProcess && isCorrect) score += 5; }

    public static void localSave() {
        SharedPreferences.Editor editor = players.edit();
        editor.remove(PLAYERS);
        editor.apply();
        editor.putStringSet(PLAYERS, playersJson);
        editor.apply();
    }

    public static void check() {
        newPlayer = new Player(name, score);
        existsPlayer = null;
        exists = false;

        if (!playersJava.contains(newPlayer)) {
            for (Player p : playersJava) {
                if (p.name.equals(newPlayer.name)) {
                    exists = true;
                    if (p.score < newPlayer.score) existsPlayer = p;
                }
            }
            if (!exists) save(newPlayer);
            else if (exists && existsPlayer != null) {
                playersJava.remove(existsPlayer);
                playersJson.remove(json.toJson(existsPlayer, Player.class));
                if (online) Database.removePlayer(existsPlayer);
                save(newPlayer);
            }
        }

        inProcess = false;
    }

    private static void save(Player player) {
        playersJava.add(player);
        if (online) Database.addPlayer(player);
        playersJson.add(json.toJson(player, Player.class));
        localSave();
        updateLeaderboard = true;
    }

    public static void finishLeaderboardUpdate() { updateLeaderboard = false; }
}

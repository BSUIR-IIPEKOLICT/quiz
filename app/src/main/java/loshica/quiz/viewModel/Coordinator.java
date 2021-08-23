package loshica.quiz.viewModel;

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
import loshica.quiz.model.Database;

public class Coordinator extends Application {

    // TODO: Удобняшки (доступ к ресурсам/контексту в любом месте)
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static Resources res;
    //

    public static final String PLAYERS = "players"; // Название ключа в Shared Pref с игроками

    public static Gson json = new Gson();
    public static SharedPreferences players;
    public static Set<String> playersJson = new HashSet<>(); // Сет игроков в Json формате
    public static Set<Player> playersJava = new HashSet<>(); // Сет игроков в Java формате

    public static String name = ""; // player name
    public static int score = 0; // player score
    public static int help = 3; // help counter
    public static boolean inProcess = false; // Флаг состояния
    public static boolean updateLeaderboard = false; // Флаг для обновления лидербоарда
    public static boolean online = false; // Флаг режима (онлайн/оффлайн)

    public static Map<Integer, Boolean> isChecked = new HashMap<>(); // map (номер вопроса | чекнут ли)
    public static Map<Integer, Integer> choose = new HashMap<>(); // map (номер вопроса | какой варик выбрал игрок)

    static Player newPlayer; // переменная для создания нового игрока
    static Player existsPlayer; // переменная, куда будет занесен игрок с таким же именем (если есть)
    static boolean exists; // флаг, показывающий, был ли игрок с таким же именем

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

        // if not connect -> read players from local shared pref
        if (!online) {
            playersJson = players.getStringSet(PLAYERS, new HashSet<>());
            for (String playerJson : playersJson) {
                playersJava.add(json.fromJson(playerJson, Player.class));
            }
        }
        //

        updateMaps();
    }

    public static Context context() { return mContext; }

    public static Resources res() { return res; }

    public static void updateMaps() {
        // обнуление мапов (очистка до сток состояния)
        for (int i = 0; i < Question.questions.length; i++) {
            isChecked.put(i, false);
            choose.put(i, -1);
        }
    }

    public static void startGame(String playerName) {
        name = playerName;
        score = 0;
        inProcess = true;
    }

    public static void resetScore() { score = 0; }

    public static void calcScore(boolean isCorrect) { if (inProcess && isCorrect) score += 10; }

    public static void localSave() {
        // save players data to shared pref
        SharedPreferences.Editor editor = players.edit();
        editor.remove(PLAYERS);
        editor.apply();
        editor.putStringSet(PLAYERS, playersJson);
        editor.apply();
    }

    public static void check() {
        // check state in end of game
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

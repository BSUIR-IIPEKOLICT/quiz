package com.example.quiz.controller;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.example.quiz.model.Player;
import com.google.gson.Gson;

public class Quiz extends Application {

    private static final String PLAYERS = "players";

    public static Gson gson = new Gson(); // Утилитка для работы с Json
    public static SharedPreferences storage; // Переменная, в которую будут загружены сохраненные преференсы
    public static Set<Player> playersJava = new HashSet<>(); // Сет игроков в Java формате
    public static Set<String> playersJson = new HashSet<>(); // Сет игроков в Json формате

    public static String name = ""; // player name
    public static int score = 0; // player score
    public static int help = 3; // help counter
    public static boolean inProcess = false; // Флаг состояния
    public static boolean updateLeaderboard = false; // Флаг для обновления лидербоарда

    public static Map<Integer, Boolean> isChecked = new HashMap<>(); // map (номер вопроса | чекнут ли)
    public static Map<Integer, Integer> choose = new HashMap<>(); // map (номер вопроса | какой варик выбрал игрок)

    static Player newPlayer; // переменная для создания нового игрока
    static Player existsPlayer; // переменная, куда будет занесен игрок с таким же именем (если есть)
    static boolean exists; // флаг, показывающий, был ли игрок с таким же именем

    @Override
    public void onCreate() {
        super.onCreate();

        storage = getSharedPreferences(PLAYERS, Context.MODE_PRIVATE); // загрузка сохраненных преференсов
        loadPlayers();
        updateLeaderboard = true; // флаг обновления рейтинга -> true
        updateMaps(); // обнуляем мапы
    }

    public static void loadPlayers() {
        playersJson = storage.getStringSet(PLAYERS, new HashSet<>()); // загрузка игроков в JSON формате

        for (String playerJson : playersJson) { // пробегаем циклом по каждому игроку
            playersJava.add(gson.fromJson(playerJson, Player.class));
                // преобразуем в JAVA формат и добавляем в сет playersJava
        }
    }

    public static void updateMaps() {
        // обнуление мапов (очистка до сток состояния)
        for (int i = 0; i < Question.questions.length; i++) {
            isChecked.put(i, false);
            choose.put(i, -1);
        }
    }

    public static void startGame(String playerName) {
        // гачало игры
        name = playerName; // установить имя игрока на введенное пользователем
        score = 0; // установить число очков в 0
        inProcess = true; // установить флаг процесса
    }

    public static void resetScore() { score = 0; } // метод для обнуления очков

    public static void calcScore(boolean isCorrect) { if (inProcess && isCorrect) score += 10; }
        // метод, который считает, скольк игрок заработал за вопрос очков (0 или 10)

    public static void check() {
        // check state in end of game
        newPlayer = new Player(name, score); // создаем обьект нового игрока
        existsPlayer = null;
        exists = false;

        if (!playersJava.contains(newPlayer)) { // если в сете нет точно такого же нового игрока
            for (Player p : playersJava) { // перебираем каждый обьект сета
                if (p.name.equals(newPlayer.name)) { // и сравниваем его имя с именем нового игрока
                    exists = true; // если совплали - устанавить exists в true
                    if (p.score < newPlayer.score) existsPlayer = p; // и если число очков уже
                        // имевшегося игрока меньше нового - присвоить existsPlayer имевшегося игрока
                }
            }
            if (!exists) save(newPlayer); // сохраняем нового игрока, если нет с таким же именем
            else if (exists && existsPlayer != null) {
                // если есть с таким же именем, но меньшим количеством очков - заменяем новым
                playersJava.remove(existsPlayer);
                playersJson.remove(gson.toJson(existsPlayer, Player.class));
                save(newPlayer);
            }
        }

        inProcess = false; // сброс флага процесса
    }

    private static void save(Player player) {
        // сохранение игрока в рейтинге
        playersJava.add(player); // добавить в сет игроков
        playersJson.add(gson.toJson(player, Player.class));
        localSave();
        updateLeaderboard = true; // надо обновить рейтинг
    }

    private static void localSave() { // сохранение в преференсы инфы об игроках
        SharedPreferences.Editor editor = storage.edit();
        editor.remove(PLAYERS);
        editor.apply();
        editor.putStringSet(PLAYERS, playersJson);
        editor.apply();
    }

    public static void finishLeaderboardUpdate() { updateLeaderboard = false; }
        // метод, завешающий обновление рейтинга
}

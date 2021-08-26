package com.example.quiz.controller;

import android.app.Application;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.mongodb.Credentials;
import com.example.quiz.model.Player;
import com.example.quiz.model.Database;

public class Quiz extends Application {

    public static Set<Player> players = new HashSet<>(); // Сет игроков в Java формате

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

        // Connect to Mongo
        Realm.init(this); // старт работы realm
        Database.app.loginAsync(Credentials.anonymous(), result -> { // попытка подключиться к бд
            if (result.isSuccess()) { // если получилось
                online = true; // флаг онлайна -> активен
                Database.init(); // получаем данные из бд
                players = Database.getPlayers();
            }
        });
        //

        updateLeaderboard = true; // флаг обновления рейтинга -> true
        updateMaps(); // обнуляем мапы
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

        if (!players.contains(newPlayer)) { // если в сете нет точно такого же нового игрока
            for (Player p : players) { // перебираем каждый обьект сета
                if (p.name.equals(newPlayer.name)) { // и сравниваем его имя с именем нового игрока
                    exists = true; // если совплали - устанавить exists в true
                    if (p.score < newPlayer.score) existsPlayer = p; // и если число очков уже
                        // имевшегося игрока меньше нового - присвоить existsPlayer имевшегося игрока
                }
            }
            if (!exists) save(newPlayer); // сохраняем нового игрока, если нет с таким же именем
            else if (exists && existsPlayer != null) {
                // если есть с таким же именем, но меньшим количеством очков - заменяем новым
                players.remove(existsPlayer);
                if (online) Database.removePlayer(existsPlayer);
                save(newPlayer);
            }
        }

        inProcess = false; // сброс флага процесса
    }

    private static void save(Player player) {
        // сохранение игрока в рейтинге
        players.add(player); // добавить в сет игроков
        if (online) Database.addPlayer(player); // сохранить в бд
        updateLeaderboard = true; // надо обновить рейтинг
    }

    public static void finishLeaderboardUpdate() { updateLeaderboard = false; }
        // метод, завешающий обновление рейтинга
}

package com.example.quiz.model;

import org.bson.Document;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class Database {

    private static final String appId = "quiz-sjhgv"; // app id из mongodb realm app
    private static final String service = "mongodb-atlas";
    private static final String database = "Main"; // название базы данных из mongodb realm app
    private static final String playerCol = "Player"; // название коллкции из database

    public static App app = new App(new AppConfiguration.Builder(appId).build());
        // создаем конфиг приложения realm
    public static User user; // пользователь realm

    static MongoClient client;
    static MongoDatabase data;
    static MongoCollection<Document> playersData;

    public static void init() { // метод, который сработает при успешном подключении к бд
        user = app.currentUser(); // установить пользователя
        client = Objects.requireNonNull(user).getMongoClient(service); // полчить монго-клиент
        data = client.getDatabase(database); // получить оттуда бд
        playersData = data.getCollection(playerCol); // и нужную коллекцию
    }

    public static Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();

        // get players collection from main database
        playersData.find().iterator().getAsync(result -> {
            // пытаемся получить всю коллекцию из базы данных
            if (result.isSuccess()) { // если получилось
                MongoCursor<Document> collection = result.get(); // устанавливаем курсор

                while (collection.hasNext()) { // перебираем им все элементы коллекции
                    Document cur = collection.next();
                    players.add(new Player( // каждый элемент преобразуем в обьект класса Player и записываем в сет
                        cur.getString("name"),
                        cur.getInteger("score"),
                        cur.getObjectId("_id")
                    ));
                }
            }
        });

        return players; // возвращаем итоговый сет
    }

    public static void addPlayer(Player player) { // метод для добавления нового игрока в бд
        playersData.insertOne(new Document()
            .append("name", player.name)
            .append("score", player.score)
            .append("_id", player._id)
        ).getAsync(result -> {});
    }

    public static void removePlayer(Player player) { // метод для удаления игрока из бд по его id
        playersData.deleteOne(new Document().append("_id", player._id)).getAsync(result -> {});
    }
}

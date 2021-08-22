package loshica.quiz;

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

    private static final String appId = "quiz-sjhgv";
    private static final String service = "mongodb-atlas";
    private static final String database = "Main";
    private static final String playerCol = "Player";
    private static final String questionCol = "Question";

    public static App app = new App(new AppConfiguration.Builder(appId).build());
    public static User user;

    static MongoClient client;
    static MongoDatabase data;
    static MongoCollection<Document> playersData;
    static MongoCollection<Document> questionsData;

    public static void init() {
        user = app.currentUser();
        client = Objects.requireNonNull(user).getMongoClient(service);
        data = client.getDatabase(database);
        playersData = data.getCollection(playerCol);
        questionsData = data.getCollection(questionCol);
    }

    public static Set<Player> getPlayers() {
        Set<Player> players = new HashSet<>();

        playersData.find().iterator().getAsync(result -> {
            if (result.isSuccess()) {
                MongoCursor<Document> collection = result.get();

                while (collection.hasNext()) {
                    Document cur = collection.next();
                    players.add(new Player(
                        cur.getString("name"),
                        cur.getInteger("score"),
                        cur.getObjectId("_id")
                    ));
                }
            }
        });

        return players;
    }

    public static void addPlayer(Player player) {
        playersData.insertOne(new Document()
            .append("name", player.name)
            .append("score", player.score)
            .append("_id", player._id)
        ).getAsync(result -> {});
    }

    public static void removePlayer(Player player) {
        playersData.deleteOne(new Document().append("_id", player._id)).getAsync(result -> {});
    }
}

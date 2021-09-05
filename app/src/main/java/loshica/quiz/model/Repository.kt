package loshica.quiz.model

import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import io.realm.mongodb.mongo.iterable.MongoCursor
import org.bson.Document
import java.util.*

object Repository {

    private const val appId = "quiz-sjhgv"
    private const val service = "mongodb-atlas"
    private const val database = "Main"
    private const val playerCol = "Player"

    var app = App(AppConfiguration.Builder(appId).build())

    private var user: User? = null
    private var client: MongoClient? = null
    private var data: MongoDatabase? = null
    private var playersData: MongoCollection<Document>? = null

    fun init() {
        user = app.currentUser()
        client = user?.getMongoClient(service)
        data = client?.getDatabase(database)
        playersData = data?.getCollection(playerCol)
    }

    val players: MutableSet<Player>
        get() {
            val players: MutableSet<Player> = HashSet()
            playersData!!.find().iterator().getAsync { result: App.Result<MongoCursor<Document>> ->
                if (result.isSuccess) {
                    val collection = result.get()
                    while (collection.hasNext()) {
                        val cur = collection.next()
                        players.add(Player.fromDb(cur))
                    }
                }
            }
            return players
        }

    fun addPlayer(player: Player) {
        playersData!!.insertOne(Player.fromKotlin(player)).getAsync { }
    }

    fun removePlayer(player: Player) {
        playersData!!.deleteOne(Document().append("_id", player._id)).getAsync { }
    }
}
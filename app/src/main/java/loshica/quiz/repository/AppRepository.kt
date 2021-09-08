package loshica.quiz.repository

import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import io.realm.mongodb.mongo.MongoClient
import io.realm.mongodb.mongo.MongoCollection
import io.realm.mongodb.mongo.MongoDatabase
import loshica.vendor.LOSApp
import org.bson.Document

class AppRepository : LOSApp() {

    private lateinit var realm: App

    private var user: User? = null
    private var client: MongoClient? = null
    private var data: MongoDatabase? = null

    override fun onCreate() {
        super.onCreate()

        if (isOnline(this)) {
            // Connect to Mongo
            Realm.init(this)
            realm = App(AppConfiguration.Builder(appId).build())
            realm.loginAsync(Credentials.anonymous()) { result: App.Result<User?> ->
                if (result.isSuccess) {
                    init()
                    get()
                }
            }
            //
        }
    }

    companion object {
        private const val appId = "quiz-sjhgv"
        private const val service = "mongodb-atlas"
        private const val database = "Main"
    }

    object Collection {
        const val player = "Player"
    }

    object Data {
        var playersData: MongoCollection<Document>? = null
    }

    private fun init() {
        user = realm.currentUser()
        client = user?.getMongoClient(service)
        data = client?.getDatabase(database)
    }

    fun get() {
        Data.playersData = data?.getCollection(Collection.player)
    }
}
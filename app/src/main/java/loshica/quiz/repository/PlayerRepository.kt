package loshica.quiz.repository

import io.realm.mongodb.App
import io.realm.mongodb.mongo.iterable.MongoCursor
import loshica.quiz.model.Player
import loshica.vendor.LOSApp
import org.bson.Document

object PlayerRepository {

    private val collection = AppRepository.Data.playersData

    val data: MutableSet<Player>?
        get() {
            return if (collection != null) {
                val players: MutableSet<Player> = HashSet()
                collection.find().iterator().getAsync { result: App.Result<MongoCursor<Document>> ->
                    if (result.isSuccess) {
                        val collection = result.get()
                        while (collection.hasNext()) {
                            val cur = collection.next()
                            players.add(Player.convert(cur))
                        }
                    }
                }
                players
            } else null
        }

    fun addPlayer(player: Player) {
        collection!!.insertOne(Player.convert(player)).getAsync { }
    }

    fun removePlayer(player: Player) {
        collection!!.deleteOne(Document().append(LOSApp.MONGO_ID, player._id)).getAsync { }
    }
}
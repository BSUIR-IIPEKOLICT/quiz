package loshica.quiz.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import loshica.quiz.model.Repository
import loshica.quiz.model.Player
import java.util.HashSet

class StorageModel(val app: Application) : AndroidViewModel(app) {

    private val localPlayers: String = "players"
    private var json = Gson()
    private var playersStorage: SharedPreferences = app.getSharedPreferences(localPlayers, Context.MODE_PRIVATE)
    private var playersJson: MutableSet<String>? = HashSet()
    private var playersJava: MutableSet<Player>? = HashSet()

    private val isOnline: Boolean
        get() {
            val cm = app.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
            val cp = cm.getNetworkCapabilities(cm.activeNetwork)

            return cp != null && (
                cp.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                cp.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                cp.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            )
        }

    private val players = MutableLiveData<MutableSet<Player>>()

    init {
        if (isOnline) {
            // Connect to Mongo
            Realm.init(app.applicationContext)
            Repository.app.loginAsync(Credentials.anonymous()) { result: App.Result<User?> ->
                if (result.isSuccess) {
                    Repository.init()
                    playersJava = Repository.players
                }
            }
            //
        }

        load()
    }

    fun load() {
        if (isOnline) {
            for (playerJava in playersJava!!) {
                playersJson!!.add(json.toJson(playerJava, Player::class.java))
            }

            localSave()
        } else {
            playersJson = playersStorage.getStringSet(localPlayers, HashSet())

            for (playerJson in playersJson!!) {
                playersJava?.add(json.fromJson(playerJson, Player::class.java))
            }
        }

        players.value = playersJava!!
    }

    fun getPlayers(): MutableSet<Player> = players.value!!

    private fun localSave() {
        val editor = playersStorage.edit()
        editor.remove(localPlayers)
        editor.apply()
        editor.putStringSet(localPlayers, playersJson!!)
        editor.apply()
    }

    fun check(player: Player) {
        var existsPlayer: Player? = null
        var exists = false

        if (!playersJava?.contains(player)!!) {
            for (p in playersJava!!) {
                if (p.name == player.name) {
                    exists = true
                    if (p.score < player.score) existsPlayer = p
                }
            }
            if (!exists) save(player) else if (exists && existsPlayer != null) {
                playersJava?.remove(existsPlayer)
                playersJson!!.remove(json.toJson(existsPlayer, Player::class.java))
                if (isOnline) Repository.removePlayer(existsPlayer)
                save(player)
            }
        }
    }

    private fun save(player: Player) {
        playersJava?.add(player)
        if (isOnline) Repository.addPlayer(player)
        playersJson!!.add(json.toJson(player, Player::class.java))
        localSave()
    }

    fun check(size: Int): Boolean = players.value!!.size == size
}
package loshica.quiz.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.ConnectivityManager
import com.google.gson.Gson
import io.realm.Realm
import io.realm.mongodb.App
import io.realm.mongodb.Credentials
import io.realm.mongodb.User
import loshica.quiz.model.Database
import loshica.quiz.model.Database.addPlayer
import loshica.quiz.model.Database.init
import loshica.quiz.model.Database.removePlayer
import loshica.quiz.model.Player
import java.util.*

class AppState : Application() {

    override fun onCreate() {
        super.onCreate()
        mContext = this
        res = resources
        players = getSharedPreferences(PLAYERS, MODE_PRIVATE)

        // Connect to Mongo
        Realm.init(this)
        Database.app.loginAsync(Credentials.anonymous()) { result: App.Result<User?> ->
            if (result.isSuccess) {
                init()
                playersJava = Database.players
            }
        }
        //
        online = isOnline
        updateLeaderboard = true
        updateMaps()
    }

    private val isOnline: Boolean
        get() {
            val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
        }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var mContext: Context? = null
        private var res: Resources? = null

        const val PLAYERS = "players"
        private var json = Gson()
        var players: SharedPreferences? = null
        private var playersJson: MutableSet<String>? = HashSet()
        var playersJava: MutableSet<Player>? = HashSet()

        var name = ""
        var score = 0
        var help = 3

        var inProcess = false
        var updateLeaderboard = false
        var online = false

        var isChecked: MutableMap<Int, Boolean> = HashMap()
        var choose: MutableMap<Int, Int> = HashMap()

        private var newPlayer: Player? = null
        private var existsPlayer: Player? = null
        private var exists = false

        fun context(): Context? = mContext

        fun res(): Resources? = res

        fun loadPlayers() {
            if (online && playersJson!!.size == 0) {
                for (playerJava in playersJava!!) {
                    playersJson!!.add(json.toJson(playerJava, Player::class.java))
                }
                localSave()
            } else if (!online && playersJava?.isEmpty()!!) {
                playersJson = players!!.getStringSet(PLAYERS, HashSet())
                for (playerJson in playersJson!!) {
                    playersJava?.add(json.fromJson(playerJson, Player::class.java))
                }
            }
        }

        fun startGame(playerName: String) {
            name = playerName
            score = 0
            inProcess = true
        }

        fun updateMaps() {
            for (i in Question.questions.indices) {
                isChecked[i] = false
                choose[i] = -1
            }
        }

        fun resetScore() { score = 0 }

        fun calcScore(isCorrect: Boolean) { if (inProcess && isCorrect) score += 10 }

        private fun localSave() {
            val editor = players!!.edit()
            editor.remove(PLAYERS)
            editor.apply()
            editor.putStringSet(PLAYERS, playersJson)
            editor.apply()
        }

        fun check() {
            newPlayer = Player(name, score)
            existsPlayer = null
            exists = false

            if (!playersJava?.contains(newPlayer)!!) {
                for (p in playersJava!!) {
                    if (p.name == newPlayer!!.name) {
                        exists = true
                        if (p.score < newPlayer!!.score) existsPlayer = p
                    }
                }
                if (!exists) save(newPlayer!!) else if (exists && existsPlayer != null) {
                    playersJava?.remove(existsPlayer)
                    playersJson!!.remove(json.toJson(existsPlayer, Player::class.java))
                    if (online) removePlayer(existsPlayer!!)
                    save(newPlayer!!)
                }
            }
            inProcess = false
        }

        private fun save(player: Player) {
            playersJava?.add(player)
            if (online) addPlayer(player)
            playersJson!!.add(json.toJson(player, Player::class.java))
            localSave()
            updateLeaderboard = true
        }

        fun finishLeaderboardUpdate() { updateLeaderboard = false }
    }
}

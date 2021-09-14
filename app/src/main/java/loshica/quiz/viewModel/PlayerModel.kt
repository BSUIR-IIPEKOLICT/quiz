package loshica.quiz.viewModel

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import loshica.quiz.repository.PlayerRepository
import loshica.quiz.model.Player
import loshica.vendor.LOSApp
import java.util.*

class PlayerModel(val app: Application) : AndroidViewModel(app) {

    private val storageName: String = "players"
    private var storage: SharedPreferences = app.getSharedPreferences(storageName, Context.MODE_PRIVATE)
    private var json: MutableSet<String>? = HashSet()
    private var java: MutableSet<Player>? = HashSet()

    private var isOnline: Boolean = false
    private var timer: CountDownTimer? = null

    val set = MutableLiveData<MutableSet<Player>>()

    init {
        java = PlayerRepository.data
        isOnline = LOSApp.isOnline(app)
        set.value = HashSet()

        timer = object : CountDownTimer(10000, 1000) {
            override fun onTick(v: Long) { load() }
            override fun onFinish() {}
        }
    }

    fun preload() { timer?.start() }

    fun load() {
        if (isOnline) {
            for (playerJava in java!!) {
                json?.add(LOSApp.json.toJson(playerJava, Player::class.java))
            }

            json?.let { localSave() }
        } else {
            json = storage.getStringSet(storageName, HashSet())

            for (playerJson in json!!) {
                java?.add(LOSApp.json.fromJson(playerJson, Player::class.java))
            }
        }

        java?.let { set.value = it }
    }

    private fun localSave() {
        val editor = storage.edit()
        editor.remove(storageName)
        editor.apply()
        editor.putStringSet(storageName, json!!)
        editor.apply()
    }

    fun check(player: Player) {
        var existsPlayer: Player? = null
        var exists = false

        for (p in set.value!!) {
            if (p.name == player.name) {
                exists = true
                if (p.score < player.score) existsPlayer = p
            }
        }

        if (!exists) save(player) else if (existsPlayer != null) {
            set.value?.remove(existsPlayer)
            java?.remove(existsPlayer)
            json?.remove(LOSApp.json.toJson(existsPlayer, Player::class.java))
            if (isOnline) PlayerRepository.removePlayer(existsPlayer)
            save(player)
        }
    }

    private fun save(player: Player) {
        set.value?.add(player)
        java?.add(player)
        if (isOnline) PlayerRepository.addPlayer(player)
        json?.add(LOSApp.json.toJson(player, Player::class.java))
        localSave()
    }
}
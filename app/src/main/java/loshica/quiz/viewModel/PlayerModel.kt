package loshica.quiz.viewModel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import loshica.quiz.repository.PlayerRepository
import loshica.quiz.model.Player
import loshica.vendor.LOSApp
import java.util.*

class PlayerModel(val app: Application) : AndroidViewModel(app) {

    private var players: MutableSet<Player>? = HashSet()

    private var isOnline: Boolean = false
    private var timer: CountDownTimer? = null

    val set = MutableLiveData<MutableSet<Player>>()

    init {
        players = PlayerRepository.data
        isOnline = LOSApp.isOnline(app)
        set.value = HashSet()

        timer = object : CountDownTimer(15000, 1000) {
            override fun onTick(v: Long) { load() }
            override fun onFinish() {}
        }
    }

    fun preload() { timer?.start() }

    fun load() { players?.let { set.value = it } }

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
            players?.remove(existsPlayer)
            if (isOnline) PlayerRepository.removePlayer(existsPlayer)
            save(player)
        }
    }

    private fun save(player: Player) {
        set.value?.add(player)
        players?.add(player)
        if (isOnline) PlayerRepository.addPlayer(player)
    }
}
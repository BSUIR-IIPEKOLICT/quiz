package loshica.quiz.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import loshica.quiz.databinding.ItemPlayerBinding
import loshica.quiz.model.Player

class LeaderboardAdapter() : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    private var players: MutableList<Player> = mutableListOf()
    private lateinit var b: ItemPlayerBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        b = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with (holder.b) {
            playerName.text = players[position].name
            playerScore.text = players[position].score.toString()
        }
    }

    override fun getItemCount(): Int = players.size

    class ViewHolder internal constructor(binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val b = binding
    }

    private fun convert(playersSet: Set<Player>?): MutableList<Player> {
        val list = mutableListOf<Player>()
        if (playersSet != null) list.addAll(0, playersSet)
        list.sortWith { o1: Player, o2: Player -> o2.score - o1.score }
        return list
    }

    fun update(playersSet: Set<Player>?) {
        this.players.clear()
        this.players = convert(playersSet)
        notifyDataSetChanged()
    }
}
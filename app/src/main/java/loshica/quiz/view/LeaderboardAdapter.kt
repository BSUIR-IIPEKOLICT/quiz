package loshica.quiz.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import loshica.quiz.databinding.ItemPlayerBinding
import loshica.quiz.model.Player

class LeaderboardAdapter internal constructor(playersSet: Set<Player>?) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    private val players: MutableList<Player>
    private lateinit var b: ItemPlayerBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        b = ItemPlayerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val player = players[position]
        holder.b.playerName.text = player.name
        holder.b.playerScore.text = player.score.toString()
    }

    override fun getItemCount(): Int = players.size

    class ViewHolder internal constructor(binding: ItemPlayerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val b = binding
    }

    init {
        val list = mutableListOf<Player>()
        if (playersSet != null) list.addAll(0, playersSet)
        list.sortWith { o1: Player, o2: Player -> o2.score - o1.score }
        this.players = list
    }
}
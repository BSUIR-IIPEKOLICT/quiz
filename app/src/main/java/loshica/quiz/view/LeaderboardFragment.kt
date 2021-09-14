package loshica.quiz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import loshica.quiz.databinding.FragmentLeaderboardBinding
import loshica.quiz.model.Player
import loshica.quiz.viewModel.PlayerModel

class LeaderboardFragment : Fragment() {

    private var _b: FragmentLeaderboardBinding? = null
    private val b get() = _b!!
    private lateinit var la: LeaderboardAdapter

    private lateinit var playerSetObserver: Observer<MutableSet<Player>>

    private val player: PlayerModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentLeaderboardBinding.inflate(inflater, container, false)
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        la = LeaderboardAdapter()
        b.recyclerView.adapter = la
        player.preload()

        playerSetObserver = Observer {
            la.update(it)
        }

        return b.root
    }

    override fun onStart() {
        super.onStart()
        player.set.observe(this, playerSetObserver)
    }

    override fun onStop() {
        super.onStop()
        player.set.removeObserver(playerSetObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
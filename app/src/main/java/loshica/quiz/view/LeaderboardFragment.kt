package loshica.quiz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import loshica.quiz.databinding.FragmentLeaderboardBinding
import loshica.quiz.viewModel.AppState

class LeaderboardFragment : Fragment() {

    private var _b: FragmentLeaderboardBinding? = null
    private val b get() = _b!!
    private lateinit var la: LeaderboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentLeaderboardBinding.inflate(inflater, container, false)
        b.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        la = LeaderboardAdapter(AppState.playersJava)
        b.recyclerView.adapter = la
        return b.root
    }

    override fun onResume() {
        super.onResume()

        AppState.loadPlayers()
        if (la.itemCount != AppState.playersJava?.size) requireActivity().recreate()
        if (AppState.updateLeaderboard) {
            la.notifyDataSetChanged()
            AppState.finishLeaderboardUpdate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
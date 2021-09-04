package loshica.quiz.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return if (position < 1) MainFragment() else LeaderboardFragment()
    }

    override fun getItemCount(): Int = 2
}

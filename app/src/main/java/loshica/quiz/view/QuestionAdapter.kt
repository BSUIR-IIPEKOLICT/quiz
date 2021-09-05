package loshica.quiz.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import loshica.quiz.model.Question

class QuestionAdapter(private val questions: Array<Question>, fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return if (position < itemCount - 1) QuestionFragment.newInstance(questions[position]) else FinishFragment()
    }

    override fun getItemCount(): Int = questions.size + 1
}
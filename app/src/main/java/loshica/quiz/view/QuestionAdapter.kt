package loshica.quiz.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import loshica.quiz.viewModel.Question

class QuestionAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return if (position < itemCount - 1)
            QuestionFragment.newInstance(Question.questions[position], position)
        else FinishFragment()
    }

    override fun getItemCount(): Int = Question.questions.size + 1
}
package loshica.quiz.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import loshica.quiz.model.Question;

public class QuestionAdapter extends FragmentStateAdapter {

    public QuestionAdapter(@NonNull FragmentActivity fa) { super(fa); }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position < getItemCount() - 1) ?
            QuestionFragment.newInstance(Question.questions[position], position) : new FinishFragment();
    }

    @Override
    public int getItemCount() { return Question.questions.length + 1; }
}

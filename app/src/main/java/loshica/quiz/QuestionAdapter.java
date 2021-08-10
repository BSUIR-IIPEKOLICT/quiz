package loshica.quiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

//public class QuestionAdapter extends FragmentPagerAdapter {
//
//    public QuestionAdapter(@NonNull FragmentManager fm) {
//        super(fm);
//    }
//
//    @NonNull
//    @Override
//    public Fragment getItem(int position) {
//        return (position < getCount() - 1) ?
//            QuestionFragment.newInstance(Question.questions[position]) : new FinishFragment();
//    }
//
//    @Override
//    public int getCount() { return Question.questions.length + 1; }
//}

public class QuestionAdapter extends FragmentStateAdapter {

    public QuestionAdapter(@NonNull FragmentActivity fa) { super(fa); }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position < getItemCount() - 1) ?
            QuestionFragment.newInstance(Question.questions[position]) : new FinishFragment();
    }

    @Override
    public int getItemCount() { return Question.questions.length + 1; }
}

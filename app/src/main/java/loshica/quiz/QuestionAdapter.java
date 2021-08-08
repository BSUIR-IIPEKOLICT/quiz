package loshica.quiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class QuestionAdapter extends FragmentPagerAdapter {

    public QuestionAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return (position < getCount() - 1) ?
            QuestionFragment.newInstance(Question.questions[position]) : new FinishFragment();
    }

    @Override
    public int getCount() { return 2; }
}

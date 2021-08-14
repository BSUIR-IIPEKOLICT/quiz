package loshica.quiz;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainAdapter extends FragmentStateAdapter {

    public MainAdapter(@NonNull FragmentActivity fa) { super(fa); }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position < 1) ? new MainFragment() : new LeaderboardFragment();
    }

    @Override
    public int getItemCount() { return 2; }
}

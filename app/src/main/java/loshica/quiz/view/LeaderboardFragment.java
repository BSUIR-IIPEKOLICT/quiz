package loshica.quiz.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import loshica.quiz.R;
import loshica.quiz.viewModel.AppState;

public class LeaderboardFragment extends Fragment {

    RecyclerView rv;
    LeaderboardAdapter la;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        rv = root.findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        la = new LeaderboardAdapter(AppState.playersJava);
        rv.setAdapter(la);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        AppState.loadPlayers();

        if (la.getItemCount() != AppState.playersJava.size()) {
            requireActivity().recreate();
        }

        if (AppState.updateLeaderboard) {
            la.notifyDataSetChanged();
            AppState.finishLeaderboardUpdate();
        }
    }
}
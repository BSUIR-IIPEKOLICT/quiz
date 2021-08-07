package loshica.quiz;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LeaderboardFragment extends Fragment implements View.OnClickListener {

    RecyclerView rv;
    LeaderboardAdapter adapter;
    Button btn;
    LeaderboardFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        rv = (RecyclerView) root.findViewById(R.id.recyclerView);
        rv.setLayoutManager(new LinearLayoutManager(root.getContext()));
        adapter = new LeaderboardAdapter(MainActivity.usersJava);
        rv.setAdapter(adapter);

        btn = (Button) root.findViewById(R.id.leaderboard_back);
        btn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.leaderboard_back) { listener.back(); }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (LeaderboardFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e);
        }
    }

    public interface LeaderboardFragmentListener {
        void back();
        void updateLeaderboard();
    }
}
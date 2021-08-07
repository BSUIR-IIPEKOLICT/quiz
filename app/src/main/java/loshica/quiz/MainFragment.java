package loshica.quiz;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements View.OnClickListener {

    Button play;
    Button leaderboard;
    MainFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        play = (Button) root.findViewById(R.id.main_play);
        leaderboard = (Button) root.findViewById(R.id.main_leaderboard);
        play.setOnClickListener(this);
        leaderboard.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        listener.reset();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_play) {
            listener.play();
        } else {
            listener.leaderboard();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MainFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e);
        }
    }

    public interface MainFragmentListener {
        void reset();
        void leaderboard();
        void play();
    }
}
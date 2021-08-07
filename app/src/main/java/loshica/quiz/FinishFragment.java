package loshica.quiz;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FinishFragment extends Fragment implements View.OnClickListener {

    TextView tv;
    Button btn;
    FinishFragmentListener listener;

    public FinishFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finish, container, false);

        tv = (TextView) root.findViewById(R.id.finish_text);
        btn = (Button) root.findViewById(R.id.finish_back);
        btn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MainActivity.user != null) { reloadText(); }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.finish_back) {
            MainActivity.score = 0;
            listener.back();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FinishFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e);
        }
    }

    public void reloadText() {
        String text = getResources().getString(R.string.finish_start_1) + " " + MainActivity.user.getName()
            + getResources().getString(R.string.finish_end_1) + "\n" +
            getResources().getString(R.string.finish_start_2) + " " + MainActivity.user.getScore()
            + " " + getResources().getString(R.string.finish_end_2);
        tv.setText(text);
    }

    public interface FinishFragmentListener {
        void back();
        void save();
    }
}
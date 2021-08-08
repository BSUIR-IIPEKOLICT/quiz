package loshica.quiz;

import android.annotation.SuppressLint;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finish, container, false);

        tv = (TextView) root.findViewById(R.id.finish_text);
        btn = (Button) root.findViewById(R.id.finish_back);
        btn.setOnClickListener(this);
        tv.setText(
            getResources().getString(R.string.finish_start_1) + " " + Data.user.name
            + getResources().getString(R.string.finish_end_1) + "\n" +
            getResources().getString(R.string.finish_start_2) + " " +
            Integer.toString(Data.score) + " " +
            getResources().getString(R.string.finish_end_2)
        );

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.finish_back) {
            listener.save();
            listener.finish();
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

    public interface FinishFragmentListener {
        void save();
        void finish();
        void back();
    }
}
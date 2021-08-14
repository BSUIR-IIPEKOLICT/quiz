package loshica.quiz;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainFragment extends Fragment implements View.OnClickListener {

    Button btn;
    NameDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        btn = root.findViewById(R.id.main_play);
        btn.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_play) {
            dialog = new NameDialog();
            dialog.show(requireActivity().getSupportFragmentManager(), null);
        }
    }
}
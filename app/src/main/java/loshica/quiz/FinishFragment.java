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
import android.widget.Toast;

import java.text.MessageFormat;

public class FinishFragment extends Fragment implements View.OnClickListener {

    TextView tv;
    Button btn;
    FinishFragmentListener listener;

    @SuppressLint({"SetTextI18n", "StringFormatInvalid"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finish, container, false);

        tv = root.findViewById(R.id.finish_text);
        btn = root.findViewById(R.id.finish_back);
        btn.setOnClickListener(this);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        String generic = MessageFormat.format(App.res().getString(R.string.finish_text), App.name,
            App.score);
        String textDefault = App.res().getString(R.string.finish_default_text);
        tv.setText((!App.name.equals("")) ? generic : textDefault);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.finish_back) { listener.finish(); }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try { listener = (FinishFragmentListener) context; }
        catch (ClassCastException e) { throw new ClassCastException(context.toString() + e); }
    }

    public interface FinishFragmentListener {
        void finish();
    }
}
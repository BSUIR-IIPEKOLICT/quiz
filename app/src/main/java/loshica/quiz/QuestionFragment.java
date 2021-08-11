package loshica.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_STRINGS = "strings";
    private static final String ARG_IMG = "img";
    private static final String ARG_NUMBERS = "numbers";

    private String[] strings;
    private int img;
    private int[] numbers;

    ImageView iv;
    TextView tv;
    RadioButton rb;
    RadioGroup rg;

    private int choose;
    public Boolean isChecked = false;
    QuestionFragmentListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param q Question object with params.
     * @return A new instance of fragment Question.
     */
    public static QuestionFragment newInstance(Question q) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        // TODO: My question obj parser
        args.putStringArray(ARG_STRINGS, q.strings);
        args.putInt(ARG_IMG, q.img);
        args.putIntArray(ARG_NUMBERS, q.numbers);
        //
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strings = getArguments().getStringArray(ARG_STRINGS);
            img = getArguments().getInt(ARG_IMG);
            numbers = getArguments().getIntArray(ARG_NUMBERS);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question, container, false);

        iv = root.findViewById(R.id.question_iv);
        tv = root.findViewById(R.id.question_tv);
        rg = root.findViewById(R.id.question_rg);

        iv.setImageResource(img);
        tv.setText(strings[0]);
        for (int i = 0; i < rg.getChildCount(); i++) {
            rb = (RadioButton) rg.getChildAt(i);
            rb.setText(strings[i + 1]);
            rb.setOnClickListener(this);
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!App.inProcess) { radioOff(); }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (!isChecked && App.inProcess) {
            for (int i = 0; i < rg.getChildCount(); i++) {
                if (v == rg.getChildAt(i)) {
                    choose = i;
                    isChecked = true;
                }
            }
            radioOff();
            listener.next(isCorrect(numbers[0], choose));
        } else {
            listener.next(false);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (QuestionFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e);
        }
    }

    public interface QuestionFragmentListener {
        void next(boolean isCorrect);
    }

    private boolean isCorrect(int right, int choose) { return right == choose; }

    private void radioOff() {
        for (int i = 0; i < rg.getChildCount(); i++) {
            rg.getChildAt(i).setClickable(false);
            rg.getChildAt(i).setAlpha((float) 0.3);
        }
    }
}
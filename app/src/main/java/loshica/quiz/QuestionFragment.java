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

public class QuestionFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMG = "img";
    private static final String ARG_TEXT = "text";
    private static final String ARG_BTN0 = "btn0";
    private static final String ARG_BTN1 = "btn1";
    private static final String ARG_BTN2 = "btn2";
    private static final String ARG_BTN3 = "btn3";

    private static final String ARG_RIGHT = "right";

    // TODO: Rename and change types of parameters
    private int img;
    private String text;
    private String btn0;
    private String btn1;
    private String btn2;
    private String btn3;

    private int right;

    ImageView iv;
    TextView tv;
    RadioButton rb0;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    RadioGroup rg;

    private int choose;
    public Boolean isChecked = false;
    QuestionFragmentListener listener;

    public QuestionFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param q string array of question and answers.
     * @return A new instance of fragment Question.
     */
    // TODO: Rename and change types and number of parameters
    public static QuestionFragment newInstance(Question q) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMG, q.img);
        args.putString(ARG_TEXT, q.text);
        args.putString(ARG_BTN0, q.btn0);
        args.putString(ARG_BTN1, q.btn1);
        args.putString(ARG_BTN2, q.btn2);
        args.putString(ARG_BTN3, q.btn3);

        args.putInt(ARG_RIGHT, q.right);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            img = getArguments().getInt(ARG_IMG);
            text = getArguments().getString(ARG_TEXT);
            btn0 = getArguments().getString(ARG_BTN0);
            btn1 = getArguments().getString(ARG_BTN1);
            btn2 = getArguments().getString(ARG_BTN2);
            btn3 = getArguments().getString(ARG_BTN3);

            right = getArguments().getInt(ARG_RIGHT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question, container, false);

        iv = (ImageView) root.findViewById(R.id.question_iv);
        tv = (TextView) root.findViewById(R.id.question_tv);
        rb0 = (RadioButton) root.findViewById(R.id.question_rb0);
        rb1 = (RadioButton) root.findViewById(R.id.question_rb1);
        rb2 = (RadioButton) root.findViewById(R.id.question_rb2);
        rb3 = (RadioButton) root.findViewById(R.id.question_rb3);
        rg = (RadioGroup) root.findViewById(R.id.question_rg);

        switch (img) {
            case 0:
                iv.setImageResource(R.drawable.omon);
                break;
            default:
                iv.setImageResource(R.drawable.about_logo_light);
                break;
        }

        tv.setText(text);
        rb0.setText(btn0);
        rb1.setText(btn1);
        rb2.setText(btn2);
        rb3.setText(btn3);

        rb0.setOnClickListener(this);
        rb1.setOnClickListener(this);
        rb2.setOnClickListener(this);
        rb3.setOnClickListener(this);

        return root;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (!isChecked) {
            switch (v.getId()) {
                case R.id.question_rb0:
                    choose = 0;
                    isChecked = true;
                    break;
                case R.id.question_rb1:
                    choose = 1;
                    isChecked = true;
                    break;
                case R.id.question_rb2:
                    choose = 2;
                    isChecked = true;
                    break;
                case R.id.question_rb3:
                    choose = 3;
                    isChecked = true;
                    break;
            }
            if (isChecked) {
                for (int i = 0; i < rg.getChildCount(); i++) {
                    rg.getChildAt(i).setClickable(false);
                    rg.getChildAt(i).setAlpha((float) 0.3);
                }
            }
            if (!MainActivity.inProgress) { rg.clearCheck(); }
            listener.next(isCorrect(right, choose));
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
        void reloadQuestions();
    }

    public boolean isCorrect(int right, int choose) { return right == choose; }
}
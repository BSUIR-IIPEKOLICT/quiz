package loshica.quiz;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.Random;

public class QuestionFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_STRINGS = "strings";
    private static final String ARG_IMG = "img";
    private static final String ARG_NUMBERS = "numbers";

    private String[] strings;
    private int img;
    private int[] numbers;

    TextView timerView;
    ImageView iv;
    TextView tv;
    RadioButton rb;
    RadioGroup rg;
    Button help;

    private final float alpha = 0.3f;
    private int choose;
    private boolean isChecked = false;
    private int timerCounter;
    int random;

    CountDownTimer timer;
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

        timerView = root.findViewById(R.id.question_timer);
        iv = root.findViewById(R.id.question_iv);
        tv = root.findViewById(R.id.question_tv);
        rg = root.findViewById(R.id.question_rg);
        help = root.findViewById(R.id.question_help);

        iv.setImageResource(img);
        tv.setText(strings[0]);
        for (int i = 0; i < rg.getChildCount(); i++) {
            rb = (RadioButton) rg.getChildAt(i);
            rb.setText(strings[i + 1]);
            rb.setOnClickListener(this);
        }
        help.setOnClickListener(this);

        timerCounter = 15;
        if (!isChecked) {
            timer = new CountDownTimer(15000, 1000) {

                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    timerView.setText(Integer.toString(timerCounter--));
                }

                public void onFinish() {
                    isChecked = true;
                    listener.next(false);
                }
            }.start();
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        help.setText(MessageFormat.format(App.res().getString(R.string.question_help), App.help));

        if (!App.inProcess) {
            radioOff();
            helpOff();
        } else if (App.help == 0) helpOff();
        if (timerCounter == 0) timerView.setText("");
    }

    @Override
    public void onPause() {
        super.onPause();
        timerCounter = 0;
        timer.cancel();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (!isChecked && App.inProcess && v.getId() != R.id.question_help) {
            for (int i = 0; i < rg.getChildCount(); i++) {
                if (v == rg.getChildAt(i)) {
                    choose = i;
                    isChecked = true;
                }
            }
            radioOff();
            listener.next(isCorrect(numbers[0], choose));
        } else if (!isChecked && App.inProcess && v.getId() == R.id.question_help) {
            App.help--;
            help();
            help.setText(MessageFormat.format(App.res().getString(R.string.question_help), App.help));
            helpOff();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try { listener = (QuestionFragmentListener) context; }
        catch (ClassCastException e) { throw new ClassCastException(context.toString() + e); }
    }

    public interface QuestionFragmentListener {
        void next(boolean isCorrect);
    }

    private boolean isCorrect(int right, int choose) { return right == choose; }

    private void radioOff() {
        for (int i = 0; i < rg.getChildCount(); i++) {
            rg.getChildAt(i).setClickable(false);
            rg.getChildAt(i).setAlpha(alpha);
        }
    }

    private void help() {
        while (true) {
            random = new Random().nextInt(rg.getChildCount());
            if (random == numbers[0]) continue;
            else break;
        }
        for (int i = 0; i < rg.getChildCount(); i++) {
            if (i != numbers[0] && i != random) {
                rg.getChildAt(i).setClickable(false);
                rg.getChildAt(i).setAlpha(alpha);
            }
        }
    }

    private void helpOff() {
        help.setClickable(false);
        help.setAlpha(alpha);
    }
}
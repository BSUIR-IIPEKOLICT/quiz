package loshica.quiz.view;

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

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Random;

import loshica.quiz.R;
import loshica.quiz.controller.Coordinator;
import loshica.quiz.model.Question;

public class QuestionFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_STRINGS = "strings";
    private static final String ARG_IMG = "img";
    private static final String ARG_ID = "id";

    private String[] strings;
    private int img;
    private int right;
    private int id;

    TextView timerView;
    ImageView iv;
    TextView tv;
    RadioButton rb;
    RadioGroup rg;
    Button help;

    private final float alpha = 0.3f;
    private int timerCounter;
    int random;

    CountDownTimer timer;
    QuestionFragmentListener listener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param q Question object with params.
     * @param id Question id.
     * @return A new instance of fragment Question.
     */
    public static QuestionFragment newInstance(Question q, int id) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();

        // TODO: My question obj parser
        args.putStringArray(ARG_STRINGS, q.strings);
        args.putInt(ARG_IMG, q.img);
        //

        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strings = getArguments().getStringArray(ARG_STRINGS);
            img = getArguments().getInt(ARG_IMG);
            id = getArguments().getInt(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question, container, false);

        right = Integer.parseInt(strings[5]);

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
        if (!Objects.requireNonNull(Coordinator.isChecked.get(id))) {
            timer = new CountDownTimer(15000, 1000) {

                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    timerView.setText(Integer.toString(timerCounter--));
                }

                public void onFinish() {
                    Coordinator.isChecked.put(id, true);
                    listener.next(false);
                }
            }.start();
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        help.setText(MessageFormat.format(Coordinator.res().getString(R.string.question_help), Coordinator.help));

        if (Coordinator.help == 0) helpOff();
        if (timerCounter == 0 || Objects.requireNonNull(Coordinator.isChecked.get(id))) timerView.setText("");
        if (Objects.requireNonNull(Coordinator.isChecked.get(id)) || !Coordinator.inProcess) {
            radioOff();
            helpOff();
            check();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        timerCounter = 0;
        if (timer != null) timer.cancel();
        Coordinator.isChecked.put(id, true);
        check();
    }

    @Override
    public void onClick(View v) {
        if (!Objects.requireNonNull(Coordinator.isChecked.get(id)) && Coordinator.inProcess && v.getId() != R.id.question_help) {
            for (int i = 0; i < rg.getChildCount(); i++) {
                if (v == rg.getChildAt(i)) {
                    Coordinator.choose.put(id, i);
                    Coordinator.isChecked.put(id, true);
                }
            }
            radioOff();
            check();
            listener.next(isCorrect(right, Objects.requireNonNull(Coordinator.choose.get(id))));
        } else if (!Objects.requireNonNull(Coordinator.isChecked.get(id)) && Coordinator.inProcess && v.getId() == R.id.question_help) {
            Coordinator.help--;
            help();
            help.setText(MessageFormat.format(Coordinator.res().getString(R.string.question_help), Coordinator.help));
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

    private void check() {
        for (int i = 0; i < rg.getChildCount(); i++) {
            if (i == right) {
                rb = (RadioButton) rg.getChildAt(i);
                rb.setTextColor(requireActivity().getResources().getColor(
                    R.color.right_answer, requireActivity().getTheme()
                ));
            } else if (i == Objects.requireNonNull(Coordinator.choose.get(id))) {
                rb = (RadioButton) rg.getChildAt(i);
                rb.setTextColor(requireActivity().getResources().getColor(
                    R.color.wrong_answer, requireActivity().getTheme()
                ));
            }
        }
    }

    private void help() {
        do { random = new Random().nextInt(rg.getChildCount()); } while (random == right);
        for (int i = 0; i < rg.getChildCount(); i++) {
            if (i != right && i != random) {
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
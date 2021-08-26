package com.example.quiz.view;

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
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

import com.example.quiz.R;
import com.example.quiz.controller.Quiz;
import com.example.quiz.controller.Question;

public class QuestionFragment extends Fragment implements View.OnClickListener {

    private int right; // номер правильного варика ответа (отсчет начинается с 0)
    private int id; // номер фрагмента

    TextView timerView; // вьюшка таймера
    TextView tv;
    Button b1;
    Button b2;
    Button b3;
    Button b4;
    Button help;

    private int timerCounter; // счетчик таймера
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

        // TODO: загрузка параметров вопроса в аргументы фрагмента
        args.putString("question", q.question);
        args.putString("b1", q.btn1);
        args.putString("b2", q.btn2);
        args.putString("b3", q.btn3);
        args.putString("b4", q.btn4);
        args.putInt("right", q.right);
        //

        args.putInt("id", id); // номер вопроса в массиве questions
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: извлечение параметров вопроса из аргументов внутрь фрагмента
            right = getArguments().getInt("right");
            id = getArguments().getInt("id");
            //
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_question, container, false);

        timerView = root.findViewById(R.id.question_timer); // найти вьюшку для таймера
        tv = root.findViewById(R.id.question_tv); // найти вьюшку для текста
        b1 = root.findViewById(R.id.button1); // найти 1 кнопку
        b2 = root.findViewById(R.id.button2); // найти 2 кнопку
        b3 = root.findViewById(R.id.button3); // найти 3 кнопку
        b4 = root.findViewById(R.id.button4); // найти 4 кнопку
        help = root.findViewById(R.id.question_help); // найти кнопку для подсказки

        assert getArguments() != null;
        tv.setText(getArguments().getString("question")); // установить соответствующие текста
        b1.setText(getArguments().getString("b1"));
        b2.setText(getArguments().getString("b2"));
        b3.setText(getArguments().getString("b3"));
        b4.setText(getArguments().getString("b4"));

        b1.setOnClickListener(this); // и обработчики клика
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        help.setOnClickListener(this);

        // Timer
        timerCounter = 15; // установить счетчик таймера в 15
        if (!Objects.requireNonNull(Quiz.isChecked.get(id))) { // если вопрос не пройден
            timer = new CountDownTimer(15000, 1000) {
                // создать таймер на 15 секунд с шакгом в 1 сек

                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    // каждую секунду счетчик таймера уменьшается на 1 и записывается во вьюшку таймера
                    timerView.setText(Integer.toString(timerCounter--));
                }

                public void onFinish() { // в конце работы таймера
                    Quiz.isChecked.put(id, true); // установить, что вопрос пройден
                    listener.next(false); // вызвать метод next из активити
                }
            }.start();
        }
        //

        return root;
    }

    // Событие показа фрагмента
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();

        help.setText("50/50 (" + Quiz.help + ")"); // установить текст подсказки

        if (Quiz.help == 0) { // если счетчик подсказок упал до 0
            helpOff(); // отключить подсказку
        }

        if (timerCounter == 0 || Objects.requireNonNull(Quiz.isChecked.get(id))) {
            // если счетчик таймера упал до 0 или вопрос пройден
            timerView.setText(""); // очистить вьюшку для таймера
        }

        if (Objects.requireNonNull(Quiz.isChecked.get(id)) || !Quiz.inProcess) {
            // если вопрос пройден или прохождение не в процессе
            btnOff(); // отключить кнопки
            helpOff(); // отключить подсказку
            check(); // выставить цвета текста кнопкам
        }
    }
    //

    // Событие при уходе с фрагмента
    @Override
    public void onPause() {
        super.onPause();

        timerCounter = 0; // обнулить счетчик таймера

        if (timer != null) { // если таймер не пуст
            timer.cancel(); // завершить работу таймера
        }

        Quiz.isChecked.put(id, true); // записать в мап с инфой о состоянии вопроса, что этот вопрос пройден
        check();
    }
    //

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onClick(View v) {
        if (!Objects.requireNonNull(Quiz.isChecked.get(id)) && Quiz.inProcess &&
            v.getId() != R.id.question_help) {  // если вопрос не пройден и прохождение в процессе и
                                                // была нажата не кнопка подсказки

            switch (v.getId()) { // проверяем id нажатой кнопки
                case R.id.button1: // если нажата 1 кнопка
                    Quiz.choose.put(id, 0); // записать, что был выбран 0 вариант ответа
                    Quiz.isChecked.put(id, true); // записать, что вопрос пройден
                    break;
                case R.id.button2: // если нажата 2 кнопка
                    Quiz.choose.put(id, 1); // записать, что был выбран 1 вариант ответа
                    Quiz.isChecked.put(id, true); // записать, что вопрос пройден
                    break;
                case R.id.button3: // если нажата 3 кнопка
                    Quiz.choose.put(id, 2); // записать, что был выбран 2 вариант ответа
                    Quiz.isChecked.put(id, true); // записать, что вопрос пройден
                    break;
                case R.id.button4: // если нажата 4 кнопка
                    Quiz.choose.put(id, 3); // записать, что был выбран 3 вариант ответа
                    Quiz.isChecked.put(id, true); // записать, что вопрос пройден
                    break;
            }

            btnOff(); // отключить кнопки
            check(); // расставить зеленый/красный цвет текста кнопок
            listener.next(isCorrect(right, Objects.requireNonNull(Quiz.choose.get(id))));
                // вызвать метод next в активити
        } else if (!Objects.requireNonNull(Quiz.isChecked.get(id)) && Quiz.inProcess
            && v.getId() == R.id.question_help) {   // если вопрос не пройден и прохождение в процессе и
                                                    // была нажата кнопка подсказки
            Quiz.help--; // уменьшаем на 1 счетчик подсказок
            help(); // убираем 2 варианта ответа
            help.setText("50/50 (" + Quiz.help + ")"); // обновляем текст посказки
            helpOff(); // отключаем подсказку
        }
    }

    // для интерфейса
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (QuestionFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e);
        }
    }
    //

    // интерфейс для отправки данных в активити
    public interface QuestionFragmentListener {
        void next(boolean isCorrect);
    }
    //

    private boolean isCorrect(int right, int choose) { return right == choose; }

    private void btnOff() {
        // блокировка кнопок
        b1.setClickable(false); // сделать четыре кнопки некликабельными
        b2.setClickable(false);
        b3.setClickable(false);
        b4.setClickable(false);
        b1.setAlpha(0.3f); // сделать их прозрачность в 30%
        b2.setAlpha(0.3f);
        b3.setAlpha(0.3f);
        b4.setAlpha(0.3f);
    }

    private void check() {
        // расстановка цвета кнопок (зеленый/красный)

        if (right == 0) {
            // если правильный вариант ответа - первый (0) -> покрасить первую кнопку в зеленый цвет
            b1.setTextColor(requireActivity().getResources().getColor(
                R.color.right_answer,
                requireActivity().getTheme()
            ));
        } else if (Objects.requireNonNull(Quiz.choose.get(id)) == 0) {
            // если выбранный пользователем вариант ответа - первый (0) и он не правильный ->
            // покрасить первую кнопку в красный цвет
            b1.setTextColor(requireActivity().getResources().getColor(
                R.color.wrong_answer,
                requireActivity().getTheme()
            ));
        }

        if (right == 1) {
            // если правильный вариант ответа - второй (1) -> покрасить вторую кнопку в зеленый цвет
            b2.setTextColor(requireActivity().getResources().getColor(
                R.color.right_answer,
                requireActivity().getTheme()
            ));
        } else if (Objects.requireNonNull(Quiz.choose.get(id)) == 1) {
            // если выбранный пользователем вариант ответа - второй (1) и он не правильный ->
            // покрасить вторую кнопку в красный цвет
            b2.setTextColor(requireActivity().getResources().getColor(
                R.color.wrong_answer,
                requireActivity().getTheme()
            ));
        }

        if (right == 2) {
            // если правильный вариант ответа - третий (2) -> покрасить третью кнопку в зеленый цвет
            b3.setTextColor(requireActivity().getResources().getColor(
                R.color.right_answer,
                requireActivity().getTheme()
            ));
        } else if (Objects.requireNonNull(Quiz.choose.get(id)) == 2) {
            // если выбранный пользователем вариант ответа - третий (2) и он не правильный ->
            // покрасить третью кнопку в красный цвет
            b3.setTextColor(requireActivity().getResources().getColor(
                R.color.wrong_answer,
                requireActivity().getTheme()
            ));
        }

        if (right == 3) {
            // если правильный вариант ответа - четвертый (3) -> покрасить четвертую кнопку в зеленый цвет
            b4.setTextColor(requireActivity().getResources().getColor(
                R.color.right_answer,
                requireActivity().getTheme()
            ));
        } else if (Objects.requireNonNull(Quiz.choose.get(id)) == 3) {
            // если выбранный пользователем вариант ответа - четвертый (3) и он не правильный ->
            // покрасить четвертую кнопку в красный цвет
            b4.setTextColor(requireActivity().getResources().getColor(
                R.color.wrong_answer,
                requireActivity().getTheme()
            ));
        }
    }

    private void help() {
        // подсказка (блочит все кнопки, кроме правильной и 1 рандомной)
        do { // присвоить переменной random рандомное значение от 0 до 3
            random = new Random().nextInt(4);
        } while (random == right);  // пока рандм будет получаться равным номеру правильного варианта
                                    // ответа - будет генерировать снова и снова

        if (right != 0 && random != 0) {
            // если 0 вариант ответа - не рандомный и не правильный -> отключить 1 кнопку
            b1.setClickable(false);
            b1.setAlpha(0.3f);
        }

        if (right != 1 && random != 1) {
            // если 1 вариант ответа - не рандомный и не правильный -> отключить 2 кнопку
            b2.setClickable(false);
            b2.setAlpha(0.3f);
        }

        if (right != 2 && random != 2) {
            // если 2 вариант ответа - не рандомный и не правильный -> отключить 3 кнопку
            b3.setClickable(false);
            b3.setAlpha(0.3f);
        }

        if (right != 3 && random != 3) {
            // если 3 вариант ответа - не рандомный и не правильный -> отключить 4 кнопку
            b4.setClickable(false);
            b4.setAlpha(0.3f);
        }
    }

    private void helpOff() {
        // деактивация подсказки
        help.setClickable(false); // сделать некликабельной
        help.setAlpha(0.3f); // установить ей прозрачность в 30%
    }
}
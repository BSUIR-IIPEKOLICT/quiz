package com.example.quiz.view;

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

import com.example.quiz.R;
import com.example.quiz.controller.Quiz;

public class FinishFragment extends Fragment implements View.OnClickListener {

    TextView tv;
    Button btn;
    FinishFragmentListener listener; // слушатель для интерфейса

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finish, container, false);

        tv = root.findViewById(R.id.finish_text); // находим в фрагменте текстовую вьюшку
        btn = root.findViewById(R.id.finish_back); // находим кнопку
        btn.setOnClickListener(this); // на кнопку вешаем слушатель клика

        return root;
    }

    // Событие показа фрагмента
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if ((!Quiz.name.equals(""))) {
            // если имя игрока не пустое - поменять текст на правильный
            tv.setText("Поздравляем, игрок " + Quiz.name + "\nВаш результат: " + Quiz.score + " очков.");
        }
    }
    //

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.finish_back) {
            listener.finish();  // если нажата кнопка назад - вызвать метод finish в активити
        }
    }

    // Надо для интерфейса
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (FinishFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + e);
        }
    }
    //

    // Интерфейс для передачи данных в активити
    public interface FinishFragmentListener {
        void finish();
    }
    //
}
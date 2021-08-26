package com.example.quiz.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quiz.controller.Question;

public class QuestionAdapter extends FragmentStateAdapter {

    public QuestionAdapter(@NonNull FragmentActivity fa) { super(fa); }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // метод, который расставлет все фрагменты вьюпейджера
        if (position < getItemCount() - 1) {
            // все элементы, кроме последнего - question фрагмент с ссответствующей инфой
            return QuestionFragment.newInstance(Question.questions[position], position);
        } else {
            // последний - finish fragment
            return new FinishFragment();
        }
    }

    // метод, определяющий количество фрагментов
    @Override
    public int getItemCount() { return Question.questions.length + 1; }
}

package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.text.MessageFormat;
import java.util.Objects;

import loshica.quiz.controller.Coordinator;
import loshica.quiz.view.FinishFragment;
import loshica.quiz.view.MyPageTransformer;
import loshica.quiz.view.QuestionAdapter;
import loshica.quiz.view.QuestionFragment;

public class QuestionActivity extends AppCompatActivity implements
    QuestionFragment.QuestionFragmentListener,
    FinishFragment.FinishFragmentListener {

    ViewPager2 qp;
    QuestionAdapter qa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // QuestionPager
        qp = findViewById(R.id.question_pager);
        qa = new QuestionAdapter(this);
        qp.setAdapter(qa);
        qp.setCurrentItem(0);
        qp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (Coordinator.inProcess && position == qa.getItemCount() - 1) {
                    Coordinator.check();
                    Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.finish_label);
                } else {
                    Objects.requireNonNull(getSupportActionBar()).setTitle(
                        MessageFormat.format(
                            getResources().getString(R.string.question_label), position + 1
                        )
                    );
                }
            }
        });
        qp.setPageTransformer(new MyPageTransformer());
        //
    }

    @Override
    public void finish() {
        Coordinator.resetScore();
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void next(boolean isCorrect) {
        Coordinator.calcScore(isCorrect);
        Toast.makeText(getApplicationContext(), (isCorrect) ? R.string.question_right :
            R.string.question_wrong, Toast.LENGTH_SHORT).show();
        qp.setCurrentItem(qp.getCurrentItem() + 1, true);
    }

    public void onBackPressed() {
        if ((qp.getCurrentItem() > 0)) qp.setCurrentItem(qp.getCurrentItem() - 1, true);
        else startActivity(new Intent(this, MainActivity.class));
    }
}
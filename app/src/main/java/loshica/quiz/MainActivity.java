package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import loshica.quiz.viewModel.Coordinator;
import loshica.quiz.view.MainAdapter;
import loshica.quiz.view.MyPageTransformer;
import loshica.quiz.view.NameDialog;

public class MainActivity extends AppCompatActivity implements
    NameDialog.NameDialogListener {

    ViewPager2 mp;
    MainAdapter ma;
    TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Main pager
        mp = findViewById(R.id.main_pager);
        ma = new MainAdapter(this);
        mp.setAdapter(ma);
        mp.setCurrentItem(0); // показываемая на старте страничка
        mp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                // слушатель показываемой странички пейджера
                // необязательно, меняет заголовок в шапке в зависимости от странички
                super.onPageSelected(position);
                Objects.requireNonNull(getSupportActionBar()).setTitle(
                    getResources().getStringArray(R.array.main_tabs)[position]
                );
            }
        });
        mp.setPageTransformer(new MyPageTransformer()); // необязательно (анимашка пролистывания)
        //

        // TabLayout (необязательно)
        tab = findViewById(R.id.main_tab);
        new TabLayoutMediator(tab, mp, (tab, position) ->
            tab.setText(getResources().getStringArray(R.array.main_tabs)[position])
        ).attach();
        //
    }

    // Обработчик для nameDialog (сработает при нажатии на ок)
    @Override
    public void name(String playerName) {
        Coordinator.startGame(playerName);
        Coordinator.updateMaps();
        startActivity(new Intent(this, QuestionActivity.class));
    }
    //

    // необязательно, событие при нажатии кнопки назад
    public void onBackPressed() {
        if ((mp.getCurrentItem() > 0)) mp.setCurrentItem(0, true);
        else finish();
    }
    //
}
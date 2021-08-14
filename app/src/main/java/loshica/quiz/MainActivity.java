package loshica.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
    NameDialog.NameDialogListener {

    ViewPager2 mp;
    MainAdapter ma;
    TabLayout tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // LOSTheme
        new LOSTheme(this);
        //

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Main pager
        mp = findViewById(R.id.main_pager);
        ma = new MainAdapter(this);
        mp.setAdapter(ma);
        mp.setCurrentItem(0);
        mp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(getSupportActionBar()).setTitle(
                    getResources().getStringArray(R.array.main_tabs)[position]
                );
            }
        });
        mp.setPageTransformer(new MyPageTransformer());
        //

        // TabLayout
        tab = findViewById(R.id.main_tab);
        new TabLayoutMediator(tab, mp, (tab, position) ->
            tab.setText(getResources().getStringArray(R.array.main_tabs)[position])
        ).attach();
        //
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, LOSSettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void name(String username) {
        App.name = username;
        App.score = 0;
        App.inProcess = true;
        startActivity(new Intent(this, QuestionActivity.class));
    }

    public void onBackPressed() {
        if ((mp.getCurrentItem() > 0)) mp.setCurrentItem(0, true);
        else finish();
    }
}
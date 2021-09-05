package loshica.quiz

import android.content.Intent
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import loshica.quiz.databinding.ActivityMainBinding
import loshica.quiz.interfaces.MainFragmentHandler
import loshica.quiz.view.MainAdapter
import loshica.quiz.view.MyPageTransformer
import loshica.vendor.LOSActivity

class MainActivity : LOSActivity(), MainFragmentHandler {

    private val nameArg: String = "name"
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        // TODO: Main pager
        b.mPager.adapter = MainAdapter(this)
        b.mPager.currentItem = 0
        b.mPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                supportActionBar?.setTitle(resources.getStringArray(R.array.main_tabs)[position])!!
            }
        })
        b.mPager.setPageTransformer(MyPageTransformer())
        //

        // TabLayout
        TabLayoutMediator(b.tab, b.mPager) { tab: TabLayout.Tab, position: Int ->
            tab.text = resources.getStringArray(R.array.main_tabs)[position]
        }.attach()
        //
    }

    override fun name(playerName: String) {
        startActivity(Intent(this, QuestionActivity::class.java).putExtra(nameArg, playerName))
    }

    override fun onBackPressed() {
        if (b.mPager.currentItem > 0) b.mPager.setCurrentItem(0, true) else finish()
    }
}
package loshica.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import loshica.quiz.databinding.ActivityQuestionBinding
import loshica.quiz.interfaces.FinishFragmentHandler
import loshica.quiz.interfaces.QuestionFragmentHandler
import loshica.quiz.view.MyPageTransformer
import loshica.quiz.view.QuestionAdapter
import loshica.quiz.viewModel.AppState
import loshica.vendor.LOSActivity
import java.text.MessageFormat

class QuestionActivity : LOSActivity(), QuestionFragmentHandler, FinishFragmentHandler {

    private lateinit var qa: QuestionAdapter
    private lateinit var b: ActivityQuestionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)!!

        // QuestionPager
        qa = QuestionAdapter(this)
        b.qPager.adapter = qa
        b.qPager.currentItem = 0
        b.qPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (AppState.inProcess && position == qa.itemCount - 1) {
                    AppState.check()
                    supportActionBar?.setTitle(R.string.finish_label)!!
                } else {
                    supportActionBar?.title = MessageFormat.format(
                        resources.getString(R.string.question_label), position + 1
                    )
                    if (AppState.questionCounter < position) AppState.setQuestion(position)
                }
            }
        })
        b.qPager.setPageTransformer(MyPageTransformer())
        //
    }

    override fun finish() {
        AppState.resetScore()
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun next(isCorrect: Boolean) {
        AppState.calcScore(isCorrect)
        AppState.setQuestion(b.qPager.currentItem + 1)
        Toast.makeText(
            applicationContext,
            if (isCorrect) R.string.question_right else R.string.question_wrong,
            Toast.LENGTH_SHORT
        ).show()
        b.qPager.setCurrentItem(b.qPager.currentItem + 1, true)
    }

    override fun onBackPressed() {
        if (b.qPager.currentItem > 0) b.qPager.setCurrentItem(b.qPager.currentItem - 1, true)
        else startActivity(Intent(this, MainActivity::class.java))
    }
}
package loshica.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import loshica.quiz.databinding.ActivityQuestionBinding
import loshica.quiz.interfaces.FinishFragmentHandler
import loshica.quiz.interfaces.QuestionFragmentHandler
import loshica.quiz.view.MyPageTransformer
import loshica.quiz.view.QuestionAdapter
import loshica.quiz.viewModel.*
import loshica.vendor.LOSActivity

class QuestionActivity : LOSActivity(), QuestionFragmentHandler, FinishFragmentHandler {

    private val nameArg: String = "name"
    private lateinit var qa: QuestionAdapter
    private lateinit var b: ActivityQuestionBinding

    private val game: GameModel by viewModels()
    private val question: QuestionModel by viewModels()
    private val player: PlayerModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityQuestionBinding.inflate(layoutInflater)
        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)!!
        game.setName(intent.getStringExtra(nameArg)!!.toString())
        player.preload()

        // QuestionPager
        qa = QuestionAdapter(question.getData(), this)
        b.qPager.adapter = qa
        b.qPager.currentItem = 0
        b.qPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (game.inProcess() && position == qa.itemCount - 1) {
                    if (game.getPlayer().score >= 50) {
                        player.check(game.getPlayer())
                    }

                    game.cancel()
                    supportActionBar?.setTitle(R.string.finish_label)!!
                } else {
                    supportActionBar?.title = "Вопрос ${position + 1}"
                    question.checkCounter(position)
                }
            }
        })
        b.qPager.setPageTransformer(MyPageTransformer())
        //
    }

    override fun finish() { startActivity(Intent(this, MainActivity::class.java)) }

    override fun next(isCorrect: Boolean) {
        game.calcScore(isCorrect)
        question.incCounter()
        Toast.makeText(applicationContext, question.toastText(b.qPager.currentItem), Toast.LENGTH_SHORT)
            .show()
        b.qPager.setCurrentItem(b.qPager.currentItem + 1, true)
    }

    override fun onBackPressed() {
        if (b.qPager.currentItem > 0) b.qPager.setCurrentItem(b.qPager.currentItem - 1, true)
        else startActivity(Intent(this, MainActivity::class.java))
    }
}
package loshica.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import loshica.quiz.databinding.ActivityQuestionBinding
import loshica.quiz.interfaces.FinishFragmentHandler
import loshica.quiz.interfaces.QuestionFragmentHandler
import loshica.quiz.view.MyPageTransformer
import loshica.quiz.view.QuestionAdapter
import loshica.quiz.viewModel.*
import loshica.vendor.LOSActivity
import java.text.MessageFormat

class QuestionActivity : LOSActivity(), QuestionFragmentHandler, FinishFragmentHandler {

    private val nameArg: String = "name"
    private lateinit var qa: QuestionAdapter
    private lateinit var b: ActivityQuestionBinding

    private lateinit var game: GameModel
    private lateinit var gameFactory: GameModelFactory
    private val question: QuestionModel by viewModels()
    private val storage: StorageModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = ActivityQuestionBinding.inflate(layoutInflater)
        gameFactory = GameModelFactory(intent.getStringExtra(nameArg)!!.toString(), application)
        game = ViewModelProvider(this, gameFactory).get(GameModel::class.java)

        setContentView(b.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)!!

        // QuestionPager
        qa = QuestionAdapter(question.getQuestions(), this)
        b.qPager.adapter = qa
        b.qPager.currentItem = 0
        b.qPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (game.inProcess() && position == qa.itemCount - 1) {
                    storage.check(game.getPlayer())
                    game.cancel()
                    supportActionBar?.setTitle(R.string.finish_label)!!
                } else {
                    supportActionBar?.title = MessageFormat.format(
                        resources.getString(R.string.question_label), position + 1
                    )
                    if (question.isLeftSwipe(position)) question.setCounter(position)
                }
            }
        })
        b.qPager.setPageTransformer(MyPageTransformer())
        //
    }

    override fun finish() { startActivity(Intent(this, MainActivity::class.java)) }

    override fun next(isCorrect: Boolean) {
        game.calcScore(isCorrect)
        question.setCounter(b.qPager.currentItem + 1)
        Toast.makeText(applicationContext, question.toastText(b.qPager.currentItem), Toast.LENGTH_SHORT)
            .show()
        b.qPager.setCurrentItem(b.qPager.currentItem + 1, true)
    }

    override fun onBackPressed() {
        if (b.qPager.currentItem > 0) b.qPager.setCurrentItem(b.qPager.currentItem - 1, true)
        else startActivity(Intent(this, MainActivity::class.java))
    }
}
package loshica.quiz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import loshica.quiz.R
import loshica.quiz.databinding.FragmentQuestionBinding
import loshica.quiz.interfaces.QuestionFragmentHandler
import loshica.quiz.model.Question
import loshica.quiz.viewModel.*
import java.util.*

class QuestionFragment : Fragment(), View.OnClickListener {

    private var _b: FragmentQuestionBinding? = null
    private val b get() = _b!!

    private var stringsId: Int = 0
    private var strings: Array<String>? = null
    private var img = 0
    private var right = 0
    private var pos = 0

    private var rb: RadioButton? = null
    private val alpha = 0.3f
    private var random = 0
    private var handler: QuestionFragmentHandler? = null

    private lateinit var timerCounterObserver: Observer<Int>
    private lateinit var helpCounterObserver: Observer<Int>
    private lateinit var gameInProcessObserver: Observer<Boolean>
    private lateinit var questionCounterObserver: Observer<Int>

    private val timer: TimerModel by viewModels()
    private val help: HelpModel by activityViewModels()
    private val game: GameModel by activityViewModels()
    private val question: QuestionModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            stringsId = requireArguments().getInt(ARG_STRINGS_ID)
            img = requireArguments().getInt(ARG_IMG)
            pos = requireArguments().getInt(ARG_POS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentQuestionBinding.inflate(inflater, container, false)

        strings = requireActivity().resources.getStringArray(stringsId)
        right = strings!![5].toInt()

        b.questionIv.setImageResource(img)
        b.questionTv.text = strings!![0]

        for (i in 0 until b.questionRg.childCount) {
            rb = b.questionRg.getChildAt(i) as RadioButton
            rb!!.text = strings!![i + 1]
            rb!!.setOnClickListener(this)
        }

        b.questionHelp.setOnClickListener(this)
        handler = activity as? QuestionFragmentHandler

        timerCounterObserver = Observer {
            b.questionTimer.text = if (it >= 0) it.toString() else ""

            when (it) {
                0 -> block()
                -1 -> {
                    timer.reset()
                    timer.cancel()
                    handler?.next(false)
                }
            }
        }

        helpCounterObserver = Observer {
            b.questionHelp.text = help.text()
            if (it == 0) helpOff()
        }

        gameInProcessObserver = Observer {
            if (!it) block()
        }

        questionCounterObserver = Observer {
            if (it == pos && !timer.isZero()) timer.start()
            else if (it > pos) {
                block()
                timer.reset()
                timer.cancel()
            }
        }

        return b.root
    }

    override fun onStart() {
        super.onStart()

        if (question.isLast(pos)) timer.start()
        timer.counter.observe(this, timerCounterObserver)
        help.counter.observe(this, helpCounterObserver)
        game.inProcess.observe(this, gameInProcessObserver)
        question.counter.observe(this, questionCounterObserver)
    }

    override fun onResume() {
        super.onResume()

        if (!question.isLast(pos)) {
            timer.reset()
            timer.cancel()
        }
    }

    override fun onPause() {
        super.onPause()

        timer.reset()
        timer.cancel()
        if (question.isLast(pos)) question.incCounter()
        check()
    }

    override fun onStop() {
        super.onStop()

        timer.counter.removeObserver(timerCounterObserver)
        help.counter.removeObserver(helpCounterObserver)
        game.inProcess.removeObserver(gameInProcessObserver)
        question.counter.removeObserver(questionCounterObserver)
    }

    override fun onClick(v: View) {
        if (question.isLast(pos) && game.inProcess()) {
            when (v) {
                b.questionHelp -> {
                    help.use()
                    help()
                    helpOff()
                }
                else -> {
                    for (i in 0 until b.questionRg.childCount) {
                        if (v === b.questionRg.getChildAt(i)) question.setChoose(pos, i)
                    }
                    radioOff()
                    check()
                    handler?.next(question.isCorrect(pos))
                }
            }
        }
    }

    private fun radioOff() {
        for (i in 0 until b.questionRg.childCount) {
            b.questionRg.getChildAt(i).isClickable = false
            b.questionRg.getChildAt(i).alpha = alpha
        }
    }

    private fun check() {
        for (i in 0 until b.questionRg.childCount) {
            if (i == right) {
                rb = b.questionRg.getChildAt(i) as RadioButton
                rb!!.setTextColor(
                    requireActivity().resources.getColor(R.color.right_answer, requireActivity().theme)
                )
            } else if (i == question.getChoose(pos)) {
                rb = b.questionRg.getChildAt(i) as RadioButton
                rb!!.setTextColor(
                    requireActivity().resources.getColor(R.color.wrong_answer, requireActivity().theme)
                )
            }
        }
    }

    private fun help() {
        do { random = Random().nextInt(b.questionRg.childCount) } while (random == right)
        for (i in 0 until b.questionRg.childCount) {
            if (i != right && i != random) {
                b.questionRg.getChildAt(i).isClickable = false
                b.questionRg.getChildAt(i).alpha = alpha
            }
        }
    }

    private fun helpOff() {
        b.questionHelp.isClickable = false
        b.questionHelp.alpha = alpha
    }

    private fun block() {
        radioOff()
        helpOff()
        check()
    }

    companion object {
        private const val ARG_STRINGS_ID = "stringsId"
        private const val ARG_IMG = "img"
        private const val ARG_POS = "pos"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param q Question object with params.
         * @return A new instance of fragment Question.
         */
        fun newInstance(q: Question): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()

            // TODO: My question obj parser
            args.putInt(ARG_STRINGS_ID, q.stringsId)
            args.putInt(ARG_IMG, q.img)
            args.putInt(ARG_POS, q.pos)
            //
            fragment.arguments = args
            return fragment
        }
    }
}
package loshica.quiz.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import loshica.quiz.R
import loshica.quiz.databinding.FragmentQuestionBinding
import loshica.quiz.interfaces.QuestionFragmentHandler
import loshica.quiz.viewModel.AppState
import loshica.quiz.viewModel.Question
import java.text.MessageFormat
import java.util.*

class QuestionFragment : Fragment(), View.OnClickListener {

    private var _b: FragmentQuestionBinding? = null
    private val b get() = _b!!

    private var strings: Array<String>? = null
    private var img = 0
    private var right = 0
    private var pos = 0

    private var rb: RadioButton? = null

    private val alpha = 0.3f
    private var timerCounter = 0

    private var random = 0
    private var timer: CountDownTimer? = null
    var handler: QuestionFragmentHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            strings = requireArguments().getStringArray(ARG_STRINGS)
            img = requireArguments().getInt(ARG_IMG)
            pos = requireArguments().getInt(ARG_POS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentQuestionBinding.inflate(inflater, container, false)
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

        timerCounter = 15
        timer = object : CountDownTimer(15000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                b.questionTimer.text = (timerCounter--).toString()
            }

            override fun onFinish() { handler?.next(false) }
        }
        return b.root
    }

    override fun onResume() {
        super.onResume()

        if (AppState.questionCounter == pos && timerCounter == 15) timer!!.start()
        else timer!!.cancel()

        b.questionHelp.text = MessageFormat.format(resources.getString(R.string.question_help), AppState.help)

        if (AppState.help == 0) helpOff()
        if (timerCounter == 0) b.questionTimer.text = ""
        if (AppState.questionCounter > pos || !AppState.inProcess || timerCounter == 0) {
            radioOff()
            helpOff()
            check()
        }
    }

    override fun onPause() {
        super.onPause()
        timerCounter = 0
        timer!!.cancel()
        AppState.setQuestion(pos + 1)
        check()
    }

    override fun onClick(v: View) {
        if (AppState.questionCounter == pos && AppState.inProcess) {
            when (v) {
                b.questionHelp -> {
                    AppState.useHelp()
                    help()
                    b.questionHelp.text = MessageFormat.format(resources.getString(R.string.question_help), AppState.help)
                    helpOff()
                }
                else -> {
                    for (i in 0 until b.questionRg.childCount) {
                        if (v === b.questionRg.getChildAt(i)) Question.questions[pos].choose = i
                    }
                    radioOff()
                    check()
                    handler?.next(isCorrect(right, Question.questions[pos].choose))
                }
            }
        }
    }

    private fun isCorrect(right: Int, choose: Int): Boolean = right == choose

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
            } else if (i == Question.questions[pos].choose) {
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

    companion object {
        private const val ARG_STRINGS = "strings"
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
            args.putStringArray(ARG_STRINGS, q.strings)
            args.putInt(ARG_IMG, q.img)
            args.putInt(ARG_POS, q.pos)
            //
            fragment.arguments = args
            return fragment
        }
    }
}
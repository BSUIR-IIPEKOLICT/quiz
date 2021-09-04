package loshica.quiz.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import loshica.quiz.R
import loshica.quiz.databinding.FragmentQuestionBinding
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
    var listener: QuestionFragmentListener? = null

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
            println("this: $this.toString()")
        }

        b.questionHelp.setOnClickListener(this)

        timerCounter = 15
        if (!AppState.isChecked[pos]!!) {
            timer = object : CountDownTimer(15000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    b.questionTimer.text = (timerCounter--).toString()
                }

                override fun onFinish() {
                    AppState.isChecked[pos] = true
                    listener!!.next(false)
                }
            }.start()
        }
        return b.root
    }

    override fun onResume() {
        super.onResume()
        b.questionHelp.text = MessageFormat.format(resources.getString(R.string.question_help), AppState.help)

        if (AppState.help == 0) helpOff()
        if (timerCounter == 0 || AppState.isChecked[pos]!!) b.questionTimer.text = ""
        if (AppState.isChecked[pos]!! || !AppState.inProcess) {
            radioOff()
            helpOff()
            check()
        }
    }

    override fun onPause() {
        super.onPause()
        timerCounter = 0
        if (timer != null) timer!!.cancel()
        AppState.isChecked[pos] = true
        check()
    }

    override fun onClick(v: View) {
        if (!AppState.isChecked[pos]!! && AppState.inProcess) {
            when (v) {
                b.questionHelp -> {
                    AppState.help--
                    help()
                    b.questionHelp.text = MessageFormat.format(resources.getString(R.string.question_help), AppState.help)
                    helpOff()
                }
                else -> {
                    for (i in 0 until b.questionRg.childCount) {
                        if (v === b.questionRg.getChildAt(i)) {
                            AppState.choose[pos] = i
                            AppState.isChecked[pos] = true
                        }
                    }
                    radioOff()
                    check()
                    listener!!.next(isCorrect(right, AppState.choose[pos]!!))
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try { context as QuestionFragmentListener }
        catch (e: ClassCastException) { throw ClassCastException(context.toString() + e) }
    }

    interface QuestionFragmentListener {
        fun next(isCorrect: Boolean)
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
                    requireActivity().resources.getColor(
                        R.color.right_answer, requireActivity().theme
                    )
                )
            } else if (i == AppState.choose[pos]!!) {
                rb = b.questionRg.getChildAt(i) as RadioButton
                rb!!.setTextColor(
                    requireActivity().resources.getColor(
                        R.color.wrong_answer, requireActivity().theme
                    )
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
         * @param id Question id.
         * @return A new instance of fragment Question.
         */
        fun newInstance(q: Question, id: Int): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()

            // TODO: My question obj parser
            args.putStringArray(ARG_STRINGS, q.strings)
            args.putInt(ARG_IMG, q.img)
            //
            args.putInt(ARG_POS, id)
            fragment.arguments = args
            return fragment
        }
    }
}
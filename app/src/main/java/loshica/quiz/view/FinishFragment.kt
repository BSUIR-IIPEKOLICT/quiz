package loshica.quiz.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import loshica.quiz.R
import loshica.quiz.databinding.FragmentFinishBinding
import loshica.quiz.viewModel.AppState
import java.text.MessageFormat

class FinishFragment : Fragment(), View.OnClickListener {

    private var _b: FragmentFinishBinding? = null
    private val b get() = _b!!
    private var listener: FinishFragmentListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentFinishBinding.inflate(inflater, container, false)
        b.finishBack.setOnClickListener(this)
        return b.root
    }

    override fun onResume() {
        super.onResume()
        val generic = MessageFormat.format(
            resources.getString(R.string.finish_text), AppState.name, AppState.score
        )
        val textDefault = resources.getString(R.string.finish_default_text)
        b.finishText.text = if (AppState.name != "") generic else textDefault
    }

    override fun onClick(v: View) {
        when (v) {
            b.finishBack -> listener!!.finish()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try { context as FinishFragmentListener }
        catch (e: ClassCastException) { throw ClassCastException(context.toString() + e) }
    }

    interface FinishFragmentListener {
        fun finish()
    }
}
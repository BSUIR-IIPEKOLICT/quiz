package loshica.quiz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import loshica.quiz.databinding.FragmentFinishBinding
import loshica.quiz.interfaces.FinishFragmentHandler
import loshica.quiz.viewModel.GameModel

class FinishFragment : Fragment(), View.OnClickListener {

    private var _b: FragmentFinishBinding? = null
    private val b get() = _b!!

    private val game: GameModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentFinishBinding.inflate(inflater, container, false)

        b.finishBack.setOnClickListener(this)
        game.counter.observe(viewLifecycleOwner, Observer {
            b.finishText.text = game.finishText()
        })

        return b.root
    }

    override fun onClick(v: View) {
        when (v) {
            b.finishBack -> (activity as? FinishFragmentHandler)?.finish()
        }
    }
}
package loshica.quiz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import loshica.quiz.R
import loshica.quiz.databinding.FragmentMainBinding

class MainFragment : Fragment(), View.OnClickListener {

    private var _b: FragmentMainBinding? = null
    private val b get() = _b!!
    private lateinit var dialog: NameDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentMainBinding.inflate(inflater, container, false)
        b.mainPlay.setOnClickListener(this)
        return b.root
    }

    override fun onClick(v: View) {
        if (v.id == R.id.main_play) {
            dialog = NameDialog()
            dialog.show(requireActivity().supportFragmentManager, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
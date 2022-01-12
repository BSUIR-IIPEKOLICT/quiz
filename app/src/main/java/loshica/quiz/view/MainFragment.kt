package loshica.quiz.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import loshica.quiz.R
import loshica.quiz.databinding.DialogNameBinding
import loshica.quiz.databinding.FragmentMainBinding
import loshica.quiz.interfaces.MainFragmentHandler
import loshica.vendor.view.LOSDialogBuilder

class MainFragment : Fragment(), View.OnClickListener {

    private var _b: FragmentMainBinding? = null
    private val b get() = _b!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _b = FragmentMainBinding.inflate(inflater, container, false)
        b.mainPlay.setOnClickListener(this)
        return b.root
    }

    override fun onClick(v: View) {
        when (v) {
            b.mainPlay -> {
                val name: DialogNameBinding = DialogNameBinding.inflate(layoutInflater)

                LOSDialogBuilder(requireContext())
                    .setTitle(R.string.name_dialog_title)
                    .setView(name.root)
                    .setPositiveButton(R.string.ok) { dialog, _ ->
                        if (
                            !name.nameInput.text?.equals("")!! &&
                            name.nameInput.text?.trim()?.length != 0
                        ) {
                            (activity as? MainFragmentHandler)?.name(name.nameInput.text.toString())
                        } else dialog.cancel()
                    }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }
}
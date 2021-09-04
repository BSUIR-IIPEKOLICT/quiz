package loshica.quiz.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import loshica.quiz.R
import loshica.quiz.databinding.DialogNameBinding
import loshica.vendor.view.LOSDialogBuilder

class NameDialog : DialogFragment() {

    private var _b: DialogNameBinding? = null
    private val b get() = _b!!
    private var listener: NameDialogListener? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = LOSDialogBuilder(requireActivity())
        _b = DialogNameBinding.inflate(requireActivity().layoutInflater)

        return builder
            .setView(b.root)
            .setTitle(R.string.name_dialog_title)
            .setPositiveButton(R.string.ok) { dialog, _ ->
                if (b.nameInput.text.toString() != "") listener!!.name(b.nameInput.text.toString())
                else dialog.cancel()
            }
            .setNegativeButton(R.string.cancel, null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _b = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try { context as NameDialogListener }
        catch (e: ClassCastException) { throw ClassCastException(context.toString() + e) }
    }

    interface NameDialogListener {
        fun name(playerName: String)
    }
}
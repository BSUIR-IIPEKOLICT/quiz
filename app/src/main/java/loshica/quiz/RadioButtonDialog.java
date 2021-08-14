package loshica.quiz;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class RadioButtonDialog extends DialogFragment {

    private static final String ARG_VALUES = "values";
    private static final String ARG_LABELS = "labels";
    private static final String ARG_KEY = "key";
    private static final String ARG_SETTINGS = "settings";
    private static final String ARG_DEFAULT = "default";
    private static final String ARG_TITLE = "title";
    private static final String ARG_CHECKED = "checked";

    int[] values;
    String[] labels;
    String key;
    String settings;
    int def;
    int title;
    int checked;

    RadioGroup rg;
    RadioButtonDialogListener listener;

    public static RadioButtonDialog newInstance(
        int[] values, String[] labels, String key, String settings, int def, int title, int checked
    ) {
        RadioButtonDialog dialog = new RadioButtonDialog();
        Bundle args = new Bundle();
        args.putIntArray(ARG_VALUES, values);
        args.putStringArray(ARG_LABELS, labels);
        args.putString(ARG_KEY, key);
        args.putString(ARG_SETTINGS, settings);
        args.putInt(ARG_DEFAULT, def);
        args.putInt(ARG_TITLE, title);
        args.putInt(ARG_CHECKED, checked);
        dialog.setArguments(args);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_radio_button, null);

        assert getArguments() != null;
        values = getArguments().getIntArray(ARG_VALUES);
        labels = getArguments().getStringArray(ARG_LABELS);
        key = getArguments().getString(ARG_KEY);
        settings = getArguments().getString(ARG_SETTINGS);
        def = getArguments().getInt(ARG_DEFAULT);
        title = getArguments().getInt(ARG_TITLE);
        checked = getArguments().getInt(ARG_CHECKED);

        rg = root.findViewById(R.id.rbd_rg);
        for (int i = 0; i < values.length; i++) {
            RadioButton rb = new RadioButton(this.getContext());
            rb.setText(labels[i]);
            rb.setId(i);
            rb.setGravity(Gravity.CENTER_VERTICAL);
            rg.addView(rb, new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, 120));
        }
        rg.check(checked);
        rg.setOnCheckedChangeListener((group, checkedId) -> listener.change(key, checkedId));

        Dialog dialog = builder
            .setView(root)
            .setTitle(requireActivity().getResources().getText(title))
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .create();

        LOSDialog.convert(dialog);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try { listener = (RadioButtonDialogListener) context; }
        catch (ClassCastException e) { throw new ClassCastException(context.toString() + e); }
    }

    public interface RadioButtonDialogListener {
        void change(String key, int value);
    }
}

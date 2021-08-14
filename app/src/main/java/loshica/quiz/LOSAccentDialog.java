package loshica.quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class LOSAccentDialog extends DialogFragment implements View.OnClickListener {

    int theme;
    boolean dark;
    SharedPreferences settings;

    RadioGroup table, row1, row2;
    RadioGroup.LayoutParams lp;

    LOSAccentDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_accent, null);

        settings = requireActivity().getSharedPreferences(LOSTheme.SETTINGS, Context.MODE_PRIVATE);
        theme = settings.getInt(LOSTheme.THEME_KEY, LOSTheme.THEME_DEFAULT);
        dark = (theme == 0) ? LOSTheme.isSystemDark : (theme < 2);

        table = root.findViewById(R.id.accent_root);
        row1 = (RadioGroup) table.getChildAt(0);
        row2 = (RadioGroup) table.getChildAt(1);
        lp = new RadioGroup.LayoutParams(120, 120);

        for (int i = 0; i < LOSTheme.coloredBgs.length - 1; i++) {
            CheckedTextView button = new CheckedTextView(this.getContext());
            button.setText(R.string.colored_btn_text);
            button.setId(i);
            button.setGravity(Gravity.CENTER);
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            button.setTextAppearance(R.style.ColoredBthStyle);
            button.setBackgroundResource(
                (i == LOSTheme.coloredBgs.length - 2 && dark) ?
                LOSTheme.coloredBgs[i + 1] : LOSTheme.coloredBgs[i]
            );
            button.setOnClickListener(this);

            if (settings.getInt(LOSTheme.ACCENT_KEY, LOSTheme.ACCENT_DEFAULT) == i) {
                button.setChecked(true);
            }

            if (i - 6 < 0) row1.addView(button, lp);
            else row2.addView(button, lp);
        }

        Dialog dialog = builder
            .setView(root)
            .setTitle(requireActivity().getResources().getText(R.string.accent_section))
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)
            .create();

        LOSUtils.dialog(dialog);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        listener.setAccent(v.getId());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try { listener = (LOSAccentDialogListener) context; }
        catch (ClassCastException e) { throw new ClassCastException(context.toString() + e); }
    }

    public interface LOSAccentDialogListener {
        void setAccent(int value);
    }
}

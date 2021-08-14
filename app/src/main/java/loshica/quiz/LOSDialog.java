package loshica.quiz;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class LOSDialog {

    public static void convert(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.horizontalMargin = 0;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);

        dialog.setOnShowListener(dialogInterface -> {
            View view = window.getDecorView();
            ObjectAnimator
                .ofFloat(view, "translationY", view.getHeight(), 0.0f)
                .start();
        });
    }
}

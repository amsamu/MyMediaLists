package com.amsamu.mymedialists.util;

import android.content.Context;
import android.widget.Toast;

public class ToastManager {

    public static Toast toast = null;

    public static void showToast(final Context context, CharSequence text, int duration) {
        if (toast != null) {
            toast.cancel();
            toast = null;
            showToast(context, text, duration);
        } else {
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public static void showToast(final Context context, int textId, int duration) {
        showToast(context, context.getString(textId), duration);
    }

    public static void cancelToast() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}

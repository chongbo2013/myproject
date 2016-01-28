package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.SoftReference;

public final class Toasts {
    private static SoftReference<Toast> sLastToastWrapper;
    private static final int OFFSET_X = 0;
    private static final int OFFSET_Y = 50;

    public static final int LENGTH_SHORT = Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = Toast.LENGTH_LONG;

    private static Toast mToast;
    private static TextView mMsgView;

    private Toasts() {
    }

    public static void showText(Context context, CharSequence text, int duration) {
        showToast(genStyledToast(context, text, duration));
    }

    public static void showShortLengthText(Context context, CharSequence text) {
        showToast(genStyledToast(context, text, LENGTH_SHORT));
    }

    public static void showShortLengthText(Context context, int resId) {
        showText(context, resId, LENGTH_SHORT);
    }

    public static void showLongLengthText(Context context, CharSequence text) {
        showToast(genStyledToast(context, text, LENGTH_LONG));
    }

    public static void showText(Context context, int resId, int duration) {
        showText(context, context.getString(resId), duration);
    }

    public static void hideText() {
        hideToast();
    }

    private static Toast genStyledToast(Context context, CharSequence text, int duration) {
//        final TextView messageView = new TextView(context);
//        messageView.setBackgroundColor(Color.BLACK);
//        messageView.setTextColor(Color.WHITE);
//        messageView.setText(text);
//        messageView.setTextSize(20);
//
//        final Toast newToast = new Toast(context);
//        newToast.setView(messageView);
//        newToast.setDuration(duration);
//        newToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, OFFSET_X, OFFSET_Y);
//        return newToast;
        if (mToast == null) {
            mMsgView = new TextView(context);
            mMsgView.setBackgroundColor(Color.BLACK);
            mMsgView.setTextColor(Color.WHITE);
            mMsgView.setText(text);
            mMsgView.setTextSize(20);

            mToast = new Toast(context);
            mToast.setView(mMsgView);
            mToast.setDuration(duration);
            mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, OFFSET_X, OFFSET_Y);
//            mToast = Toast.makeText(context, text, duration);
        } else {
//            mToast.setText(text);
            mMsgView.setText(text);
        }
        return mToast;
    }

    private static void showToast(Toast targetToast) {
//        ensureLastToastCanceled();
//        if (targetToast != null) {
//            sLastToastWrapper = new SoftReference<Toast>(targetToast);
//            targetToast.show();
//        }
        mToast.show();
    }

    private static void hideToast() {
//        ensureLastToastCanceled();
        if (mToast != null) {
            mToast.cancel();
        }
    }

    private static void ensureLastToastCanceled() {
        if (sLastToastWrapper != null) {
            final Toast lastShowedToast = sLastToastWrapper.get();
            if (lastShowedToast != null) {
                lastShowedToast.cancel();
            }
            sLastToastWrapper.clear();
            sLastToastWrapper = null;
        }
    }
}

package com.yeyanxiang.util.gitv;

import android.os.Build;
import android.text.format.DateFormat;

public final class TimeUtils {

    private TimeUtils() {
    }

    public static String getCurrentTime() {
        final String pattern = (Build.VERSION.SDK_INT < 18 ? "kk:mm" : "HH:mm");
        return DateFormat.format(pattern, System.currentTimeMillis()).toString();
    }
}
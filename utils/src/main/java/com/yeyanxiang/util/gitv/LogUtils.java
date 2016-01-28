package com.yeyanxiang.util.gitv;

import android.util.Log;

/**
 * Created by yeyanxiang on 15-4-28.
 */
public class LogUtils {

    public static void logd(String Tag, String msg) {
        Log.d(Tag, msg);
    }

    public static void loge(String Tag, String msg) {
        Log.e(Tag, msg);
    }

    public static void logi(String Tag, String msg) {
        Log.i(Tag, msg);
    }

    public static void logw(String Tag, String msg) {
        Log.w(Tag, msg);
    }

    public static void logv(String Tag, String msg) {
        Log.v(Tag, msg);
    }
}

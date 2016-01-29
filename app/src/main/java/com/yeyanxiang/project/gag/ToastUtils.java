package com.yeyanxiang.project.gag;

import android.content.Context;
import android.widget.Toast;

import com.yeyanxiang.project.ProjectApplication;

/**
 * @author 叶雁翔
 * @version 1.0
 * @Email yanxiang1120@gmail.com
 * @update 2014年6月26日
 * @简介
 */
public class ToastUtils {
    private ToastUtils() {
    }

    private static void show(Context context, int resId, int duration) {
        Toast.makeText(context, resId, duration).show();
    }

    private static void show(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(ProjectApplication.getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(String message) {
        Toast.makeText(ProjectApplication.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(int resId) {
        Toast.makeText(ProjectApplication.getContext(), resId, Toast.LENGTH_LONG).show();
    }

    public static void showLong(String message) {
        Toast.makeText(ProjectApplication.getContext(), message, Toast.LENGTH_LONG).show();
    }
}

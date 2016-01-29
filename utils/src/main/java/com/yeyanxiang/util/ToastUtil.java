package com.yeyanxiang.util;

import android.content.Context;
import android.widget.Toast;

/**
 * @author 叶雁翔
 * @version 1.0
 * @Email yanxiang1120@gmail.com
 * @data 2014年1月2日 下午5:16:00
 * @简介
 */
public class ToastUtil {
    private static Toast toast;

    public static Toast createToast(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        return toast;
    }

    public static Toast showText(Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
        return toast;
    }

    public static Toast showText(Toast toast, Context context, String text) {
        if (toast == null) {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
        return toast;
    }
}

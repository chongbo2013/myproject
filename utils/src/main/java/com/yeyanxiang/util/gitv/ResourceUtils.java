package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created on 2015-11-20
 *
 * @author lizhi (lizhi@cnbn.cn)
 */
public final class ResourceUtils {
    private ResourceUtils() { /* No instances */ }

    public static String getString(@NonNull Context context, @StringRes int resId) {
        return context.getString(resId);
    }
}

package com.yeyanxiang.util.gitv;

import android.util.Base64;

/**
 * Created by yeyanxiang on 15-7-6.
 */
public class Base64Utils {

    public static String encode(String encodeStr){
        return Base64.encodeToString(encodeStr.getBytes(), Base64.NO_WRAP);
    }

}

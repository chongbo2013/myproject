package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.content.SharedPreferences;


public class SharedPreferencesUtils {

    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences mPingSharedPreferences;

    public static void init(Context context, String name) {
        mSharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    public static void initPing(Context context,String name){
        mPingSharedPreferences=context.getSharedPreferences(name, Context.MODE_MULTI_PROCESS);
    }

    public static boolean put(String key, Object value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(key, String.valueOf(value));
        return editor.commit();
    }

    public static synchronized String getString(String key) {
        return mSharedPreferences.getString(key, null);
    }

    public static void putPing(String key,String value){
        SharedPreferences.Editor editor = mPingSharedPreferences.edit();
        editor.putString(key, value).apply();
    }

    public static String getPingString(String key) {
        return mPingSharedPreferences.getString(key,"");
    }

}

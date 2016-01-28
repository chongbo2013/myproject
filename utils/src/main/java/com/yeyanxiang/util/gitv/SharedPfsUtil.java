package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

/**
 * Created by yeyanxiang on 15-7-14.
 */
public class SharedPfsUtil {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharedPfsUtil(Context context, String name) {
        sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    public SharedPreferences getSharedPreferences() {
        return sp;
    }

    public boolean putValue(String key, String value) {
        editor = sp.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public boolean putValue(String key, boolean value) {
        editor = sp.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public boolean putValue(String key, float value) {
        editor = sp.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    public boolean putValue(String key, int value) {
        editor = sp.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public boolean putValue(String key, long value) {
        editor = sp.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public boolean putValue(String key, List<String> values) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : values) {
            stringBuilder.append(str + ",");
        }
        editor = sp.edit();
        editor.putString(key, stringBuilder.toString());
        return editor.commit();
    }

    public boolean removeValue(String key) {
        editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

    public String getValue(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public boolean getValue(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public float getValue(String key, float defValue) {
        return sp.getFloat(key, defValue);
    }

    public int getValue(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public long getValue(String key, long defValue) {
        return sp.getLong(key, defValue);
    }

    public List<String> getValue(String key) {
        String value = sp.getString(key, "");
        String[] values = value.split(",");
        return Arrays.asList(value);
    }
}

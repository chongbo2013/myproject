package com.yeyanxiang.project.inject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

public class InjectProxyMainActivity extends InjectProxyBaseActivity {
    private static final String TAG = "InjectProxyMainActivity";
    private final String START = "onStart";
    private final String RESUME = "onResume";
    private final String PAUSE = "onPause";
    private final String STOP = "onStop";
    private final String DESTROY = "onDestroy";
    private Object pluginActivity;
    private Class<?> pluginClass;

    private HashMap<String, Method> methodMap = new HashMap<String, Method>();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        File appFile = new File(getFilesDir() + "/app.apk");
        setApkPath(appFile.getAbsolutePath());
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate ");
        try {
            File filesDir = this.getCacheDir();
            File dexOutputDir = getDir("dex", MODE_PRIVATE);
            Log.i(TAG, "initClassLoader filesDir " + filesDir.getAbsolutePath() + " dex " + dexOutputDir.getAbsolutePath());
            DexClassLoader loader = new DexClassLoader(apkPath, dexOutputDir.getAbsolutePath(), filesDir.getAbsolutePath(), getClass().getClassLoader());

            //动态加载插件Activity
            pluginClass = loader.loadClass("com.cnbn.android_test2.PassiveProxyActivity");
            Constructor<?> localConstructor = pluginClass.getConstructor(new Class[]{});
            pluginActivity = localConstructor.newInstance(new Object[]{});

            //将代理对象设置给插件Activity
            Method setProxy = pluginClass.getMethod("setProxy", new Class[]{Activity.class});
            setProxy.setAccessible(true);
            setProxy.invoke(pluginActivity, new Object[]{this});

            initMethodMap();

            //调用它的onCreate方法
            Method onCreate = pluginClass.getDeclaredMethod("onCreate",
                    new Class[]{Bundle.class});
            onCreate.setAccessible(true);
            onCreate.invoke(pluginActivity, new Object[]{new Bundle()});

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate error " + e);
        }
    }

    /**
     * 存储每个生命周期的方法
     */
    private void initMethodMap() {
        methodMap.put(START, null);
        methodMap.put(RESUME, null);
        methodMap.put(PAUSE, null);
        methodMap.put(STOP, null);
        methodMap.put(DESTROY, null);

        for (String key : methodMap.keySet()) {
            try {
                Method method = pluginClass.getDeclaredMethod(key);
                if (method != null) {
                    method.setAccessible(true);
                    methodMap.put(key, method);
                    Log.i(TAG, "initMethodMap getMethod success " + key);
                } else {
                    Log.e(TAG, "initMethodMap getMethod fail " + key);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "initMethodMap error " + e);
            }
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart ");
        try {
            methodMap.get(START).invoke(pluginActivity, new Object[]{});
        } catch (Exception e) {
            Log.e(TAG, START + " error " + e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume ");
        try {
            methodMap.get(RESUME).invoke(pluginActivity, new Object[]{});
        } catch (Exception e) {
            Log.e(TAG, RESUME + " error " + e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause ");
        try {
            methodMap.get(PAUSE).invoke(pluginActivity, new Object[]{});
        } catch (Exception e) {
            Log.e(TAG, PAUSE + " error " + e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop ");
        try {
            methodMap.get(STOP).invoke(pluginActivity, new Object[]{});
        } catch (Exception e) {
            Log.e(TAG, STOP + " error " + e);
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy ");
        try {
            methodMap.get(DESTROY).invoke(pluginActivity, new Object[]{});
        } catch (Exception e) {
            Log.e(TAG, DESTROY + " error " + e);
            e.printStackTrace();
        }
    }
}

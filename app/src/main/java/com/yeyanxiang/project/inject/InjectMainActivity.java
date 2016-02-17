package com.yeyanxiang.project.inject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;

import com.yeyanxiang.project.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

/**
 * Created by yezi on 16-2-17.
 */
public class InjectMainActivity extends Activity {
    private static final String TAG = "InjectMainActivity";
    private String apkPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inject_main);
        copyFile();
    }

    private void copyFile() {
        try {
            InputStream in = getAssets().open("app-debug.apk");
            if (in != null) {
                File appFile = new File(getFilesDir() + "/app.apk");
                if (appFile.exists()) appFile.delete();
                FileOutputStream out = openFileOutput("app.apk", MODE_PRIVATE);
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
                out.flush();
                Log.i(TAG, "copyFile success " + appFile.getAbsolutePath());
                apkPath = appFile.getAbsolutePath();
            } else {
                Log.e(TAG, "copyFile fail ");
            }
        } catch (IOException e) {
            Log.e(TAG, "copyFile fail " + e);
            e.printStackTrace();
        }
    }

    public void mode1(View view) {
        File dexOutputDir = getDir("dex", MODE_PRIVATE);
        Log.i(TAG, "loadApk dexOutputDir " + dexOutputDir.getAbsolutePath());
        DexClassLoader dLoader = new DexClassLoader(apkPath, dexOutputDir.getAbsolutePath(), dexOutputDir.getAbsolutePath(), getClassLoader());
        loadApkClassLoader(dLoader);

        try {
            Class<?> clazz = dLoader.loadClass("com.cnbn.android_test2.PassiveMainActivity");
            Intent intent = new Intent(InjectMainActivity.this, clazz);
            intent.putExtra("apkPath",apkPath);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void mode2(View view) {
        startActivity(new Intent(this, InjectProxyMainActivity.class));
    }

    public void listActivity() {
        PackageInfo packageInfo = getPackageManager().getPackageArchiveInfo(apkPath, 1);
        if ((packageInfo.activities != null) && (packageInfo.activities.length > 0)) {
            for (ActivityInfo activityInfo : packageInfo.activities) {
                Log.i(TAG, "onCreate app activity : " + activityInfo);
            }
        }
    }

    @SuppressLint("NewApi")
    private void loadApkClassLoader(DexClassLoader dLoader) {
        try {
            // 配置动态加载环境
            Object currentActivityThread = RefInvoke.invokeStaticMethod(
                    "android.app.ActivityThread", "currentActivityThread",
                    new Class[]{}, new Object[]{});//获取主线程对象 http://blog.csdn.net/myarrow/article/details/14223493
            String packageName = this.getPackageName();//当前apk的包名
            ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldOjbect(
                    "android.app.ActivityThread", currentActivityThread,
                    "mPackages");
            WeakReference wr = (WeakReference) mPackages.get(packageName);
            RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",
                    wr.get(), dLoader);

            Log.i(TAG, "loadApkClassLoader success " + dLoader);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "loadApkClassLoader error " + e);
        }
    }

    private Field getField(Class<?> cls, String name) {
        for (Field field : cls.getDeclaredFields()) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }


    /**
     * 以下是一种方式实现的
     *
     * @param loader
     */
    private void inject(DexClassLoader loader) {
        PathClassLoader pathLoader = (PathClassLoader) getClassLoader();

        try {
            Object dexElements = combineArray(
                    getDexElements(getPathList(pathLoader)),
                    getDexElements(getPathList(loader)));
            Object pathList = getPathList(pathLoader);
            setField(pathList, pathList.getClass(), "dexElements", dexElements);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Object getPathList(Object baseDexClassLoader)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        ClassLoader bc = (ClassLoader) baseDexClassLoader;
        return getField(baseDexClassLoader, Class.forName("dalvik.system.BaseDexClassLoader"), "pathList");
    }

    private static Object getField(Object obj, Class<?> cl, String field)
            throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        return localField.get(obj);
    }

    private static Object getDexElements(Object paramObject)
            throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        return getField(paramObject, paramObject.getClass(), "dexElements");
    }

    private static void setField(Object obj, Class<?> cl, String field,
                                 Object value) throws NoSuchFieldException,
            IllegalArgumentException, IllegalAccessException {

        Field localField = cl.getDeclaredField(field);
        localField.setAccessible(true);
        localField.set(obj, value);
    }

    private static Object combineArray(Object arrayLhs, Object arrayRhs) {
        Class<?> localClass = arrayLhs.getClass().getComponentType();
        int i = Array.getLength(arrayLhs);
        int j = i + Array.getLength(arrayRhs);
        Object result = Array.newInstance(localClass, j);
        for (int k = 0; k < j; ++k) {
            if (k < i) {
                Array.set(result, k, Array.get(arrayLhs, k));
            } else {
                Array.set(result, k, Array.get(arrayRhs, k - i));
            }
        }
        return result;
    }


}


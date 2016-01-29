package com.yeyanxiang.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.yeyanxiang.project.config.Config;
import com.yeyanxiang.project.fragment.BaseFragment;
import com.yeyanxiang.util.SharedPfsUtil;
import com.yeyanxiang.util.gitv.KeySoundHelper;
import com.yeyanxiang.util.gitv.LogUtils;
import com.yeyanxiang.util.gitv.ViewUtils;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Created by yeyanxiang on 15-4-22.
 */
public abstract class BaseActivity extends FragmentActivity {

    protected boolean isPause = false;
    protected FragmentManager mFragmentManager;
    protected SharedPfsUtil mSharedPfsUtil;
    protected Handler mMainHandler = new Handler(Looper.getMainLooper());

    public abstract String getTAG();

    public void logi(String log) {
        LogUtils.logi(getTAG(), log);
    }

    public void logi(Object msgObj) {
        LogUtils.logi(getTAG(), msgObj.toString());
    }

    public void loge(String log) {
        LogUtils.loge(getTAG(), log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logi("onCreate " + savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mFragmentManager = getSupportFragmentManager();
        mSharedPfsUtil = new SharedPfsUtil(this, Config.USER_DATA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
        logi("onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        logi("onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        logi("onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
        logi("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        logi("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        logi("onDestroy");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        logi("onNewIntent");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        logi("onWindowFocusChanged");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
        logi("onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        logi("onRestoreInstanceState");
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        logi("dispatchKeyEvent " + event.getKeyCode() + " action: " + event.getAction() + " curFocus " + getCurrentFocus());
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            KeySoundHelper.sounding(event.getKeyCode());
        }
        return super.dispatchKeyEvent(event);
    }

    public boolean isShown(View view) {
        return ViewUtils.isVisible(view);
    }

    protected boolean isShow(BaseFragment fragment) {
        return fragment != null && !fragment.isHidden();
    }

    protected boolean isHide(BaseFragment fragment) {
        return fragment != null && fragment.isHidden();
    }

    protected void showFragment(BaseFragment fragment, int enter_anim_id) {
        if (isHide(fragment)) {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(enter_anim_id, 0);
            mFragmentTransaction.show(fragment);
            mFragmentTransaction.commitAllowingStateLoss();
        } else {
            loge(fragment.getTAG() + " is already show");
        }
    }

    protected void hideFragment(BaseFragment fragment, int exit_anim_id) {
        if (isShow(fragment)) {
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.setCustomAnimations(0, exit_anim_id);
            mFragmentTransaction.hide(fragment);
            mFragmentTransaction.commitAllowingStateLoss();
        } else {
            loge(fragment.getTAG() + " is already hiden");
        }
    }

    public void initSize() {
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        if (width > 0) Config.screenWidth = width;
        if (height > 0) Config.screenHeight = height;
        Config.screenDensity = density;
        Config.screenDensityDpi = densityDpi;
        Config.resolution = width + "*" + height;
        logi("width " + width + " height " + height + " density " + density + " densityDpi " + densityDpi);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logi("onActivityResult requestCode " + requestCode + " resultCode " + resultCode + " data " + ReflectionToStringBuilder.toString(data));
    }

    protected void onEvent(Object event) {
        logi("onEvent " + event);
    }
}

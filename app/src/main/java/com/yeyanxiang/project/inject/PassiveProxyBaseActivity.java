package com.yeyanxiang.project.inject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by yezi on 16-2-17.
 */
@SuppressLint("MissingSuperCall")
public class PassiveProxyBaseActivity extends Activity {
    protected Activity mProxyActivity;

    public void setProxy(Activity proxyActivity) {
        mProxyActivity = proxyActivity;
    }

    protected Context getContext() {
        return mProxyActivity;
    }

    protected Activity getActivity() {
        return mProxyActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (mProxyActivity == null) super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        if (mProxyActivity == null) super.onResume();
    }

    @Override
    protected void onStart() {
        if (mProxyActivity == null) super.onStart();
    }

    @Override
    protected void onPause() {
        if (mProxyActivity == null) super.onPause();
    }

    @Override
    protected void onStop() {
        if (mProxyActivity == null) super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mProxyActivity == null) super.onDestroy();
    }

    @Override
    public void setContentView(View view) {
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(view);
        } else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mProxyActivity != null) {
            mProxyActivity.setContentView(layoutResID);
        } else {
            super.setContentView(layoutResID);
        }
    }

    @Override
    public View findViewById(int id) {
        if (mProxyActivity == null) {
            return super.findViewById(id);
        } else {
            return mProxyActivity.findViewById(id);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        if (mProxyActivity != null) {
            mProxyActivity.startActivity(intent);
        } else {
            super.startActivity(intent);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (mProxyActivity != null) {
            mProxyActivity.setTitle(title);
        } else {
            super.setTitle(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (mProxyActivity != null) {
            mProxyActivity.setTitle(titleId);
        } else {
            super.setTitle(titleId);
        }
    }
}

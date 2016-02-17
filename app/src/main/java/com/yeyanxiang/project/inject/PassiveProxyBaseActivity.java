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
    }

    @Override
    protected void onResume() {
    }

    @Override
    protected void onStart() {
    }

    @Override
    protected void onPause() {
    }

    @Override
    protected void onStop() {
    }

    @Override
    protected void onDestroy() {
    }

    @Override
    public void setContentView(View view) {
        if (mProxyActivity != null) mProxyActivity.setContentView(view);
    }

    @Override
    public void setContentView(int layoutResID) {
        if (mProxyActivity != null) mProxyActivity.setContentView(layoutResID);
    }

    @Override
    public View findViewById(int id) {
        return mProxyActivity == null ? null : mProxyActivity.findViewById(id);
    }

    @Override
    public void startActivity(Intent intent) {
        if (mProxyActivity != null) mProxyActivity.startActivity(intent);
    }
}

package com.yeyanxiang.project.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;

import com.yeyanxiang.project.config.Config;
import com.yeyanxiang.util.SharedPfsUtil;
import com.yeyanxiang.util.gitv.LogUtils;

/**
 * Created by yeyanxiang on 15-4-29.
 */
public abstract class BaseFragment extends Fragment {
    public static final int STATUS_NORMAL = 1;
    public static final int STATUS_ERROR = 2;
    public static final int STATUS_NOLOGIN = 3;

    protected ViewGroup mViewGroup;
    protected boolean isPause = false;
    protected FragmentManager mFragmentManager;
    protected Handler mMainHandler = new Handler(Looper.getMainLooper());
    protected SharedPfsUtil mSharedPfsUtil;

    public abstract String getTAG();

    public abstract int getFlag();

    public void logi(String log) {
        LogUtils.logi(getTAG(), log);
    }

    public void logi(Object msgObj) {
        LogUtils.logi(getTAG(), msgObj.toString());
    }

    public void loge(String log) {
        LogUtils.loge(getTAG(), log);
    }

    public BaseFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logi("onCreate");
        mSharedPfsUtil = new SharedPfsUtil(getActivity(), Config.USER_DATA);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logi("onCreateView");
        mFragmentManager = getChildFragmentManager();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        logi("onCreateAnimation");
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        logi("onContextItemSelected");
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        logi("onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logi("onActivityCreated");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        logi("onActivityResult");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        logi("onAttach");
    }

    @Override
    public void onResume() {
        super.onResume();
        logi("onResume");
        isPause = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        logi("onPause");
        isPause = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        logi("onStart");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        logi("onSaveInstanceState");
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        logi("onViewStateRestored");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logi("onViewCreated");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        logi("onDetach");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        logi("onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logi("onDestroy");

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        logi("onHiddenChanged " + hidden);
    }


    protected boolean isShow(BaseFragment fragment) {
        return fragment != null && !fragment.isHidden();
    }

    protected boolean isHide(BaseFragment fragment) {
        return fragment != null && fragment.isHidden();
    }

    protected boolean isShow(View view) {
        return (view != null && view.getVisibility() == View.VISIBLE);
    }

    protected boolean isHide(View view) {
        return view.getVisibility() != View.VISIBLE;
    }

    protected void showFragment(BaseFragment fragment, int enter_anim_id) {
        try {
            if (isHide(fragment)) {
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.setCustomAnimations(enter_anim_id, 0);
                mFragmentTransaction.show(fragment);
                mFragmentTransaction.commitAllowingStateLoss();
            } else {
                loge(fragment.getTAG() + " is already show");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void hideFragment(BaseFragment fragment, int exit_anim_id) {
        try {
            if (isShow(fragment)) {
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.setCustomAnimations(0, exit_anim_id);
                mFragmentTransaction.hide(fragment);
                mFragmentTransaction.commitAllowingStateLoss();
            } else {
                if (fragment != null) loge(fragment.getTAG() + " is already hiden");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final boolean isShow() {
        return !isHidden();
    }

    protected void onEvent(Object event) {
        logi("onEvent " + event);
    }

}

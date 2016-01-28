package com.yeyanxiang.util.gitv;

import android.view.View;
import android.view.animation.Animation;

/**
 * Created by lizhi on 15-5-22.
 */
public final class ViewUtils {

    private ViewUtils() {
        throw new UnsupportedOperationException("Cant be instantiated");
    }

    public static void setVisibility(final View view, final int visibility) {
        if (view != null) {
            view.setVisibility(visibility);
        }
    }

    public static boolean isVisible(final View view) {
        return (view != null && view.getVisibility() == View.VISIBLE);
    }

    public static void hideViewForAnim(final View view, Animation anim, final int visible) {
        if (view == null) return;
        view.clearAnimation();
        if (view.getVisibility() != View.VISIBLE) return;
        if (anim == null) {
            view.setVisibility(visible);
            return;
        }
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LogUtils.logi("ViewUtils", view + " hide onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtils.logi("ViewUtils", view + " hide onAnimationEnd");
                view.setVisibility(visible);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                LogUtils.logi("ViewUtils", view + " hide onAnimationRepeat");
            }
        });

        view.startAnimation(anim);
    }

    public static void showViewForAnim(final View view, Animation anim) {
        if (view == null) return;
        view.clearAnimation();
        if (view.getVisibility() == View.VISIBLE) return;
        view.setVisibility(View.VISIBLE);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                LogUtils.logi("ViewUtils", view + " show onAnimationStart");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                LogUtils.logi("ViewUtils", view + " show onAnimationEnd");
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                LogUtils.logi("ViewUtils", view + " show onAnimationRepeat");
            }
        });
        view.startAnimation(anim);
    }
}

package com.yeyanxiang.util.gitv;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

public final class AnimUtils {

    private static final float SCALE_BIG = 1.1f;
    private static final float SCALE_MIDDLE = 1.05f;
    private static final float SCALE_SMALL = 1.0f;
    private static final int ANIM_SCALE_DURATION = 400;

    private AnimUtils() {
    }

    public static void zoom(View v, boolean zoomIn) {
        if (zoomIn) {
            zoomIn(v);
        } else {
            zoomOut(v);
        }
    }

    public static void zoomIn(View v) {
        zoomIn(v, ANIM_SCALE_DURATION);
    }

    public static void zoomOut(View v) {
        zoomOut(v, ANIM_SCALE_DURATION);
    }

    public static void zoomIn(View v, int duration) {
        scaleXY(v, duration, SCALE_SMALL, SCALE_BIG);
    }

    public static void zoomOut(View v, int duration) {
        scaleXY(v, duration, SCALE_MIDDLE, SCALE_SMALL);
    }

    private static void scaleXY(View v, int duration, float... scaleValues) {
        final ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "scaleX", scaleValues);
        final ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "scaleY", scaleValues);

        final AnimatorSet set = new AnimatorSet();
        set.setDuration(duration).playTogether(animatorX, animatorY);
        set.start();
    }
}

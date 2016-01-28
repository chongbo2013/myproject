package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.os.PowerManager;

/**
 * Created by yezi on 15-12-4.
 */

public class WakeHelper {
    private static PowerManager.WakeLock wakeLock;

    public static void keepWake(Context context) {
        LogUtils.logi("WakeHelper", "keepWake");
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "cinema");
        }
        wakeLock.acquire();
    }

    public static void releaseWake(Context context) {
        LogUtils.logi("WakeHelper", "releaseWake");
        if (wakeLock == null) {
            PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "cinema");
        }
        wakeLock.release();
    }

    private static void setLockPatternEnabled(Context context, boolean enabled) {
        android.provider.Settings.Secure.putInt(context.getContentResolver(), android.provider.Settings.Secure.LOCK_PATTERN_ENABLED, enabled ? 1 : 0);
    }
}

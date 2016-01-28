package com.yeyanxiang.util.gitv;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by yeyanxiang on 15-9-18.
 */
public class Utils {

    public static boolean isServiceRunning(Context context, Class<?> checkRunService) {
        LogUtils.logi("Utils", "checkServiceRunning " + checkRunService);
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            LogUtils.logi("Utils", "runingService: " + service.service.getClassName());
            if (checkRunService.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}

package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;

public class NetUtils {
    private static final String TAG = "NetUtils";
    public static final int NONE = -1;
    public static final int DISCONNECT = 0;
    public static final int ETHERNET = 1;
    public static final int WIFI = 5;
    public static final int PPPOE = 6;

    private static long preRxBytes = 0;

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cwjManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    public static int getConnectState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        LogUtils.logi(TAG, "received network change !");
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.isConnected()) {
            int type = activeNetInfo.getType();
            LogUtils.logi(TAG, "received network type:" + type);
            switch (type) {
                case ConnectivityManager.TYPE_ETHERNET:
                    LogUtils.logi(TAG, "有线连接成功");
                    return ETHERNET;
                case ConnectivityManager.TYPE_WIFI:
                    LogUtils.logi(TAG, "WiFi 已连接成功");
                    return WIFI;
                case 14:  //PPPOE
                    LogUtils.logi(TAG, "PPPOE 连接成功");
                    return PPPOE;
                default:
                    return NONE;
            }
        } else {
            if (activeNetInfo == null) {
                LogUtils.loge(TAG, "activeNetInfo null");
            } else {
                LogUtils.loge(TAG, "网络已断开");
            }
            return DISCONNECT;
        }
    }

    public static int getNetSpeed(Context context) {
        long curRxBytes = getNetworkRxBytes(context);
        long bytes = curRxBytes - preRxBytes;
        preRxBytes = curRxBytes;
        int kb = (int) Math.floor(bytes / 1024 + 0.5);
        LogUtils.logi(TAG, "curNetSpeed " + kb + " k/s");
        return kb;
    }


    private static long getNetworkRxBytes(Context context) {
        int currentUid = getUid(context);
        LogUtils.logd(TAG, "currentUid =" + currentUid);
        if (currentUid < 0) {
            return 0;
        }
        long rxBytes = TrafficStats.getUidRxBytes(currentUid);
        /* 下句中if里的一般都为真，只能得到全部的网速 */
        if (rxBytes == TrafficStats.UNSUPPORTED) {
            LogUtils.logd(TAG, "getUidRxBytes fail !!!");/* 本函数可以只用下面一句即可 */
            rxBytes = TrafficStats.getTotalRxBytes();
        }
        return rxBytes;
    }

    private static int getUid(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return ai.uid;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}

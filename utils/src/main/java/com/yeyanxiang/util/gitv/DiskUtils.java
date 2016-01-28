package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class DiskUtils {

    public static String getDownloadPath(Context context) {
        return getDownloadDocument(context) + getApkName(context);
    }

    public static String getApkName(Context context) {
        return context.getPackageName() + ".apk";
    }

    public static String getDownloadDocument(Context context) {
        return context.getFilesDir().getPath() + "/";
    }

    /**
     * 外部存储是否可用
     *
     * @return
     */
    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 外部存储是否已满
     *
     * @return
     */
    public static boolean externalMemoryFull() {
        return false;
    }

    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

}

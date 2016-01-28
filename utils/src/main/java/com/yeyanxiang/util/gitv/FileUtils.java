package com.yeyanxiang.util.gitv;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by yeyanxiang on 15-8-20.
 */
public class FileUtils {
    private static final String TAG = "FileUtils";

    public static boolean checkMd5(File file, String md5) throws IOException {
        if (file.exists()) {
            if (MD5Utils.encodeFileMD5(file.getAbsolutePath()).equals(md5)) {
                LogUtils.logi(TAG, "md5 Agreement");
                return true;
            } else {
                file.delete();
                LogUtils.logi(TAG, "md5 Inconsistent , already delete.");
                return false;
            }
        } else {
            LogUtils.logi(TAG, "file not exists");
            return false;
        }
    }

    /**
     * 判断是否存在目录，不存在就创建
     *
     * @param path
     */
    public static boolean makeDirs(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static void delFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void setAuthData(Object obj, Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("auth.ser", Context.MODE_WORLD_READABLE);
            ObjectOutputStream ostream = new ObjectOutputStream(fos);
            ostream.writeObject(obj);
            ostream.flush();
            ostream.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object getAuthData(Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput("auth.ser");
            ObjectInputStream s = new ObjectInputStream(fis);
            Object obj = s.readObject();
            s.close();
            fis.close();
            return obj;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}

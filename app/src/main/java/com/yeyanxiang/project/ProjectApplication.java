package com.yeyanxiang.project;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yeyanxiang.util.gitv.KeySoundHelper;
import com.yeyanxiang.util.gitv.LogUtils;

/**
 * Created by yezi on 16-1-29.
 */
public class ProjectApplication extends Application {
    private final String TAG = "ProjectApplication";
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.logi(TAG, "onCreate");
        mContext = getApplicationContext();
        initImageLoader(getApplicationContext());
        initKeySound();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        LogUtils.logi(TAG, "onTerminate");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogUtils.logi(TAG, "onConfigurationChanged");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtils.logi(TAG, "onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.logi(TAG, "onTrimMemory");
    }

    // 初始化ImageLoader
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .discCacheSize(10 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

    private void initKeySound() {
        KeySoundHelper.loadSoundResources(getApplicationContext());
    }

    public static Context getContext() {
        return mContext;
    }
}

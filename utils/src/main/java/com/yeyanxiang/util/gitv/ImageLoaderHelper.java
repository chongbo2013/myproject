/*
 * Copyright (c) 2015 银河互联网电视有限公司. All rights reserved.
 */

package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public final class ImageLoaderHelper {
    private static final int DEFAULT_LOADING_IMG = 0;
    private static final int DEFAULT_DELAY_BEFORE_LOADING = 100;
    private static final int TAG_KEY_URI = 0x7FFFFFFF;

    private static DisplayImageOptions sDisplayImageOptions;

    private ImageLoaderHelper() {}

    public static void init(Context context) {
        if (getInstance().isInited()) {
            getInstance().destroy();
        }

        sDisplayImageOptions = new DisplayImageOptions.Builder()
                .delayBeforeLoading(DEFAULT_DELAY_BEFORE_LOADING)
//                .showImageOnLoading(DEFAULT_LOADING_IMG)
                .cacheInMemory(true)
                .build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .defaultDisplayImageOptions(sDisplayImageOptions)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .threadPoolSize(4)
                .build();

        getInstance().init(config);
        getInstance().handleSlowNetwork(true);
    }

    public static void pause() {
        getInstance().pause();
    }

    public static void resume() {
        getInstance().resume();
    }

    public static void displayImage(String uri, ImageView imageView, DisplayImageOptions options) {
        getInstance().displayImage(uri, imageView, options);
    }

    public static void displayImage(String uri, ImageView imageView) {
        // FIXME fix images flicker
        final String loadingUriForView = (String) imageView.getTag(TAG_KEY_URI);
        if (!TextUtils.equals(uri, loadingUriForView)) {
            getInstance().cancelDisplayTask(imageView);
            getInstance().displayImage(uri, imageView);
            imageView.setTag(TAG_KEY_URI, uri);
        }
    }

    public static void cancelDisplayTask(ImageView imageView) {
        getInstance().cancelDisplayTask(imageView);
    }

    private static ImageLoader getInstance() {
        return ImageLoader.getInstance();
    }
}

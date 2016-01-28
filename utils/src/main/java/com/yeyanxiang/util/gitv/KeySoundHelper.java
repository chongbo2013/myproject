/*
 * Copyright (c) 2015 银河互联网电视有限公司. All rights reserved.
 */

package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseArray;
import android.view.KeyEvent;

import com.yeyanxiang.util.R;

public final class KeySoundHelper {
    private static final float LEFT_VOLUME = 1;
    private static final float RIGHT_VOLUME = 1;
    private static final int PRIORITY = 0;
    private static final int LOOP = 0;
    private static final float RATE = 1;
    private static final int AUDIO_QUALITY = 0;

    private static final int RESOURCES_SIZE = 11;
    private static SoundPool sSoundPool;
    private static SparseArray<Integer> sKeySoundResMap;
    private static SparseArray<Integer> sKeySoundResIdMap = new SparseArray<Integer>(RESOURCES_SIZE);

    static {
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_DPAD_LEFT, R.raw.left);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_DPAD_RIGHT, R.raw.right);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_DPAD_UP, R.raw.left);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_DPAD_DOWN, R.raw.right);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_DPAD_CENTER, R.raw.ok);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_ENTER, R.raw.ok);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_BACK, R.raw.back);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_MENU, R.raw.menu);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_HOME, R.raw.home);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_VOLUME_UP, R.raw.vol_up);
        sKeySoundResIdMap.put(KeyEvent.KEYCODE_VOLUME_DOWN, R.raw.vol_down);
    }

    private KeySoundHelper() {
        throw new UnsupportedOperationException();
    }

    public static void loadSoundResources(Context context) {
        if (context != null) {
            releaseSoundResources();
            Traces.d("start to load sound resources");
            synchronized (KeySoundHelper.class) {
                sSoundPool = new SoundPool(RESOURCES_SIZE, AudioManager.STREAM_MUSIC, AUDIO_QUALITY);
                sKeySoundResMap = new SparseArray<Integer>(RESOURCES_SIZE);
                for (int i = 0; i < RESOURCES_SIZE; i++) {
                    final int key = sKeySoundResIdMap.keyAt(i);
                    final int soundResId = sKeySoundResIdMap.get(key, 0);
                    if (soundResId > 0) {
                        sKeySoundResMap.put(key, loadSoundRes(sSoundPool, context, soundResId));
                    }
                }
            }
        }
    }

    public static void releaseSoundResources() {
        if (sSoundPool != null) {
            sSoundPool.release();
            sSoundPool = null;
        }
        if (sKeySoundResMap != null) {
            sKeySoundResMap.clear();
            sKeySoundResMap = null;
        }
    }

    private static int loadSoundRes(SoundPool soundPool, Context context, int resId) {
        if (soundPool != null && context != null && resId != 0) {
            return soundPool.load(context, resId, 0);
        }
        return 0;
    }

    private static void playSoundBy(int keyCode) {
        final int soundId = sKeySoundResMap.get(keyCode, 0);
        if (soundId > 0) {
            sSoundPool.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
        }
    }

    private static void checkSoundResources() {
        if (sSoundPool == null) {
            throw new NullPointerException("Sound resources not loaded");
        }
    }

    public static void sounding(int keyCode) {
//        Traces.d("keyCode is %s", keyCode);
        checkSoundResources();
        playSoundBy(keyCode);
    }
}

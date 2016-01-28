package com.yeyanxiang.util.gitv;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by yeyanxiang on 15-9-24.
 */
public class WindowsUtils {
    private static WindowManager mWindowManager;
    private static View mUpgradeView;

    public static void showUpgradeWindow(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        //设置window type
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        //wmParams.flags = WindowManager.LayoutParams.
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.windowAnimations = 0;
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        if (mUpgradeView == null) {
            mUpgradeView = View.inflate(context, 0, null);
        }

        //添加mFloatLayout
        mWindowManager.addView(mUpgradeView, wmParams);
    }

    public static void hideUpgradeWindow() {
        if (mWindowManager != null && mUpgradeView != null) {
            mWindowManager.removeView(mUpgradeView);
            mUpgradeView = null;
        }
    }

}

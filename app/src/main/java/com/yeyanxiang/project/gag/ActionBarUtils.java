package com.yeyanxiang.project.gag;

import android.app.Activity;
import android.view.View;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年6月26日
 * 
 * @简介
 */
public class ActionBarUtils {
    public static View findActionBarContainer(Activity activity) {
        int id = activity.getResources().getIdentifier("action_bar_container", "id", "android");
        return activity.findViewById(id);
    }

    public static View findSplitActionBar(Activity activity) {
        int id = activity.getResources().getIdentifier("split_action_bar", "id", "android");
        return activity.findViewById(id);
    }
}

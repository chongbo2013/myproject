package com.yeyanxiang.project.gag;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yeyanxiang.project.ProjectApplication;

/**
 * @author 叶雁翔
 * @version 1.0
 * @Email yanxiang1120@gmail.com
 * @update 2014年6月26日
 * @简介
 */
public class RequestManager {
    public static RequestQueue mRequestQueue = Volley.newRequestQueue(ProjectApplication
            .getContext());

    private RequestManager() {
        // no instances
    }

    public static void addRequest(Request<?> request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    public static void cancelAll(Object tag) {
        mRequestQueue.cancelAll(tag);
    }
}

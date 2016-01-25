package com.yeyanxiang.project.gag;

import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

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
public abstract class BaseFragment extends Fragment {

	@Override
	public void onDestroy() {
		super.onDestroy();
		RequestManager.cancelAll(this);
	}

	protected void executeRequest(Request request) {
		RequestManager.addRequest(request, this);
	}

	protected Response.ErrorListener errorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				ToastUtils.showLong(error.getMessage());
			}
		};
	}
}

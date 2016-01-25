package com.yeyanxiang.project.referencedemo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.HorizontalScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshHorizontalScrollView;
import com.yeyanxiang.project.R;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年4月10日
 * 
 * @简介
 */
public final class PullToRefreshHorizontalScrollViewActivity extends Activity {

	PullToRefreshHorizontalScrollView mPullRefreshScrollView;
	HorizontalScrollView mScrollView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ptr_horizontalscrollview);

		mPullRefreshScrollView = (PullToRefreshHorizontalScrollView) findViewById(R.id.pull_refresh_horizontalscrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<HorizontalScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<HorizontalScrollView> refreshView) {
						new GetDataTask().execute();
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(String[] result) {
			// Do some stuff here

			// Call onRefreshComplete when the list has been refreshed.
			mPullRefreshScrollView.onRefreshComplete();

			super.onPostExecute(result);
		}
	}

}

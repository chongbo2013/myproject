package com.yeyanxiang.project.reference;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.pullrefresh.PullListView;
import com.yeyanxiang.view.pullrefresh.PullListViewListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月13日
 * 
 * @简介
 */
public class PullListActivity extends Activity implements PullListViewListener {
	private List<String> mStrings = new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;
	private PullListView pullListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pulllist);

		pullListView = (PullListView) findViewById(R.id.pulllistview);
		pullListView.setPullLoadEnable(true);
		pullListView.setPullRefreshEnable(true);
		pullListView.setPullListViewListener(this);

		mAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1,
				mStrings);
		pullListView.setAdapter(mAdapter);

		loadData();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pullListView.RefreshFinish();
			}
		}, 1000);
	}

	private void loadData() {
		for (int i = 0; i < 15; i++) {
			mStrings.add(i + "");
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mStrings.add(0, new Date().toLocaleString());
				mAdapter.notifyDataSetChanged();
				pullListView.RefreshFinish();
			}
		}, 1000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mStrings.add(new Date().toLocaleString());
				mAdapter.notifyDataSetChanged();
				pullListView.LoadMoreFinish(false);
			}
		}, 1000);
	}

}

package com.yeyanxiang.project.reference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年4月3日
 * 
 * @简介
 */
public class ReferenceActivity extends Activity implements
		SwipeRefreshLayout.OnRefreshListener {

	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private List<String> mStrings = new ArrayList<String>();
	private ArrayAdapter<String> mAdapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.referencelayout);

		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(this);
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);

		mAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1,
				mStrings);
		listView = (ListView) findViewById(R.id.refenencelist);
		listView.setAdapter(mAdapter);
		loadData();
	}

	public void onRefresh() {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				swipeLayout.setRefreshing(false);
				mStrings.add(0, new Date().toLocaleString());
				mAdapter.notifyDataSetChanged();
			}
		}, 1000);
	}

	private void loadData() {
		for (int i = 0; i < 15; i++) {
			mStrings.add(i + "");
		}
		mAdapter.notifyDataSetChanged();
	}
}

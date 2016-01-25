package com.yeyanxiang.project.tab;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.TestActivity1;
import com.yeyanxiang.project.pub.util.TestActivity2;
import com.yeyanxiang.project.pub.util.TestActivity3;
import com.yeyanxiang.project.pub.util.TestActivity4;
import com.yeyanxiang.view.FleepTabHost;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.widget.TabWidget;
import android.widget.TabHost.OnTabChangeListener;

/**
 * 
 * Create on 2013-5-7 上午10:29:40 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 可滑动的TAB
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class FleepTabActivity extends TabActivity implements
		OnTabChangeListener, OnGestureListener {
	private GestureDetector gestureDetector;
	private FleepTabHost tabHost;
	private TabWidget tabWidget;

	private static final int FLEEP_DISTANCE = 50;

	/** 记录当前分页ID */
	private int currentTabID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabfleep);

		tabHost = (FleepTabHost) findViewById(android.R.id.tabhost);
		// tabWidget = (TabWidget) findViewById(android.R.id.tabs);
		tabHost.setOnTabChangedListener(this);

		init();

		gestureDetector = new GestureDetector(this);
	}

	private void init() {
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("Tab1",
						getResources().getDrawable(R.drawable.icon))
				.setContent(new Intent(this, TestActivity1.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("Tab1",
						getResources().getDrawable(R.drawable.icon))
				.setContent(new Intent(this, TestActivity2.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("Tab1",
						getResources().getDrawable(R.drawable.icon))
				.setContent(new Intent(this, TestActivity3.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("Tab1",
						getResources().getDrawable(R.drawable.icon))
				.setContent(new Intent(this, TestActivity4.class)));
	}

	@Override
	public void onTabChanged(String tabId) {
		// tabId值为要切换到的tab页的索引位置
		// int tabID = Integer.valueOf(tabId);
		// for (int i = 0; i < tabWidget.getChildCount(); i++) {
		// if (i == tabID) {
		// tabWidget.getChildAt(Integer.valueOf(i)).setBackgroundColor(
		// Color.BLUE);
		// } else {
		// tabWidget.getChildAt(Integer.valueOf(i)).setBackgroundColor(
		// Color.WHITE);
		// }
		// }
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() <= (-FLEEP_DISTANCE)) {// 从左向右滑动
			currentTabID = tabHost.getCurrentTab() - 1;
			if (currentTabID < 0) {
				currentTabID = tabHost.getTabCount() - 1;
			}
		} else if (e1.getX() - e2.getX() >= FLEEP_DISTANCE) {// 从右向左滑动
			currentTabID = tabHost.getCurrentTab() + 1;
			if (currentTabID >= tabHost.getTabCount()) {
				currentTabID = 0;
			}
		}
		tabHost.setCurrentTab(currentTabID);
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}
}

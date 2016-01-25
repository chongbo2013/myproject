package com.yeyanxiang.project.tab;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.NullActivity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;

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
public class MyTabActivity5 extends TabActivity {
	private Context context;
	private TabHost tabHost;
	private RadioGroup group;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab5);

		context = MyTabActivity5.this;
		tabHost = getTabHost();

		intent = new Intent(context, MyTabActivity6.class);
		tabHost.addTab(tabHost.newTabSpec("politicsnew").setIndicator("时政要闻")
				.setContent(intent));

		intent = new Intent(context, NullActivity.class);
		tabHost.addTab(tabHost.newTabSpec("departnew").setIndicator("部门新闻")
				.setContent(intent));

		tabHost.addTab(tabHost.newTabSpec("activitysub").setIndicator("活动专题")
				.setContent(intent));

		tabHost.addTab(tabHost.newTabSpec("greatproject").setIndicator("重大项目")
				.setContent(intent));

		group = (RadioGroup) findViewById(R.id.currenttb);
		group.check(R.id.tab1);

		// 自定义tab标签单击事件
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.tab1:
					tabHost.setCurrentTabByTag("politicsnew");
					break;
				case R.id.tab2:
					tabHost.setCurrentTabByTag("departnew");
					break;
				case R.id.tab3:
					tabHost.setCurrentTabByTag("activitysub");
					break;
				case R.id.tab4:
					tabHost.setCurrentTabByTag("greatproject");
					break;
				default:
					break;
				}
			}
		});

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub

			}
		});
	}
}

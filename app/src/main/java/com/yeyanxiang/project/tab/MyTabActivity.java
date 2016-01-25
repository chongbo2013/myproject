package com.yeyanxiang.project.tab;

import com.yeyanxiang.project.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

/**
 * 
 * Create on 2013-5-7 上午10:30:03 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 自定义Tab风格
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class MyTabActivity extends TabActivity {
	private Context context;
	private TabHost tabHost;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		context = MyTabActivity.this;

		tabHost = getTabHost();
		intent = new Intent(context, MyTabActivity1.class);
		tabHost.addTab(tabHost
				.newTabSpec("tab1")
				.setIndicator("Tab1",
						getResources().getDrawable(R.drawable.icon))
				.setContent(intent));
		intent = new Intent(context, MyTabActivity2.class);
		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator("Tab2",
						getResources().getDrawable(R.drawable.icon))
				.setContent(intent));
		intent = new Intent(context, MyTabActivity5.class);
		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setIndicator("Tab3",
						getResources().getDrawable(R.drawable.icon))
				.setContent(intent));
	}

}

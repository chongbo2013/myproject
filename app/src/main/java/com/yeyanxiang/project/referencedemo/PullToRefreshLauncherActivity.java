package com.yeyanxiang.project.referencedemo;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
public class PullToRefreshLauncherActivity extends ListActivity {

	public static final String[] options = { "ListView", "ExpandableListView",
			"GridView", "WebView", "ScrollView", "Horizontal ScrollView",
			"ViewPager", "ListView Fragment", "WebView Advanced",
			"ListView in ViewPager" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, options));
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent;

		switch (position) {
		default:
		case 0:
			intent = new Intent(this, PullToRefreshListActivity.class);
			break;
		case 1:
			intent = new Intent(this, PullToRefreshExpandableListActivity.class);
			break;
		case 2:
			intent = new Intent(this, PullToRefreshGridActivity.class);
			break;
		case 3:
			intent = new Intent(this, PullToRefreshWebViewActivity.class);
			break;
		case 4:
			intent = new Intent(this, PullToRefreshScrollViewActivity.class);
			break;
		case 5:
			intent = new Intent(this,
					PullToRefreshHorizontalScrollViewActivity.class);
			break;
		case 6:
			intent = new Intent(this, PullToRefreshViewPagerActivity.class);
			break;
		case 7:
			intent = new Intent(this, PullToRefreshListFragmentActivity.class);
			break;
		case 8:
			intent = new Intent(this, PullToRefreshWebView2Activity.class);
			break;
		case 9:
			intent = new Intent(this,
					PullToRefreshListInViewPagerActivity.class);
			break;
		}

		startActivity(intent);
	}

}

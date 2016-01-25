package com.yeyanxiang.project.indicator;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.indicator.TitlePageIndicator;

public class SampleTitlesInitialPage extends BaseSampleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_titles);

		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (TitlePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		mIndicator.setCurrentItem(mAdapter.getCount() - 1);

		// You can also do: indicator.setViewPager(pager, initialPage);
	}
}
package com.yeyanxiang.project.indicator;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.yeyanxiang.view.indicator.UnderlinePageIndicator;
import com.yeyanxiang.project.R;

public class SampleUnderlinesNoFade extends BaseSampleActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simple_underlines);

		mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		UnderlinePageIndicator indicator = (UnderlinePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setFades(false);
		mIndicator = indicator;
	}
}
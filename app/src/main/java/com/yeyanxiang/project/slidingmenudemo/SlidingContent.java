package com.yeyanxiang.project.slidingmenudemo;

import com.yeyanxiang.project.R;

import android.os.Bundle;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月14日
 * 
 * @简介
 */
public class SlidingContent extends BaseActivity {

	public SlidingContent() {
		super(R.string.title_bar_content);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new SampleListFragment()).commit();

		setSlidingActionBarEnabled(false);
	}

}

package com.yeyanxiang.project.slidingmenudemo;

import android.os.Bundle;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.slidemenu.SlidingMenu;
import com.yeyanxiang.view.slidemenu.SlidingMenu.CanvasTransformer;

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
public abstract class CustomAnimation extends BaseActivity {

	private CanvasTransformer mTransformer;

	public CustomAnimation(int titleRes, CanvasTransformer transformer) {
		super(titleRes);
		mTransformer = transformer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// set the Above View
		setContentView(R.layout.content_frame);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new SampleListFragment()).commit();

		SlidingMenu sm = getSlidingMenu();
		setSlidingActionBarEnabled(true);
		sm.setBehindScrollScale(0.0f);
		sm.setBehindCanvasTransformer(mTransformer);
	}

}

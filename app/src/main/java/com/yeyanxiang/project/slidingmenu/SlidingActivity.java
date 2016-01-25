package com.yeyanxiang.project.slidingmenu;

import android.app.ActionBar;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Interpolator;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.slidemenu.SlidingFragmentActivity;
import com.yeyanxiang.view.slidemenu.SlidingMenu;
import com.yeyanxiang.view.slidemenu.SlidingMenu.CanvasTransformer;

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
public class SlidingActivity extends SlidingFragmentActivity {
	protected ActionBar actionBar;
	protected SlidingMenu slidingMenu;
	protected int screenwidth;
	protected int screenheight;

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// initActionBar();
		initSlidingMenu();
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		actionBar = getActionBar();
		// actionBar.hide();
	}

	private void initSlidingMenu() {
		// TODO Auto-generated method stub
		screenwidth = getWindowManager().getDefaultDisplay().getWidth();
		screenheight = getWindowManager().getDefaultDisplay().getHeight();
		slidingMenu = getSlidingMenu();
		slidingMenu.setBackgroundColor(Color.parseColor("#8B0000"));
		slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setBehindContentView(R.layout.left_menu);
		slidingMenu.setSecondaryMenu(R.layout.left_menu);
		slidingMenu.setBehindOffset(screenwidth * 3 / 7);
		slidingMenu.setBehindCanvasTransformer(new CanvasTransformer() {

			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				// TODO Auto-generated method stub
				// zoom
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);

				// scale
				// canvas.scale(percentOpen, 1, 0, 0);

				// slide
				// canvas.translate(
				// 0,
				// canvas.getHeight()
				// * (1 - interp.getInterpolation(percentOpen)));
			}
		});
	}
}
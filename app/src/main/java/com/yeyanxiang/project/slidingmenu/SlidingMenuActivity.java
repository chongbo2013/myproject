package com.yeyanxiang.project.slidingmenu;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.slidemenu.SlidingMenu;
import com.yeyanxiang.view.slidemenu.SlidingMenu.OnCloseListener;

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
public class SlidingMenuActivity extends SlidingActivity {
	private Button leftButton;
	private Button rightButton;
	private TextView topTextView;
	private ViewPager viewPager;
	private TitlePageIndicator indicator;
	private FragmentStatePagerAdapter pagerAdapter;
	private ImageView imgLeft;
	private ImageView imgRight;
	private List<ProductionListFragment> fragments;
	private int slidemode = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidingmain2);
		initViewPager();
	}

	private void initViewPager() {
		// TODO Auto-generated method stub
		// actionBar.setDisplayHomeAsUpEnabled(true);
		slidemode = SlidingMenu.LEFT;
		slidingMenu.setMode(slidemode);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);
		imgLeft = (ImageView) findViewById(R.id.imageview_above_left);
		imgRight = (ImageView) findViewById(R.id.imageview_above_right);

		leftButton = (Button) findViewById(R.id.left_title_btton);
		rightButton = (Button) findViewById(R.id.right_title_btton);
		topTextView = (TextView) findViewById(R.id.topTextView);
		leftButton.setText("Left");
		rightButton.setText("Right");
		topTextView.setText("SlideMenu");

		slidingMenu.setOnCloseListener(new OnCloseListener() {

			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				slidingMenu.setMode(slidemode);
			}
		});

		leftButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				slidemode = slidingMenu.getMode();
				slidingMenu.setMode(SlidingMenu.LEFT);
				toggle();
			}
		});
		rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				slidemode = slidingMenu.getMode();
				slidingMenu.setMode(SlidingMenu.RIGHT);
				secondtoggle();
			}
		});

		fragments = new ArrayList<ProductionListFragment>();
		fragments.add(new ProductionListFragment(null, "写真"));
		fragments.add(new ProductionListFragment(null, "油画"));
		fragments.add(new ProductionListFragment(null, "素描"));
		fragments.add(new ProductionListFragment(null, "色彩"));
		fragments.add(new ProductionListFragment(null, "视频"));
		fragments.add(new ProductionListFragment(null, "排行"));
		fragments.add(new ProductionListFragment(null, "收藏"));
		pagerAdapter = new FragmentStatePagerAdapter(
				getSupportFragmentManager()) {

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return fragments.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return fragments.get(arg0);
			}

			@Override
			public CharSequence getPageTitle(int position) {
				// TODO Auto-generated method stub
				return fragments.get(position).getTitle();
			}
		};
		viewPager.setOffscreenPageLimit(0);
		viewPager.setAdapter(pagerAdapter);
		indicator.setViewPager(viewPager);
		indicator.setOnPageChangeListener(new MyPageChangeListener());
	}

	class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if (arg0 == 0) {
				imgLeft.setVisibility(View.GONE);
				slidemode = SlidingMenu.LEFT;
				slidingMenu.setMode(slidemode);
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else if (arg0 == (pagerAdapter.getCount() - 1)) {
				imgRight.setVisibility(View.GONE);
				slidemode = SlidingMenu.RIGHT;
				slidingMenu.setMode(slidemode);
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
			} else {
				slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				imgRight.setVisibility(View.VISIBLE);
				imgLeft.setVisibility(View.VISIBLE);
			}
		}
	}
}

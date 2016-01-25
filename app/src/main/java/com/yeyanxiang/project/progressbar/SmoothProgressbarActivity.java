package com.yeyanxiang.project.progressbar;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.smoothprogressbar.SmoothProgressDrawable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年4月3日
 * 
 * @简介
 */
public class SmoothProgressbarActivity extends Activity {

	private ProgressBar mProgressBar1;
	private ProgressBar mProgressBar2;
	private ProgressBar mProgressBar3;
	private ProgressBar mProgressBar4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smoothprogressbarlayout);

		mProgressBar1 = (ProgressBar) findViewById(R.id.progressbar1);
		mProgressBar2 = (ProgressBar) findViewById(R.id.progressbar2);
		mProgressBar3 = (ProgressBar) findViewById(R.id.progressbar3);
		mProgressBar4 = (ProgressBar) findViewById(R.id.progressbar4);

		mProgressBar1
				.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(
						this).interpolator(new LinearInterpolator()).build());
		mProgressBar2
				.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(
						this).interpolator(new AccelerateInterpolator())
						.build());
		mProgressBar3
				.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(
						this).interpolator(new DecelerateInterpolator())
						.build());
		mProgressBar4
				.setIndeterminateDrawable(new SmoothProgressDrawable.Builder(
						this).interpolator(
						new AccelerateDecelerateInterpolator()).build());

		findViewById(R.id.button_make).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(
								SmoothProgressbarActivity.this,
								MakeCustomProgressbarActivity.class);
						startActivity(intent);
					}
				});
	}
}

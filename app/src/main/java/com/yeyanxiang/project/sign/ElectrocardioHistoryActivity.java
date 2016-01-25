package com.yeyanxiang.project.sign;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

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
public class ElectrocardioHistoryActivity extends Activity {
	private Context context;
	private GECGWaveFormhistory gecgWaveFormhistory;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		gecgWaveFormhistory = new GECGWaveFormhistory(context);
		setContentView(gecgWaveFormhistory);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		gecgWaveFormhistory.setVisibility(View.GONE);
	}
}

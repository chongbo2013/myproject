package com.yeyanxiang.project.sign;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
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
public class XueYangHistoryActivity extends Activity {
	private Context context;
	private PieBitmap weekBitmap;
	private PieBitmap monthBitmap;
	private PieBitmap threemonthBitmap;
	private PieBitmap sixmonthBitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context = this;
		setContentView(R.layout.xueyanghistory);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		weekBitmap = (PieBitmap) findViewById(R.id.xueyangweekhistory);
		monthBitmap = (PieBitmap) findViewById(R.id.xueyangmonthhistory);
		threemonthBitmap = (PieBitmap) findViewById(R.id.xueyangthreemonthhistory);
		sixmonthBitmap = (PieBitmap) findViewById(R.id.xueyangsixmonthhistory);

		float[] rates = new float[] { 10, 80, 10 };
		int[] colors = new int[] { Color.RED, Color.GREEN, Color.YELLOW };
		String[] descs = new String[] { "97%以下", "97%-98%", "98%以上" };

		weekBitmap.setData(rates, colors, descs);
		monthBitmap.setData(rates, colors, descs);
		threemonthBitmap.setData(rates, colors, descs);
		sixmonthBitmap.setData(rates, colors, descs);
	}
}

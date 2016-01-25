package com.yeyanxiang.project.pub.util;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

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
public class TestActivity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		getWindow().setBackgroundDrawableResource(R.drawable.black);
		TextView textView = (TextView) findViewById(R.id.testtext);
		textView.setText("TestActivity2");
		textView.setTextColor(Color.WHITE);
	}

}

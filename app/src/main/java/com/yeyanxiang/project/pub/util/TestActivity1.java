package com.yeyanxiang.project.pub.util;

import com.yeyanxiang.project.R;

import android.app.Activity;
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
public class TestActivity1 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);

		((TextView) findViewById(R.id.testtext)).setText("TestActivity1");

	}

}

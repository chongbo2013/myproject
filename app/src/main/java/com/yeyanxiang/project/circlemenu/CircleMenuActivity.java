package com.yeyanxiang.project.circlemenu;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.circleview.CircleImageView;
import com.yeyanxiang.view.circleview.CircleLayout;
import com.yeyanxiang.view.circleview.CircleLayout.OnItemClickListener;
import com.yeyanxiang.view.circleview.CircleLayout.OnItemSelectedListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * Create on 2013-5-7 上午10:11:21 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 旋转菜单
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class CircleMenuActivity extends Activity implements
		OnItemSelectedListener, OnItemClickListener {
	TextView selectedTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.circlemenu);
		CircleLayout circleMenu = (CircleLayout) findViewById(R.id.main_circle_layout);
		circleMenu.setOnItemSelectedListener(this);
		circleMenu.setOnItemClickListener(this);

		selectedTextView = (TextView) findViewById(R.id.main_selected_textView);
		selectedTextView.setText(((CircleImageView) circleMenu
				.getSelectedItem()).getName());
	}

	@Override
	public void onItemSelected(View view, int position, long id, String name) {
		selectedTextView.setText(name);
	}

	@Override
	public void onItemClick(View view, int position, long id, String name) {
		Toast.makeText(getApplicationContext(),
				getResources().getString(R.string.start_app) + " " + name,
				Toast.LENGTH_SHORT).show();
	}

}

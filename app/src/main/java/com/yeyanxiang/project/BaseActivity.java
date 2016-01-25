package com.yeyanxiang.project;

import com.yeyanxiang.view.util.ActivityUtil;
import com.yeyanxiang.view.marquee.MarqueeText;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月11日
 * 
 * @简介 baseActivity,Activity需要继承BaseActivity,
 *     oncreate方法下setcontentview后再用super.onCreate,布局文件中一定要包含activonbar
 */
public class BaseActivity extends Activity {
	public Activity activity;

	@Override
	public void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		initTopView();
		activity = this;
	}

	public Button left_title_btton;
	public Button right_title_btton;
	public MarqueeText center_title_text;

	public void initTopView() {
		// TODO Auto-generated method stub
		left_title_btton = (Button) findViewById(R.id.left_title_btton);
		right_title_btton = (Button) findViewById(R.id.right_title_btton);
		center_title_text = (MarqueeText) findViewById(R.id.topTextView);

		left_title_btton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				leftbuttonclick();
			}
		});

		right_title_btton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rightbuttonclick();
			}
		});
	}

	public void rightbuttonclick() {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	public void leftbuttonclick() {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	public void setTitle(String title) {
		center_title_text.setText(title);
	}

	public void setLefttext(String text) {
		left_title_btton.setText(text);
	}

	public void setRighttext(String text) {
		right_title_btton.setText(text);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ActivityUtil.loadbackanim(this);
	}
}

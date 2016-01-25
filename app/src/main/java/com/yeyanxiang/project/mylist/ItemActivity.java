package com.yeyanxiang.project.mylist;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class ItemActivity extends Activity implements OnTouchListener {

	private float x, upx;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.slidecutlist_itemlayout);
		init();
	}

	private void init() {
		findViewById(R.id.ll_parent).setOnTouchListener(this);
		((TextView) findViewById(R.id.tv_text)).setText(getIntent()
				.getStringExtra("item"));
	}

	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			x = event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			upx = event.getX();
			if (Math.abs(x - upx) > 20) {
				this.finish();
				overridePendingTransition(0, R.anim.slide_out_to_right);
			}
		}
		return true;
	}

}

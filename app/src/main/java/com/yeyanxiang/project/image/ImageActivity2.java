package com.yeyanxiang.project.image;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.img.MulitPointTouchListener;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

/**
 * 
 * Create on 2013-5-7 上午10:32:59 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 多点触控ImageView放大缩小（重写onTouchEvent,默认不居中）
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class ImageActivity2 extends Activity {
	private ImageView view;
	Matrix matrix = new Matrix();
	Rect rect;
	private ImageButton zoom_in, zoom_out;
	private PointF mid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoomimage2);
		mid = new PointF();
		findAll();
		setListener();
	}

	private void findAll() {
		view = (ImageView) findViewById(R.id.image_View);
		zoom_in = (ImageButton) findViewById(R.id.ibtn_zoom_in);
		zoom_out = (ImageButton) findViewById(R.id.ibtn_zoom_out);
	}

	private void setListener() {
		view.setOnTouchListener(new MulitPointTouchListener());
		// 放大
		zoom_in.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				matrix.set(view.getImageMatrix());
				setMid();// 设置放大的中心
				matrix.postScale(1.3f, 1.3f, mid.x, mid.y);
				view.setImageMatrix(matrix);
				view.invalidate();
			}
		});
		// 缩小
		zoom_out.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				matrix.set(view.getImageMatrix());
				setMid();// 设置放大的中心
				matrix.postScale(0.8f, 0.8f, mid.x, mid.y);
				view.setImageMatrix(matrix);
				view.invalidate();
			}
		});
	}

	private void setMid() {
		rect = view.getDrawable().getBounds();
		mid.x = view.getDrawable().getBounds().centerX();
		mid.y = view.getDrawable().getBounds().centerY();
	}
}
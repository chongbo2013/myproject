package com.yeyanxiang.project.image;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.img.ZoomImageView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.ZoomControls;

/**
 * 
 * Create on 2013-5-7 上午10:31:56 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 多点触控放大缩小ImageView(需要重写ImageView)
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class ImageActivity1 extends Activity {
	/** Called when the activity is first created. */
	private ZoomImageView mZoomView;
	private Bitmap mBitmap;
	private ProgressBar progressBar;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			progressBar.setVisibility(View.GONE);
			mZoomView.setImageBitmap(mBitmap);
			resetZoomState();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zoomimage1);
		mZoomView = (ZoomImageView) findViewById(R.id.zoomView);
		progressBar = (ProgressBar) findViewById(R.id.progress_large);
		progressBar.setVisibility(View.VISIBLE);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				/*
				 * 加载网络图片 load form url
				 */
				// mBitmap =
				// ImageDownloader.getInstance().getBitmap(url);
				mBitmap = BitmapFactory.decodeResource(
						ImageActivity1.this.getResources(), R.drawable.pic1);
				handler.sendEmptyMessage(0);
			}
		});
		thread.start();

		ZoomControls zoomCtrl = (ZoomControls) findViewById(R.id.zoomCtrl);
		zoomCtrl.setOnZoomInClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				float z = mZoomView.getZoom() + 0.25f;
				mZoomView.setZoom(z);
			}
		});
		zoomCtrl.setOnZoomOutClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				float z = mZoomView.getZoom() - 0.25f;
				mZoomView.setZoom(z);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBitmap != null)
			mBitmap.recycle();
		// mZoomView.setOnTouchListener(null);
		// mZoomState.deleteObservers();
	}

	private void resetZoomState() {
		// mZoomView.setPanX(0.5f);
		// mZoomView.setPanY(0.5f);
		mZoomView.setZoom(1.0f);
	}
}
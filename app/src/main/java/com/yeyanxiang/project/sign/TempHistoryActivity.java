package com.yeyanxiang.project.sign;

import com.yeyanxiang.project.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;

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
public class TempHistoryActivity extends Activity {
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
		setContentView(R.layout.xueyanghistory);
		context = this;

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
		String[] descs = new String[] { "36.5℃以上", "36-36.5℃", "36℃以下" };

		weekBitmap.setData(rates, colors, descs);
		monthBitmap.setData(rates, colors, descs);
		threemonthBitmap.setData(rates, colors, descs);
		sixmonthBitmap.setData(rates, colors, descs);
	}

	private class MyView extends SurfaceView implements Callback, Runnable {
		private final String TAG = "体温";
		private SurfaceHolder surfaceHolder = null;
		private Paint paint;
		private Canvas canvas;
		private int screenWidth = 1280, screenHeight = 720;

		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			surfaceHolder = getHolder();
			surfaceHolder.addCallback(this);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			super.onDraw(canvas);
			System.out.println(TAG + "------onDraw--------");
		}

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			synchronized (surfaceHolder) {
				try {
					canvas = surfaceHolder.lockCanvas();
					onDraw(canvas);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			System.out.println(TAG + "------surfaceCreated--------");
			paint = new Paint();
			// 消除锯齿
			paint.setAntiAlias(true);

			new Thread(this).start();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			System.out.println(TAG + "------surfaceChanged--------");
			screenWidth = width;
			screenHeight = height;
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			System.out.println(TAG + "------surfaceDestroyed--------");
		}
	}
}

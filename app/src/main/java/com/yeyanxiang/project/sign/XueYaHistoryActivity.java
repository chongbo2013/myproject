package com.yeyanxiang.project.sign;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.yeyanxiang.project.pub.util.PubUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

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
public class XueYaHistoryActivity extends Activity {
	private Context context;

	private List<Integer> dayhistoryhigh;
	private List<Integer> dayhistorylow;
	private List<Integer> weekhistory;
	private List<Integer> monthhistory;
	private List<Integer> threemonthhistory;

	private static int currentstate = 0;
	private static MyView myView;

	public static Handler handler = new Handler() {
		@SuppressLint("WrongCall")
		public void handleMessage(android.os.Message msg) {
			// System.out.println("血压---" + msg.what + "---" + currentstate);
			if (currentstate != msg.what) {
				currentstate = msg.what;
				myView.onDraw(currentstate);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		context = this;
		init();
		myView = new MyView(context);
		setContentView(myView);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		myView.setVisibility(View.VISIBLE);
		SignHistoryTabActivity.handler.sendEmptyMessage(currentstate);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		myView.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		currentstate = 0;
	}

	private void init() {
		// TODO Auto-generated method stub
		Random random = new Random();

		dayhistorylow = new ArrayList<Integer>();

		for (int i = 0; i < 24; i++) {
			dayhistorylow.add(random.nextInt(20) + 45);
		}

		dayhistoryhigh = new ArrayList<Integer>();
		for (int i = 0; i < 24; i++) {
			dayhistoryhigh.add(random.nextInt(20) + 75);
		}

		weekhistory = new ArrayList<Integer>();

		monthhistory = new ArrayList<Integer>();

		threemonthhistory = new ArrayList<Integer>();

	}

	private class MyView extends SurfaceView implements Callback, Runnable {
		private final String TAG = "血压";
		private SurfaceHolder surfaceHolder = null;
		private Paint paint;
		private Canvas canvas;
		Random random = new Random();
		private int screenWidth = 1280, screenHeight = 720;

		public MyView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			surfaceHolder = getHolder();
			surfaceHolder.addCallback(this);
		}

		protected void onDraw(int currentstate) {
			// TODO Auto-generated method stub
			synchronized (surfaceHolder) {
				try {
					canvas = surfaceHolder.lockCanvas();
					super.onDraw(canvas);
					System.out.println(TAG + "------onDraw--------");

					paint.setColor(Color.BLACK);
					canvas.drawRect(0, 0, screenWidth, screenHeight, paint);

					paint.setColor(Color.WHITE);
					paint.setStrokeWidth(4);
					paint.setTextSize(20);
					paint.setStyle(Style.FILL);

					canvas.drawLine(50, 50, 50, 600, paint);
					canvas.drawLine(48, 600, 1200, 600, paint);
					canvas.drawText("0", 30, 620, paint);
					canvas.drawText("mmHg", 20, 50, paint);
					canvas.drawLine(50, 50, 45, 70, paint);
					canvas.drawLine(50, 50, 55, 70, paint);
					canvas.drawLine(1200, 600, 1180, 595, paint);
					canvas.drawLine(1200, 600, 1180, 605, paint);

					canvas.drawText("高压", 1140, 25, paint);
					canvas.drawText("低压", 1140, 50, paint);
					paint.setStrokeWidth(2);
					paint.setColor(Color.RED);
					canvas.drawLine(1100, 20, 1120, 20, paint);
					paint.setColor(Color.GREEN);
					canvas.drawLine(1100, 45, 1120, 45, paint);

					for (int i = 550; i > 50; i -= 50) {
						paint.setColor(Color.DKGRAY);
						canvas.drawLine(50, i, 1200, i, paint);

						paint.setColor(Color.WHITE);
						int k = (600 - i) * 10 / 50 + 30;
						canvas.drawText(k + "", 30 - ((k / 100 + 1) * 10),
								i + 5, paint);
					}

					if (currentstate == 0) {
						drawhistory(paint, canvas, 160, 210, 1170, 0, "Date",
								"最近一周历史记录", 1);
					} else if (currentstate == 1) {
						drawhistory(paint, canvas, 37, 87, 1160, 1, "Date",
								"最近一月历史记录", 1);
					} else if (currentstate == 2) {
						drawhistory(paint, canvas, 94, 144, 1178, 0, "Date",
								"最近三个月历史记录", 7);
					} else if (currentstate == 3) {
						drawhistory(paint, canvas, 45, 95, 1130, 1, "Date",
								"最近半年历史记录", 7);
					}
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					if (canvas != null) {
						surfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
		}

		private void drawdayshistory(Paint paint, Canvas canvas) {

			paint.setColor(Color.WHITE);
			canvas.drawText("Hour", 1160, 620, paint);
			for (int i = 95; i < 1150; i += 45) {
				paint.setColor(Color.DKGRAY);
				paint.setStrokeWidth(2);
				canvas.drawLine(i, 80, i, 600, paint);
				paint.setColor(Color.WHITE);
				int k = i / 45 - 1;
				if (k > 9) {
					canvas.drawText(k + "", i - 10, 620, paint);
				} else {
					canvas.drawText(k + "", i - 5, 620, paint);
				}

				// if (k > 1) {
				// paint.setColor(Color.GREEN);
				// int m = 600 - dayhistorylow.get(k - 1) * 5;
				// int n = 600 - dayhistorylow.get(k - 2) * 5;
				// canvas.drawLine(i - 45, n, i, m, paint);
				//
				// paint.setColor(Color.RED);
				// int p = 600 - dayhistoryhigh.get(k - 1) * 5;
				// int q = 600 - dayhistoryhigh.get(k - 2) * 5;
				// canvas.drawLine(i - 45, q, i, p, paint);
				// }
				paint.setColor(Color.GREEN);
				int m = random.nextInt(150) + 300;
				int n = random.nextInt(150) + 300;
				canvas.drawLine(i - 45, n, i, m, paint);

				paint.setColor(Color.RED);
				int p = random.nextInt(150) + 150;
				int q = random.nextInt(150) + 150;
				canvas.drawLine(i - 45, q, i, p, paint);
			}

		}

		private void drawhistory(Paint paint, Canvas canvas, int vv, int left,
				int right, int x, String xunit, String desc, int multiple) {

			paint.setColor(Color.WHITE);
			canvas.drawText(xunit, 1160, 655, paint);
			canvas.drawText(desc, 530, 50, paint);

			paint.setTextSize(13);
			int inith = 0;
			int initl = 0;
			for (int i = left; i <= right; i += vv) {
				paint.setColor(Color.DKGRAY);
				paint.setStrokeWidth(2);
				canvas.drawLine(i, 80, i, 600, paint);

				int m = random.nextInt(150) + 300;
				if (initl > 0) {
					paint.setColor(Color.GREEN);
					// initl = random.nextInt(150) + 300;
					canvas.drawLine(i - vv, initl, i, m, paint);
				}
				initl = m;

				int n = random.nextInt(150) + 150;
				if (inith > 0) {
					paint.setColor(Color.RED);
					// inith = random.nextInt(150) + 150;
					canvas.drawLine(i - vv, inith, i, n, paint);
				}
				inith = n;

				paint.setColor(Color.WHITE);
				int k = i / vv - x;
				// if (k > 9) {
				// canvas.drawText(k + "", i - 10, 620, paint);
				// } else {
				// canvas.drawText(k + "", i - 5, 620, paint);
				// }
				canvas.drawCircle(i, m, 5, paint);
				canvas.drawCircle(i, n, 5, paint);
				String dateString = PubUtil.getdateString(k - 1, multiple);
				canvas.drawLine(i + 3, 620, i - 3, 635, paint);
				canvas.drawText(
						dateString.substring(dateString.indexOf("/") + 1),
						i + 4, 638, paint);
				if (dateString.substring(0, dateString.indexOf("/")).length() > 1) {
					canvas.drawText(
							dateString.substring(0, dateString.indexOf("/")),
							i - 16, 625, paint);

				} else {
					canvas.drawText(
							dateString.substring(0, dateString.indexOf("/")),
							i - 8, 625, paint);
				}

			}

		}

		@SuppressLint("WrongCall")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			onDraw(0);
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			System.out.println(TAG + "------surfaceCreated--------");
			paint = new Paint();
			// 消除锯齿
			paint.setAntiAlias(true);
			paint.setDither(true);
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

package com.yeyanxiang.project.sign;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.yeyanxiang.project.pub.util.ContinueDBUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
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
public class GECGWaveFormhistory extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	private SurfaceHolder mHolder = null;
	private Canvas mCanvas = null;
	private Paint mPaint = null;
	private long drawStartTime = 0, drawCostTime = 0, drawTotalTime = 40;
	private int screenWidth = 48, screenHeight = 80;

	private List<Float> mWaveData = new ArrayList<Float>(8000);
	private int nPointCount = 0;
	private int nBaseY = 100;// 基础高度
	private float fScaleY = 1.0f;// 放大比例
	// ////////////////////////////////////////////////////////////
	private int nSPR = 78;// 血氧检到的脉率
	private int nSPO = 0;
	private float fBodyTemp = 32.5f;// 体温

	private int nBPDia = 0;
	private int nBPSys = 0;

	private String time = "";

	private boolean loop = true;

	private ContinueDBUtil dbUtil;

	private SimpleDateFormat format;

	public GECGWaveFormhistory(Context context) {
		super(context);
		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}

	public GECGWaveFormhistory(Context context, AttributeSet attrs) {
		super(context, attrs);

		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		mPaint = new Paint();
		mPaint.setColor(Color.CYAN);
		dbUtil = new ContinueDBUtil(getContext());
		new Thread(this).start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		screenWidth = width;
		screenHeight = height;

		mWaveData = new ArrayList<Float>(screenWidth);

		int i;
		for (i = 0; i < mWaveData.size(); i++)
			mWaveData.set(i, 0.0f);

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		loop = false;
		dbUtil.close();
	}

	public void run() {
		Cursor cursor = dbUtil.select(null, ContinueDBUtil.sign_TABLE_NAME);
		if (cursor != null) {
			if (cursor.moveToNext()) {
				do {
					if (loop) {

						try {
							nSPR = cursor.getInt(cursor
									.getColumnIndex(ContinueDBUtil.sign_nSPR));
							fBodyTemp = cursor
									.getFloat(cursor
											.getColumnIndex(ContinueDBUtil.sign_fBodyTemp));
							nSPO = cursor.getInt(cursor
									.getColumnIndex(ContinueDBUtil.sign_nSPO));
							int m = cursor
									.getInt(cursor
											.getColumnIndex(ContinueDBUtil.sign_nBPSys));
							if (m > 0) {
								nBPSys = m;
							}

							m = cursor
									.getInt(cursor
											.getColumnIndex(ContinueDBUtil.sign_nBPDia));
							if (m > 0) {
								nBPDia = m;
							}

							time = format
									.format(new Date(
											cursor.getLong(cursor
													.getColumnIndex(ContinueDBUtil.sign_time))));

							String[] fecgs = cursor
									.getString(
											cursor.getColumnIndex(ContinueDBUtil.sign_fECG))
									.split(",");

							if (fecgs != null) {
								for (int i = 0; i < 5; i++) {
									for (int j = 0; j < 8; j++) {
										float FECG = Integer.parseInt(fecgs[8
												* i + j]);
										FECG = (fScaleY * FECG) + nBaseY;
										if (mWaveData.size() < screenWidth) {
											mWaveData.add(FECG);
										} else {
											if (nPointCount >= screenWidth - 1) {
												nPointCount = 0;
											} else {
												nPointCount++;
											}
											mWaveData.set(nPointCount, FECG);
										}
									}
									draw();
								}
							} else {
								draw();
							}
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}

					} else {
						break;
					}
				} while (cursor.moveToNext());
			}
			cursor.close();
		}
	}

	@SuppressLint("WrongCall")
	private void draw() {
		drawStartTime = SystemClock.uptimeMillis();
		synchronized (mHolder) {
			try {
				mCanvas = mHolder.lockCanvas();
				onDraw(mCanvas, mPaint, screenWidth, screenHeight); // repaint
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (mCanvas != null) {
					mHolder.unlockCanvasAndPost(mCanvas);
				}
			}
		}

		drawCostTime = SystemClock.uptimeMillis() - drawStartTime;
		// Log.i("drawCostTime", "drawCostTime = " +
		// drawCostTime);
		try {
			if (drawCostTime < drawTotalTime) {
				Thread.sleep(drawTotalTime - drawCostTime); // sleep
															// elapse
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onDraw(Canvas canvas, Paint paint, int nWidth, int nHeight) {
		if (paint == null) {
			return;
		}

		paint.setAntiAlias(true);
		paint.setAlpha(100);

		paint.setColor(Color.BLACK); // paint background color
		canvas.drawRect(0, 0, nWidth, nHeight, paint);
		// /////////////////////////////////////////////////////////////////////////////
		int n, m;
		{
			// System.out.println("========start===========");

			for (n = 0; n < 20; n++) {
				if (n % 5 == 0)
					paint.setColor(Color.rgb(254, 102, 102));
				else
					paint.setColor(Color.rgb(157, 6, 6));
				canvas.drawLine(0, nBaseY + n * 25, nWidth, nBaseY + n * 25,
						paint);
			}
			for (m = 0; m < nWidth; m += 25) {
				if (m % 125 == 0)
					paint.setColor(Color.rgb(254, 102, 102));
				else
					paint.setColor(Color.rgb(157, 6, 6));
				canvas.drawLine(m, nBaseY, m, nBaseY + 500, paint);
			}
			paint.setColor(Color.rgb(157, 6, 6));
			canvas.drawLine(0, nBaseY + n * 25, screenWidth, nBaseY + n * 25,
					paint);

			paint.setColor(Color.GREEN);
			paint.setStrokeWidth(3.0f);

			// float min = mWaveData.get(mWaveData.size() - 1);
			// float max = mWaveData.get(mWaveData.size() - 1);

			for (n = 1; n < (mWaveData.size() - 1); n++) {
				canvas.drawLine(n - 1, mWaveData.get(n - 1) + 25, n,
						mWaveData.get(n) + 25, paint);

				canvas.drawLine(n - 1, mWaveData.get(n - 1) + 275, n,
						mWaveData.get(n) + 275, paint);
				// if (mWaveData.get(n - 1) > max) {
				// max = mWaveData.get(n - 1);
				// } else if (mWaveData.get(n - 1) < min) {
				// min = mWaveData.get(n - 1);
				// }
			}

			// System.out.println("min=" + min + "---max=" + max);

			paint.setStrokeWidth(1.0f);
		}

		paint.setColor(Color.WHITE);

		paint.setTextSize(55);

		canvas.drawText("心率:" + "  " + "　  体温:" + "  " + "　   血氧:" + "       "
				+ "　血压:" + "        " + "-" + "  ", 0, 45, paint);

		paint.setColor(Color.GREEN);
		canvas.drawText("         " + nSPR, 0, 45, paint);
		canvas.drawText("　                      " + fBodyTemp, 0, 45, paint);
		canvas.drawText("                                            " + nSPO
				+ "%", 0, 45, paint);
		canvas.drawText(
				"　                                                            "
						+ nBPSys, 0, 45, paint);
		canvas.drawText(
				"　                                                                       "
						+ nBPDia, 0, 45, paint);

		paint.setColor(Color.WHITE);
		paint.setTextSize(35);
		canvas.drawText("时间：" + time, 10, 640, paint);

		// paint.setTextSize(24);
		// paint.setColor(Color.BLACK);

		// OnDrawButton(canvas,paint);
		// ////////////////////////////////////////////////////////////////////
		// System.out.println("心率=" + nSPR);
		// System.out.println("体温=" + fBodyTemp);
		// System.out.println("血氧=" + nSPO);
		// System.out.println("高压=" + nBPSys);
		// System.out.println("低压=" + nBPDia);
		// System.out.println("时间：" + nYear + "-" + nMonth + "-" + nDay + " "
		// + nHour + ":" + nMin + ":" + nSec);
		// System.out.println("=========stop==========");
	}
}
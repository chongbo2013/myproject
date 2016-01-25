package com.yeyanxiang.project.sign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.yeyanxiang.project.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnShowListener;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@126.com
 * 
 * @version 1.0
 * 
 * @date 2013-7-18 上午10:53:33
 */
public class GECGWaveFormOffLine extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	private SurfaceHolder mHolder = null;
	private boolean mLoop = false;

	private Canvas mCanvas = null;
	private Paint mPaint = null;
	private long drawStartTime = 0, drawCostTime = 0, drawTotalTime = 40;

	private float x, y;
	private int screenWidth = 48, screenHeight = 80;

	private List<Integer> mWaveData = new ArrayList<Integer>(8000);
	private int nPointCount = 0;
	private int nBaseY = 100;// 基础高度
	private float fScaleY = 1.0f;// 放大比例
	// ////////////////////////////////////////////////////////////
	private int orgSize = 8196;
	private byte[] orgBuff = new byte[orgSize];
	private byte[] baseBuff = new byte[16];
	private int orgUsed = 0;
	private int orgPosX = 0;// 当前位置
	// ///////////////////////////////////////////////////////////////////
	private int nRPluse = 0;// 心电检到 QRS 波.
	private int nSPluse = 0;// 血氧检到一个脉跳.
	private int nVolumeWave1 = 0;// 容积波
	private int nVolumeWave2 = 0;// 容积波
	private float fLastECGVal = 0;
	private int nLinkHeartRate = 0;// 心电导联是否脱落
	// /////////////////////////////////////////////////////////////////
	private int nHR = 0;// 心电检到的心率
	private int nSPR = 78;// 血氧检到的脉率
	private int nSPO = 0;
	private float fBodyTemp = 36.5f;// 体温

	private int nBloodP = 0;// 血压
	private int nMap = 0;
	private int nBPDia = 0;
	// private int nBPR = 0;
	private String szBloodPressStat = "";
	private int nBPSys = 0;
	private int nBloodPressStat = 0;

	private int m_nScreenWidth, m_nScreenHeight;
	// ///////////////////////////////////////////////////////////////////
	private String szDebugInfo = "";
	private int nJetCountX = 0;

	private int datacount = 0;
	private String szValX = "";

	private Random random;

	private List<Integer> list;
	private int index = 0;

	private int xinlvcolor = Color.rgb(0, 153, 68);
	private int tiwencolor = Color.rgb(0, 153, 68);
	private int xueyangcolor = Color.rgb(0, 153, 68);
	private int gaoyacolor = Color.rgb(0, 153, 68);
	private int diyacolor = Color.rgb(0, 153, 68);
	private String title = "注意身体哦，亲！";
	private String message;
	private AlertDialog dialog;
	private boolean simulation = false;

	private String stringflag = "";
	private MediaPlayer mediaPlayer;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				SpannableStringBuilder stytle = new SpannableStringBuilder(
						message);
				stytle.setSpan(new ForegroundColorSpan(Color.RED),
						message.indexOf("您的身体有如下症状") + 10,
						message.indexOf("请根据自己的需要及时就医"),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				dialog.setMessage(stytle);
				if (!message.equals(stringflag)) {
					dialog.show();
				}
				stringflag = message;
			} else if (msg.what == 2) {
				SpannableStringBuilder stytle = new SpannableStringBuilder(
						message);
				stytle.setSpan(new ForegroundColorSpan(Color.RED),
						message.indexOf("您的身体有如下症状") + 10,
						message.indexOf("请根据自己的需要及时就医"),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				dialog.setMessage(stytle);
				stringflag = message;
			} else if (msg.what == 3) {
				dialog.dismiss();
			}
		};
	};

	public GECGWaveFormOffLine(Context context) {
		super(context);
		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}

	public GECGWaveFormOffLine(Context context, AttributeSet attrs) {
		super(context, attrs);

		mHolder = this.getHolder();
		mHolder.addCallback(this);

	}

	public void surfaceCreated(SurfaceHolder holder) {
		dialog = new AlertDialog.Builder(getContext()).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						ElectrocardioOffLineActivity.dismissalarm();
					}
				}).create();
		dialog.setTitle(title);
		dialog.setOnShowListener(new OnShowListener() {

			@Override
			public void onShow(DialogInterface dialog) {
				// TODO Auto-generated method stub
				try {
					if (mediaPlayer == null) {
						mediaPlayer = new MediaPlayer();
						mediaPlayer
								.setAudioStreamType(AudioManager.STREAM_MUSIC);
						mediaPlayer.setDataSource(
								getContext(),
								Uri.parse("android.resource://"
										+ getContext().getPackageName() + "/"
										+ R.raw.atm));
						mediaPlayer.prepare();
					}

					if (!mediaPlayer.isPlaying()) {
						mediaPlayer.start();
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		random = new Random();
		initdata();
		mPaint = new Paint();
		mPaint.setColor(Color.CYAN);
		mLoop = true;
		// dbUtil = new ContinueDBUtil(getContext());
		new Thread(this).start();
	}

	public void alarm() {
		stringflag = "";
		int i = 0;

		if (random.nextBoolean()) {
			i++;
			int k = random.nextInt(10);
			if (random.nextBoolean()) {
				nSPR = 39 + k;
			} else {
				nSPR = 121 + k;
			}
		}
		if (random.nextBoolean()) {
			i++;
			int k = random.nextInt(10);
			if (random.nextBoolean()) {
				fBodyTemp = (float) (34.9 + (k / 10.0));
			} else {
				fBodyTemp = (float) (37.1 + (k / 10.0));
			}
		}
		if (random.nextBoolean()) {
			i++;
			nSPO = 94 - random.nextInt(5);
		}
		if (random.nextBoolean()) {
			i++;
			int k = random.nextInt(20);
			if (random.nextBoolean()) {
				nBPSys = 141 + k;

			} else {
				nBPSys = 69 + k;
			}
		}
		if (random.nextBoolean()) {
			i++;
			int k = random.nextInt(20);
			if (nBPSys > 140) {
				nBPDia = 91 + k;
			} else if (nBPSys < 90) {
				nBPDia = 39 + k;
			}
		}

		if (i == 0) {
			int k = random.nextInt(10);
			nSPR = 121 + k;
		}
	}

	public void dismissalarm() {
		nSPR = 78;
		fBodyTemp = 36.5f;
		nSPO = 98;
		nBPSys = 125;
		nBPDia = 75;

		xinlvcolor = Color.rgb(0, 153, 68);
		tiwencolor = Color.rgb(0, 153, 68);
		xueyangcolor = Color.rgb(0, 153, 68);
		gaoyacolor = Color.rgb(0, 153, 68);
		diyacolor = Color.rgb(0, 153, 68);
	}

	private void initdata() {
		// TODO Auto-generated method stub
		list = new ArrayList<Integer>();
		list.add(206);
		list.add(207);
		list.add(207);
		list.add(207);
		list.add(208);
		list.add(207);
		list.add(207);
		list.add(207);

		list.add(205);
		list.add(203);
		list.add(201);
		list.add(199);
		list.add(199);
		list.add(199);
		list.add(199);
		list.add(200);

		list.add(198);
		list.add(196);
		list.add(193);
		list.add(192);
		list.add(192);
		list.add(192);
		list.add(193);
		list.add(195);

		list.add(197);
		list.add(199);
		list.add(199);
		list.add(200);
		list.add(202);
		list.add(203);
		list.add(204);
		list.add(206);

		list.add(208);
		list.add(210);
		list.add(212);
		list.add(214);
		list.add(214);
		list.add(213);
		list.add(213);
		list.add(214);

		list.add(214);
		list.add(213);
		list.add(212);
		list.add(213);
		list.add(213);
		list.add(213);
		list.add(213);
		list.add(214);

		list.add(214);
		list.add(214);
		list.add(214);
		list.add(215);
		list.add(215);
		list.add(214);
		list.add(213);
		list.add(213);

		list.add(213);
		list.add(213);
		list.add(214);
		list.add(214);
		list.add(214);
		list.add(213);
		list.add(213);
		list.add(214);

		list.add(214);
		list.add(214);
		list.add(214);
		list.add(215);
		list.add(215);
		list.add(214);
		list.add(214);
		list.add(215);

		list.add(215);
		list.add(215);
		list.add(215);
		list.add(215);
		list.add(216);
		list.add(216);
		list.add(216);
		list.add(215);

		list.add(214);
		list.add(213);
		list.add(214);
		list.add(214);
		list.add(213);
		list.add(212);
		list.add(212);
		list.add(212);

		list.add(212);
		list.add(213);
		list.add(214);
		list.add(215);
		list.add(215);
		list.add(214);
		list.add(214);
		list.add(214);

		list.add(215);
		list.add(215);
		list.add(214);
		list.add(214);
		list.add(213);
		list.add(214);
		list.add(215);
		list.add(215);

		list.add(214);
		list.add(213);
		list.add(213);
		list.add(213);
		list.add(213);
		list.add(213);
		list.add(214);
		list.add(214);

		list.add(214);
		list.add(213);
		list.add(214);
		list.add(213);
		list.add(213);
		list.add(214);
		list.add(214);
		list.add(214);

		list.add(214);
		list.add(214);
		list.add(214);
		list.add(215);
		list.add(215);
		list.add(216);
		list.add(216);
		list.add(216);

		list.add(216);
		list.add(216);
		list.add(217);
		list.add(217);
		list.add(217);
		list.add(218);
		list.add(218);
		list.add(218);

		list.add(217);
		list.add(215);
		list.add(214);
		list.add(211);
		list.add(210);
		list.add(209);
		list.add(209);
		list.add(208);

		list.add(208);
		list.add(208);
		list.add(209);
		list.add(209);
		list.add(208);
		list.add(207);
		list.add(207);
		list.add(207);

		list.add(208);
		list.add(209);
		list.add(212);
		list.add(214);
		list.add(215);
		list.add(217);
		list.add(218);
		list.add(217);

		list.add(216);
		list.add(215);
		list.add(214);
		list.add(214);
		list.add(214);
		list.add(215);
		list.add(215);
		list.add(214);

		list.add(213);
		list.add(208);
		list.add(199);
		list.add(186);
		list.add(169);
		list.add(151);
		list.add(137);
		list.add(131);

		list.add(137);
		list.add(156);
		list.add(184);
		list.add(213);
		list.add(237);
		list.add(250);
		list.add(254);
		list.add(250);

		list.add(242);
		list.add(235);
		list.add(228);
		list.add(222);
		list.add(217);
		list.add(215);
		list.add(214);
		list.add(214);

		list.add(216);
		list.add(217);
		list.add(214);
		list.add(211);
		list.add(208);
		list.add(206);
		list.add(206);
		list.add(207);

		// list.add();
		// list.add();
		// list.add();
		// list.add();
		// list.add();
		// list.add();
		// list.add();
		// list.add();

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		screenWidth = width;
		screenHeight = height;

		x = screenWidth / 2;
		y = screenHeight / 2;

		mWaveData = new ArrayList<Integer>(screenWidth);

		m_nScreenWidth = width;
		m_nScreenHeight = height;

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mLoop = false;
	}

	@SuppressLint("WrongCall")
	public void run() {
		while (mLoop) {
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
			// Log.i("drawCostTime", "drawCostTime = " + drawCostTime);
			// OnJetXData((int) drawCostTime);

			try {
				if (drawCostTime < drawTotalTime) {
					Thread.sleep(drawTotalTime - drawCostTime); // sleep elapse
																// time
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// private void OnJetXData(int nUserDataX) {
	// for (int i = 0; i < 8; i++) {
	// nJetCountX++;
	// drawCostTime = (long) (Math.random() * 30 + 90);
	// if (mWaveData.size() < screenWidth)
	// mWaveData.add((int) ((fScaleY * drawCostTime) + nBaseY));
	// else {
	// mWaveData.set(nPointCount,
	// (int) ((fScaleY * drawCostTime) + nBaseY));
	// nPointCount++;
	//
	// if (nPointCount >= screenWidth)
	// nPointCount = 0;
	// }
	// }
	// }

	public void onDraw(Canvas canvas, Paint paint, int nWidth, int nHeight) {
		if (paint == null) {
			return;
		}

		paint.setAntiAlias(true);
		paint.setAlpha(100);

		// paint.setColor(Color.rgb(238, 250, 226)); // paint background color
		paint.setColor(Color.BLACK); // paint background color
		canvas.drawRect(0, 0, nWidth, nHeight, paint);
		// /////////////////////////////////////////////////////////////////////////////
		int n, m;
		{
			// System.out.println("========start===========");
			OnReadData();

			for (n = 0; n < 20; n++) {
				if (n % 5 == 0)
					paint.setColor(Color.rgb(254, 102, 102));
				// paint.setColor(Color.GRAY);
				// paint.setColor(Color.rgb(248, 211, 202));
				else
					// paint.setColor(Color.GRAY);
					// paint.setColor(Color.rgb(239, 215, 215));
					paint.setColor(Color.rgb(157, 6, 6));
				canvas.drawLine(0, nBaseY + n * 25, nWidth, nBaseY + n * 25,
						paint);
			}
			for (m = 0; m < nWidth; m += 25) {
				if (m % 125 == 0)
					paint.setColor(Color.rgb(254, 102, 102));
				// paint.setColor(Color.rgb(248, 211, 202));
				// paint.setColor(Color.GRAY);
				else
					paint.setColor(Color.rgb(157, 6, 6));
				// paint.setColor(Color.GRAY);
				// paint.setColor(Color.rgb(239, 215, 215));
				canvas.drawLine(m, nBaseY, m, nBaseY + 500, paint);
			}
			paint.setColor(Color.rgb(157, 6, 6));
			// paint.setColor(Color.rgb(239, 215, 215));
			canvas.drawLine(0, nBaseY + n * 25, m_nScreenWidth,
					nBaseY + n * 25, paint);

			paint.setColor(Color.GREEN);
			paint.setStrokeWidth(2.0f);

			// if (result == 1) {
			// PubUtil.showText(getContext(), "医疗设备已断开");
			// paint.setStrokeWidth(1.0f);
			// } else {
			// }

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

		paint.setColor(Color.rgb(255, 251, 199));

		paint.setTextSize(55);

		canvas.drawText("心率:" + "  " + "　  体温:" + "  " + "　   血氧:" + "       "
				+ "　血压:" + "          " + "-" + "  ", 0, 45, paint);

		boolean flag = false;
		message = "您的身体有如下症状:\n";
		if (nSPR <= 0) {
			nSPR = 78;
		} else if (nSPR < 50) {
			xinlvcolor = Color.RED;
			flag = true;
			message += "\n\t心率有些偏低";
		} else if (nSPR > 120) {
			xinlvcolor = Color.RED;
			flag = true;
			message += "\n\t心率有些偏高";
		}

		if (fBodyTemp <= 0) {
			fBodyTemp = 36.5f;
		} else if (fBodyTemp < 36) {
			tiwencolor = Color.RED;
			flag = true;
			message += "\n\t体温有些偏低";
		} else if (fBodyTemp > 37) {
			tiwencolor = Color.RED;
			flag = true;
			message += "\n\t体温有些偏高";
		}

		if (nSPO <= 0) {
			nSPO = 98;
		} else if (nSPO < 95) {
			xueyangcolor = Color.RED;
			flag = true;
			message += "\n\t血氧有些偏低";
		}

		if (nBPSys <= 0) {
			nBPSys = 125;
		} else if (nBPSys < 90) {
			gaoyacolor = Color.RED;
			flag = true;
			message += "\n\t收缩压有些偏低";
		} else if (nBPSys > 140) {
			gaoyacolor = Color.RED;
			flag = true;
			message += "\n\t收缩压有些偏高";
		}

		if (nBPDia <= 0) {
			nBPDia = 75;
		} else if (nBPDia < 60) {
			diyacolor = Color.RED;
			flag = true;
			message += "\n\t舒张压有些偏低";
		} else if (nBPDia > 90) {
			diyacolor = Color.RED;
			flag = true;
			message += "\n\t舒张压有些偏高";
		}

		message += "\n\n请根据自己的需要及时就医！";

		paint.setColor(xinlvcolor);
		canvas.drawText("         " + nSPR, 0, 45, paint);
		paint.setColor(tiwencolor);
		canvas.drawText("　                      " + fBodyTemp, 0, 45, paint);
		paint.setColor(xueyangcolor);
		canvas.drawText("                                            " + nSPO
				+ "%", 0, 45, paint);
		paint.setColor(gaoyacolor);
		canvas.drawText(
				"　                                                            "
						+ nBPSys, 0, 45, paint);
		paint.setColor(diyacolor);
		canvas.drawText(
				"　                                                                       "
						+ nBPDia, 0, 45, paint);

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

		if (flag) {
			if (!dialog.isShowing()) {
				handler.sendEmptyMessage(1);
			} else {
				handler.sendEmptyMessage(2);
			}
		} else {
			if (dialog != null && dialog.isShowing()) {
				handler.sendEmptyMessage(3);
			}
		}
	}

	public void OnReadData() {
		try {
			for (int i = 0; i < 16; i++) {
				int fECG = list.get(index);
				index++;
				if (index >= list.size()) {
					index = 0;
				}

				if (mWaveData.size() < screenWidth) {
					mWaveData.add(fECG);
				} else {
					mWaveData.set(nPointCount, fECG);
					nPointCount++;
					if (nPointCount >= screenWidth) {
						nPointCount = 0;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
package com.yeyanxiang.project.sign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.ContinueDBUtil;
import com.yeyanxiang.project.pub.util.PubUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
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
import android.util.Log;
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
public class GECGWaveForm extends SurfaceView implements
		SurfaceHolder.Callback, Runnable {

	private SurfaceHolder mHolder = null;
	private boolean mLoop = false;

	private Canvas mCanvas = null;
	private Paint mPaint = null;
	private long drawStartTime = 0, drawCostTime = 0, drawTotalTime = 40;

	private float x, y;
	private int screenWidth = 48, screenHeight = 80;

	private List<Float> mWaveData = new ArrayList<Float>(8000);
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
	private float fBodyTemp = 32.5f;// 体温

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

	private int xinlvcolor = Color.rgb(0, 153, 68);
	private int tiwencolor = Color.rgb(0, 153, 68);
	private int xueyangcolor = Color.rgb(0, 153, 68);
	private int gaoyacolor = Color.rgb(0, 153, 68);
	private int diyacolor = Color.rgb(0, 153, 68);
	private String title = "注意身体哦，亲！";
	private String message;
	private AlertDialog dialog;
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

	// private ContinueDBUtil dbUtil;

	public GECGWaveForm(Context context) {
		super(context);
		mHolder = this.getHolder();
		mHolder.addCallback(this);
	}

	public GECGWaveForm(Context context, AttributeSet attrs) {
		super(context, attrs);

		mHolder = this.getHolder();
		mHolder.addCallback(this);

	}

	public void surfaceCreated(SurfaceHolder holder) {
		dialog = new AlertDialog.Builder(getContext()).setNegativeButton("取消",
				null).create();
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

		mPaint = new Paint();
		mPaint.setColor(Color.CYAN);
		mLoop = true;
		// dbUtil = new ContinueDBUtil(getContext());
		new Thread(this).start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		screenWidth = width;
		screenHeight = height;

		x = screenWidth / 2;
		y = screenHeight / 2;

		mWaveData = new ArrayList<Float>(screenWidth);

		int i;
		for (i = 0; i < mWaveData.size(); i++)
			mWaveData.set(i, 0.0f);

		m_nScreenWidth = width;
		m_nScreenHeight = height;

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mLoop = false;
		BluetoothActivity.stopsavedata();
		// dbUtil.close();
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
			OnJetXData((int) drawCostTime);

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

	private void OnJetXData(int nUserDataX) {
		if (BluetoothActivity.bluetoothSocket != null)
			return;
		for (int i = 0; i < 8; i++) {
			nJetCountX++;
			drawCostTime = (long) (Math.random() * 30 + 90);
			if (mWaveData.size() < screenWidth)
				mWaveData.add((fScaleY * drawCostTime) + nBaseY);
			else {
				if (nPointCount >= (screenWidth - 1))
					nPointCount = 0;
				else
					nPointCount++;
				mWaveData.set(nPointCount, (fScaleY * drawCostTime) + nBaseY);
			}
		}
	}

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
			int result = OnReadData();

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

			if (result == 1) {
				PubUtil.showText(getContext(), "医疗设备已断开");
				paint.setStrokeWidth(1.0f);
			} else {
				paint.setStrokeWidth(2.0f);
			}

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
		message = "您的身体有如下症状:";
		if (nSPR <= 0) {
			nSPR = 78;
			xinlvcolor = Color.rgb(0, 153, 68);
		} else if (nSPR < 40) {
			xinlvcolor = Color.RED;
			flag = true;
			message += "\n\t心率有些偏低";
		} else if (nSPR > 120) {
			xinlvcolor = Color.RED;
			flag = true;
			message += "\n\t心率有些偏高";
		} else {
			xinlvcolor = Color.rgb(0, 153, 68);
		}

		if (fBodyTemp <= 0) {
			fBodyTemp = 36.5f;
			tiwencolor = Color.rgb(0, 153, 68);
		} else if (fBodyTemp < 36) {
			tiwencolor = Color.RED;
			flag = true;
			message += "\n\t体温有些偏低";
		} else if (fBodyTemp > 37) {
			tiwencolor = Color.RED;
			flag = true;
			message += "\n\t体温有些偏高";
		} else {
			tiwencolor = Color.rgb(0, 153, 68);
		}

		if (nSPO <= 0) {
			nSPO = 98;
			xueyangcolor = Color.rgb(0, 153, 68);
		} else if (nSPO < 95) {
			xueyangcolor = Color.RED;
			flag = true;
			message += "\n\t血氧有些偏低";
		} else {
			xueyangcolor = Color.rgb(0, 153, 68);
		}

		if (nBPSys <= 0) {
			nBPSys = 125;
			gaoyacolor = Color.rgb(0, 153, 68);
		} else if (nBPSys < 90) {
			gaoyacolor = Color.RED;
			flag = true;
			message += "\n\t高压有些偏低";
		} else if (nBPSys > 140) {
			gaoyacolor = Color.RED;
			flag = true;
			message += "\n\t高压有些偏高";
		} else {
			gaoyacolor = Color.rgb(0, 153, 68);
		}

		if (nBPDia <= 0) {
			nBPDia = 75;
			diyacolor = Color.rgb(0, 153, 68);
		} else if (nBPDia < 60) {
			diyacolor = Color.RED;
			flag = true;
			message += "\n\t低压有些偏低";
		} else if (nBPDia > 90) {
			diyacolor = Color.RED;
			flag = true;
			message += "\n\t低压有些偏高";
		} else {
			diyacolor = Color.rgb(0, 153, 68);
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

	public int OnReadData() {
		float fECG;
		int i, ret, nHeadPos, nSumVal, nRemain;

		try {
			// Log.w("BTDEV", "OnReadData Start......");
			if (BluetoothActivity.bluetoothSocket == null) {

				Log.w("BTDEV", "ActBTDevMgr.mBTSocket == null");
				return 1;
			}
			if (BluetoothActivity.bluetoothSocket.getInputStream() == null) {
				Log.w("BTDEV", "ActBTDevMgr.mBTSocket.getInputStream() == null");
				return 1;
			}
			ret = BluetoothActivity.bluetoothSocket.getInputStream().read(
					orgBuff, orgUsed, orgSize - orgUsed);
			if (ret <= 0) {
				Log.w("BTDEV",
						"ActBTDevMgr.mBTSocket.getInputStream().read()<=0");
				return 0;
			}
			orgUsed += ret;
			nHeadPos = 0;
			nRemain = 0;
			nHeadPos = 0;
			// //////////////////////////////////////////////////////////////////////////////////////////////
			// 解析蓝牙数据
			for (;;) {
				// 找数据头
				nHeadPos = GetUserDataHead(nHeadPos, orgUsed);
				// System.out.println("数据头=" + nHeadPos + "------------");
				if (nHeadPos < 0) {
					orgUsed = 0;
					return 0;
				}
				if ((orgUsed - nHeadPos) < 16)// 不足一个数据包
				{
					for (i = 0; i < (orgUsed - nHeadPos); i++)
						orgBuff[i] = orgBuff[nHeadPos + i];
					orgUsed = orgUsed - nHeadPos;
					return 0;
				}
				switch (orgBuff[nHeadPos + 1]) {
				case 0x02:// 开始传输历史数据包
				{
					if (orgBuff[nHeadPos + 15] == -5/* 0xFB */)// 开始包:
																// {0xfb,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0xfd};
																// /*
																// 开始传输数据的第一个包
																// */
					{
						Log.i("OnDataHistory", "数据开始");
					}
				}
					break;
				case 0x01:// 结束或者终止历史数据包的传输
				{
					if (orgBuff[nHeadPos + 15] == 0x09)// 结束包:
														// {0xfb,1,1,1,1,1,1,1,1,1,1,1,1,1,1,0x09};
														// /* 表示数据传输完成 */
					{
						Log.i("OnDataHistory", "数据结束");
					} else // 数据输出中止'包: {0xfb,1,1,1,1,1,1,1,1,1,1,1,1,1,0,0x08};
							// /* 传输过程没有完成, 但操作'中止' */
					{
						Log.i("OnDataHistory", "数据终止");
						// nStateDownload = 0;
						// szDownloadCaption = "开始下载";
						// mActivity.setnStateDownload(3);
						// nStateDownload = 3;//进入停止
					}
				}
					break;
				// ////////////////////////////////////////////////////////////
				case 0x03:
					OnDataRealTime(nHeadPos);
					break;// 处理实时数据
				default:
					OnDataSaveHistory(nHeadPos);
					break;// 记录定义:
				}// end of switch
				nHeadPos += 16;
			}// end of 死循环
		} catch (Exception e) {
			e.printStackTrace();
			return 1;
		}

	}// End Of OnReadData

	/*
	 * [251] 有效数据头是 251, 如果写入 255, 则为无效数.
	 * [YEAR],[MONTH],[DAY],[HOUR],[MIN],[SEC] [SYS H2], [SYS L7]: 如果SYS H
	 * 的D7=1, 则'检测到运动'; D6=1, 表示新测到的数据; D5=0, 成人; D5=1, 儿童. [MAP]: 如果>250, 就写250
	 * [DIA] 血压的数据如果=0, 则无效, [HR] >250 无效 [SPO2] >100 无效 [TEMP H3], [TEMP L7]
	 * >500 无效 [校验和]
	 */
	private void OnDataSaveHistory(int nHeadPos)// 历史数据记录处理
	{
		String szSQL, szDataValX, szDateTime;
		int i, nSumVal;

		for (i = 0; i < 16; i++)
			baseBuff[i] = this.orgBuff[i + nHeadPos];
		szDataValX = GUtilBase64.encode(baseBuff);
		szDateTime = (orgBuff[nHeadPos + 1] + 2000) + "-"
				+ orgBuff[nHeadPos + 2] + "-" + orgBuff[nHeadPos + 3] + " "
				+ orgBuff[nHeadPos + 4] + ":" + orgBuff[nHeadPos + 5] + ":"
				+ orgBuff[nHeadPos + 6];

		nBPSys = (((int) orgBuff[7 + nHeadPos]) & 0x03) << 7;
		nBPSys += (((int) orgBuff[8 + nHeadPos]) & 0x7F);

		nMap = (int) orgBuff[9 + nHeadPos];
		nBPDia = (int) orgBuff[10 + nHeadPos];

		nHR = (int) orgBuff[11 + nHeadPos];
		nSPO = ((int) orgBuff[12 + nHeadPos]);

		nSumVal = (((int) orgBuff[13 + nHeadPos]) & 0x07) << 7;
		nSumVal += (((int) orgBuff[14 + nHeadPos]) & 0x7F);
		fBodyTemp = nSumVal / 10.0f;
	}

	private void OnDataRealTime(int nHeadPos) {
		float fECG;
		int nSumVal, i;

		try {
			switch (orgBuff[nHeadPos + 2])// 指令
			{
			// ////////////////////////////////////////////////////////////////////////
			case 0x01:// 波形数据
			{
				nSumVal = 0;
				nLinkHeartRate = 1;
				int nValX;
				if (nLinkHeartRate > 0) {
					for (i = 0; i < 8; i++) {

						fECG = (orgBuff[i + 3 + nHeadPos] & 0x7F) * (1.0f);
						if (0 != (orgBuff[i + 3 + nHeadPos] & 0x80))
							fECG = fECG + 128.0f;

						fECG = 250.0f - fECG;
						if (fECG < 0.001f || fECG > 250.0f)
							fECG = 0.0f;

						if (BluetoothActivity.getsavedata()) {
							datacount++;
							if (szValX.length() < 1)
								szValX = "" + ((int) fECG);
							else
								szValX = szValX + "," + ((int) fECG);
							if (fECG < 0.001f || fECG > 250.0f)
								szValX = "";
						}

						fECG = (fScaleY * fECG) + nBaseY;
						if (mWaveData.size() < screenWidth) {
							mWaveData.add(fECG);
						} else {
							if (nPointCount >= screenWidth - 1) {
								nPointCount = 0;
							} else {
								nPointCount++;
							}
							mWaveData.set(nPointCount, fECG);
						}
					}

					if (BluetoothActivity.getsavedata()) {
						if (datacount % 5 == 0) {
							ContentValues contentValues = new ContentValues();
							contentValues.put(ContinueDBUtil.sign_time,
									System.currentTimeMillis());
							contentValues.put(ContinueDBUtil.sign_nSPR, nSPR);
							contentValues.put(ContinueDBUtil.sign_fBodyTemp,
									fBodyTemp);
							contentValues.put(ContinueDBUtil.sign_nSPO, nSPO);
							contentValues.put(ContinueDBUtil.sign_fECG, szValX);
							// System.out.println(szValX);
							// dbUtil.insert(contentValues);
							szValX = "";
							BluetoothActivity.getsavelist().add(contentValues);
						}
					}

				} else {
					for (i = 0; i < 8; i++) {
						if (mWaveData.size() < screenWidth) {
							mWaveData.add(nBaseY * (1.0f) + 125.0f);
						} else {
							if (nPointCount >= screenWidth) {
								nPointCount = 0;
							} else {
								nPointCount++;
							}
							mWaveData
									.set(nPointCount, nBaseY * (1.0f) + 125.0f);
						}
					}
				}
				nVolumeWave1 = orgBuff[11 + nHeadPos];
				nVolumeWave2 = orgBuff[12 + nHeadPos];
				nRPluse = orgBuff[13 + nHeadPos];
				nSPluse = orgBuff[14 + nHeadPos];

			}
				break;// end of 波形数据
			case 0x02:// 实时时间及血压数据 251, 03, 02, YEAR, MONTH, DAY, HOUR, MIN,
						// SEC, [SYS H2], [SYS L7], [MAP], [DIA],[BPR], 血压测试状态,
						// SUM
			{
				// nYear = 2000 + (int) orgBuff[3 + nHeadPos];
				// nMonth = (int) orgBuff[4 + nHeadPos];
				// nDay = (int) orgBuff[5 + nHeadPos];
				// nHour = (int) orgBuff[6 + nHeadPos];
				// nMin = (int) orgBuff[7 + nHeadPos];
				// nSec = (int) orgBuff[8 + nHeadPos];

				nBPSys = (((int) orgBuff[9 + nHeadPos]) & 0x03) << 7;
				nBPSys += (((int) orgBuff[10 + nHeadPos]) & 0x7F);
				nMap = (int) orgBuff[11 + nHeadPos];
				nBPDia = (int) orgBuff[12 + nHeadPos];
				// nBPR = (int) orgBuff[13 + nHeadPos];

				if (orgBuff[14 + nHeadPos] == 1) {
					szBloodPressStat = "血压测量中";
					nBloodPressStat = 1;
				} else {
					szBloodPressStat = "血压不测量";
					if (nBloodPressStat == 1) {
						System.out.println("血压更新---高压：" + nBPSys + "---低压："
								+ nBPDia + "---time:"
								+ System.currentTimeMillis());
						nBloodPressStat = 0;
						if (BluetoothActivity.getsavedata()) {
							ContentValues contentValues = new ContentValues();
							contentValues.put(ContinueDBUtil.sign_time,
									System.currentTimeMillis());
							contentValues.put(ContinueDBUtil.sign_nSPR, nSPR);
							contentValues.put(ContinueDBUtil.sign_fBodyTemp,
									fBodyTemp);
							contentValues.put(ContinueDBUtil.sign_nSPO, nSPO);
							contentValues.put(ContinueDBUtil.sign_nBPSys,
									nBPSys);
							contentValues.put(ContinueDBUtil.sign_nBPDia,
									nBPDia);
							contentValues.put(ContinueDBUtil.sign_nBPflag, 1);
							// System.out.println("血压更新---"
							// + dbUtil.insert(contentValues));
							BluetoothActivity.getsavelist().add(contentValues);
						}
					}
				}
			}
				break;
			// ///////////////////////////////////////////////////
			// SYS 是收缩压90－120，DIA 是舒展压60－90.
			case 0x03:// 体温及其它数据, 每秒1个包，251, 03, 03, HR, SPR, [TEMP H3], [TEMP
						// L7] , [袖带压 H2], [袖带压 L7], 心电探头状态,血氧探头状态, 血压测试错误提示,
						// [SPO2值], 血压倒计时分, 血压倒计时秒, SUM
			{
				nHR = (int) orgBuff[3 + nHeadPos];
				nSPR = (int) orgBuff[4 + nHeadPos];
				if (nSPR < 0 || nSPR > 255)
					nSPR = 0;
				nSumVal = (((int) orgBuff[5 + nHeadPos]) & 0x07) << 7;
				nSumVal += (((int) orgBuff[6 + nHeadPos]) & 0x7F);
				fBodyTemp = nSumVal / 10.0f;

				nBloodP = (((int) orgBuff[7 + nHeadPos]) & 0x03) << 7;
				nBloodP += (((int) orgBuff[8 + nHeadPos]) & 0x7F);

				nSPO = ((int) orgBuff[12 + nHeadPos]);
				if (nSPO > 100)
					nSPO = 0;
				nLinkHeartRate = ((int) orgBuff[9 + nHeadPos]);
			}
				break;
			}// end of switch
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int GetUserDataHead(int nStart, int nSize) {
		int i;

		for (i = nStart; i < nSize; i++) {
			if (orgBuff[i] == -5)// 0xFB)弱智的java，居然没有无符号字节
				return i;
		}
		return -1;
	}
}
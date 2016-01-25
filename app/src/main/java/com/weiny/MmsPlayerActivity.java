package com.weiny;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 * 
 * Create on 2013-5-7 上午10:34:00 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: mms协议广播电台播放
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class MmsPlayerActivity extends Activity {
	static {
		try {
			System.loadLibrary("mmsPlayer");
			Log.v("MMS", "load lib success");
		} catch (UnsatisfiedLinkError localUnsatisfiedLinkError) {
			localUnsatisfiedLinkError.printStackTrace();
		}
	}

	public native int closemms();

	public native byte[] getmmsBytes();

	public native int getmmslistsize();

	public native int initmms();

	public native int wmaDecoder();

	public native int wmaalign();

	public native int wmabit();

	public native int wmabytespersec();

	public native int wmachannels();

	public native int wmasamplerate();

	public native int wmasamplesize();

	public native int openmms(String paramString);

	private MMSTrack track;
	private int out = 0;
	private DecoderThread decoderThread;
	private PlayerThread playerThread;
	private boolean decodethread = true;
	private boolean playthread = true;

	private class DecoderThread extends Thread {
		private String url;

		public DecoderThread(String url) {
			super();
			this.url = "file://" + url;
		}

		public void run() {
			try {
				startPlay(url);
				sleep(1000L);
			} catch (InterruptedException localInterruptedException) {
				localInterruptedException.printStackTrace();
				Log.e("mms", localInterruptedException.getMessage());
			} catch (Exception localException) {
				localException.printStackTrace();
				Log.e("mms", localException.getMessage());
			}
		}
	}

	private int startPlay(String paramString) {
		int i = openmms(paramString);
		if (i != 0) {
			i = closemms();
			return i;
		}
		int j = 0;
		int l = 0;
		j = wmachannels();

		l = wmabit();

		i = javawmaDecoder();
		track = new MMSTrack(wmasamplerate(), j, l);
		track.init();
		out = 1;
		playerThread = new PlayerThread();
		playerThread.start();
		while (decodethread) {
			if (i <= 0)
				out = 0;
			try {
				i = javawmaDecoder();
			} catch (Exception localException) {
				localException.printStackTrace();
				Log.e("mms", localException.getMessage());
				break;
			}
		}
		return i;
	}

	private int javawmaDecoder() {
		int i = 0;
		try {
			i = wmaDecoder();

		} catch (Exception localException) {
			localException.printStackTrace();
			Log.e("mms", "解码异常" + localException.getMessage());
		}
		return i;
	}

	private class PlayerThread extends Thread {
		public void run() {
			try {
				while (playthread) {
					if (out == 0) {
						track.release();
						return;
					}
					try {
						byte[] arrayOfByte = getmmsBytes();
						track.playAudioTrack(arrayOfByte.clone(), 0,
								arrayOfByte.length);
					} catch (Exception localInterruptedException) {
						localInterruptedException.printStackTrace();
					}
				}
			} catch (Exception j) {
			}
		}
	}

	public void openPlayer(String url) {
		initmms();
		decoderThread = new DecoderThread(url);
		decoderThread.start();
	}

	public void closePlayer() {
		try {
			decodethread = false;
			playthread = false;
			track.release();
			System.gc();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static final String MMSPATH = "mmspath";
	private SurfaceView surfaceView;
	private String urlString;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplayer);

		if (getIntent() != null) {
			urlString = getIntent().getStringExtra(MMSPATH);
			if (TextUtils.isEmpty(urlString)) {
				PubUtil.showText(this, "参数有误");
				finish();
			} else {
				if (urlString.contains("/")) {
					((TextView) findViewById(R.id.mediaplayertitle))
							.setText(urlString.substring(urlString
									.lastIndexOf("/") + 1));
				}
				surfaceView = (SurfaceView) findViewById(R.id.mediaplayersurface);
				surfaceView.setBackgroundResource(R.drawable.music);
				surfaceView.getHolder().setFixedSize(400, 400);
				openPlayer(urlString);
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		closePlayer();
		super.onDestroy();
	}
}

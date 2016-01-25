package com.yeyanxiang.project.mediaplayer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 
 * Create on 2013-5-7 上午10:23:59 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 使用mediaplayer 和sufaceview 播放支持rtsp和http
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class MediaPlayerActivity extends Activity implements
		SurfaceHolder.Callback {
	public static final String MEDIA_PATH = "pathstring";
	public static final String MEDIA_TYPE = "mediptype";
	public static final String MEDIA_TITLE = "mediatitle";
	private Context context;
	private String urlString = "";
	private String mediatype = "";
	private String mediatitle = "";

	private TextView title;
	private TextView mediaprogress;
	private TextView videolength;
	private TextView mediaplayerbuffer;
	private SeekBar seekBar;
	private ImageButton lastButton;
	private ImageButton playButton;
	private ImageButton nextButton;
	private RelativeLayout bottomLayout;
	private ProgressDialog progressDialog;
	private Animation topin;
	private Animation bottomin;
	private Animation topout;
	private Animation bottomout;
	private boolean exit = true;
	private DecimalFormat decimalFormat;

	private MediaPlayer mediaPlayer;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private int position;
	private int duration;
	private int screenwidth = 1280;
	private int screenheight = 720;
	private float videowidth = 1280;
	private float videoheight = 720;
	private boolean isseek = false;
	private boolean oncreate = false;
	private int currentpercent = -1;
	private boolean isprepared = false;
	private WakeLock wakeLock;
	private int currentpos = 0;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				if (msg.what == 0) {
					int currentposition = mediaPlayer.getCurrentPosition();

					if (duration > 0) {
						long pos = seekBar.getMax() * currentposition
								/ duration;
						if (isseek) {
							isseek = false;
						} else {
							seekBar.setProgress((int) pos);
						}
					}
				} else if (msg.what == 1) {
					showDialog(0);
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
	private Timer timer = new Timer();
	private boolean taskrun = true;
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!taskrun) {
				return;
			}
			if (mediaPlayer == null) {
				return;
			}
			if (mediaPlayer != null) {
				if (mediaPlayer.isPlaying() && seekBar.isPressed() == false) {
					handler.sendEmptyMessage(0);
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.mediaplayer);
		context = this;

		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(true);
		progressDialog.setTitle("视频加载中");
		progressDialog.setMessage("客官请稍后...");
		progressDialog.show();
		Window window = progressDialog.getWindow();
		WindowManager.LayoutParams wLayoutParams = window.getAttributes();
		wLayoutParams.alpha = 0.7f;
		wLayoutParams.width = LayoutParams.WRAP_CONTENT;
		wLayoutParams.height = LayoutParams.WRAP_CONTENT;
		window.setAttributes(wLayoutParams);

		progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
				}
				return false;
			}
		});
		init();

		if (getIntent() != null) {
			urlString = getIntent().getStringExtra(MEDIA_PATH);
			mediatype = getIntent().getStringExtra(MEDIA_TYPE);
			mediatitle = getIntent().getStringExtra(MEDIA_TITLE);

			if (TextUtils.isEmpty(mediatitle)) {
				if (urlString.contains("/")) {
					title.setText(urlString.substring(urlString
							.lastIndexOf("/") + 1));
				}
			} else {
				title.setText(mediatitle);
			}

			if ("audio".equals(mediatype)) {
				surfaceView.setBackgroundResource(R.drawable.music);
				surfaceHolder.setFixedSize(400, 400);
			} else if ("video".equals(mediatype)) {
				surfaceView.setKeepScreenOn(true);
				surfaceView.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							if (exit) {
								leave();
							} else {
								back();
							}
						}
						return false;
					}
				});
			}
		}

		mediaPlayer = new MediaPlayer();

		mediaPlayer
				.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					@Override
					public void onBufferingUpdate(MediaPlayer mp, int percent) {
						// TODO Auto-generated method stub
						// System.out.println("---onBufferingUpdate------percent="
						// + percent + "-------");

						if (percent != currentpercent) {
							currentpercent = percent;
							seekBar.setSecondaryProgress(percent
									* seekBar.getMax() / 100);
							if (percent < 100) {
								mediaplayerbuffer.setText("正在缓冲：" + percent
										+ "%");
							} else {
								mediaplayerbuffer.setText("正在播放");
							}
						}
					}
				});

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				mediaPlayer.pause();
				mediaPlayer.seekTo(position * duration / seekBar.getMax());
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				// System.out.println("---onProgressChanged----progress="
				// + progress + "--fromUser=" + fromUser + "---");
				position = progress;
				int length = position * duration / seekBar.getMax() / 1000;
				mediaprogress.setText(decimalFormat.format(length / 60) + ":"
						+ decimalFormat.format(length % 60));
			}
		});

		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				System.out.println("-------onPrepared---------");
				isprepared = true;
				mediaplayerbuffer.setText("正在播放");
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				duration = mediaPlayer.getDuration();
				videolength.setText(decimalFormat.format(duration / 60000)
						+ ":" + decimalFormat.format((duration / 1000) % 60));

				if (currentpos > 0) {
					mediaPlayer.seekTo(currentpos);
				}
				mediaPlayer.start();
				taskrun = true;
				if (oncreate) {
					timer.schedule(task, 0, 1000);
				}
				playButton.setBackgroundResource(R.drawable.parsestyle);
			}
		});

		mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

			@Override
			public void onSeekComplete(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mediaPlayer.start();
				isseek = true;
				playButton.setBackgroundResource(R.drawable.parsestyle);
			}
		});

		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				finish();
			}
		});

		mediaPlayer
				.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {

					@Override
					public void onVideoSizeChanged(MediaPlayer mp, int width,
							int height) {
						// TODO Auto-generated method stub
						if (width > 0) {
							if (duration <= 0) {
								duration = mediaPlayer.getDuration();
								videolength.setText(decimalFormat
										.format(duration / 60000)
										+ ":"
										+ decimalFormat
												.format((duration / 1000) % 60));
							}
						}

						if ("video".equals(mediatype)) {
							if (width > height) {
								setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
								calculatewh(width, height);
								surfaceHolder.setFixedSize((int) videowidth,
										(int) videoheight);
							} else {
								setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
								calculatewh(width, height);
								surfaceHolder.setFixedSize((int) videowidth,
										(int) videoheight);
							}
						}
					}
				});

	}

	private void error() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		handler.sendEmptyMessage(1);
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(context).setTitle("不支持的媒体文件类型")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated
						// method stub
						finish();
					}
				}).create();
	}

	private void calculatewh(int width, int height) {
		screenwidth = getWindowManager().getDefaultDisplay().getWidth();
		screenheight = getWindowManager().getDefaultDisplay().getHeight();
		float size = (screenwidth * 1.0f) / screenheight;
		if ((width * 1.0f / height) > size) {
			videowidth = screenwidth;
			videoheight = videowidth * height / width;
		} else {
			videoheight = screenheight;
			videowidth = videoheight * width / height;
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		oncreate = true;
		decimalFormat = new DecimalFormat("00");
		topin = AnimationUtils.loadAnimation(context, R.anim.topin);
		topout = AnimationUtils.loadAnimation(context, R.anim.topout);
		bottomin = AnimationUtils.loadAnimation(context, R.anim.bottomin);
		bottomout = AnimationUtils.loadAnimation(context, R.anim.bottomout);
		topin.setFillAfter(true);
		topout.setFillAfter(true);
		bottomin.setFillAfter(true);
		bottomout.setFillAfter(true);

		title = (TextView) findViewById(R.id.mediaplayertitle);
		mediaprogress = (TextView) findViewById(R.id.mediaplyervideoprogress);
		videolength = (TextView) findViewById(R.id.mediaplayervideolength);
		mediaplayerbuffer = (TextView) findViewById(R.id.mediaplayerbuffer);
		seekBar = (SeekBar) findViewById(R.id.medaplayervideoseekbar);
		lastButton = (ImageButton) findViewById(R.id.mediaplayerlast);
		nextButton = (ImageButton) findViewById(R.id.mediaplayernext);
		playButton = (ImageButton) findViewById(R.id.mediaplayerplay);
		bottomLayout = (RelativeLayout) findViewById(R.id.mediaplayerbottom);
		surfaceView = (SurfaceView) findViewById(R.id.mediaplayersurface);

		playButton.setOnClickListener(listener);

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);

		wakeLock = ((PowerManager) this.getSystemService(Context.POWER_SERVICE))
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");

	}

	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.mediaplayerplay) {

				if (mediaPlayer.isPlaying()) {
					mediaPlayer.pause();
					playButton.setBackgroundResource(R.drawable.playstyle);
					taskrun = false;
					if (wakeLock != null) {
						if (wakeLock.isHeld()) {
							// 屏幕常亮关闭
							wakeLock.acquire(60000);
						}
					}
				} else {
					mediaPlayer.start();
					playButton.setBackgroundResource(R.drawable.parsestyle);
					taskrun = true;
					if (wakeLock != null) {
						wakeLock.acquire();
					}
				}
			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		oncreate = false;
		if (mediaPlayer != null) {
			taskrun = false;
			currentpos = mediaPlayer.getCurrentPosition();
			mediaPlayer.stop();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					mediaPlayer.reset();
				}
			}).start();
		}
		if (wakeLock != null) {
			if (wakeLock.isHeld()) {
				// 屏幕常亮关闭
				wakeLock.acquire(60000);
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!exit) {
			back();
		}
		mediaplayerbuffer.setText("加载中...");
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		if (wakeLock != null) {
			wakeLock.acquire();
		}
	}

	private void back() {
		exit = true;
		title.startAnimation(topin);
		bottomLayout.startAnimation(bottomin);
	}

	private void leave() {
		exit = false;
		title.startAnimation(topout);
		bottomLayout.startAnimation(bottomout);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		surfaceHolder = holder;
		if (surfaceHolder != null) {
			mediaPlayer.setDisplay(surfaceHolder);
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.setDataSource(context, Uri.parse(urlString));
					mediaPlayer.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					error();
				}
			}
		}).start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		task.cancel();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mediaPlayer.stop();
				mediaPlayer.release();
			}
		}).start();
		System.gc();
	}

}

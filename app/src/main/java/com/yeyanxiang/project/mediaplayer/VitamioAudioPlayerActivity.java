package com.yeyanxiang.project.mediaplayer;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.widget.VideoView;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * 
 * Create on 2013-5-7 上午10:25:01 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: vitamio插件支持常见协议
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * @MEDIA_PATH 播放路径
 * @MEDIA_TYPE 媒体类型 audio 和video
 * @MEDIA_TITLE 显示名称
 */
public class VitamioAudioPlayerActivity extends Activity implements
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
	private long duration;
	private boolean isseek = false;
	private int currentpercent = -1;
	private TextView mediaplayerbuffer;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try {
				if (msg.what == 0) {
					long currentposition = mediaPlayer.getCurrentPosition();
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

		if (LibsChecker.checkVitamioLibs(this)) {
			context = this;
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(true);
			progressDialog.setTitle("媒体加载中");
			progressDialog.setMessage("客官请稍后...");
			progressDialog.show();
			Window window = progressDialog.getWindow();
			WindowManager.LayoutParams wLayoutParams = window.getAttributes();
			wLayoutParams.alpha = 0.7f;
			wLayoutParams.width = LayoutParams.WRAP_CONTENT;
			wLayoutParams.height = LayoutParams.WRAP_CONTENT;
			window.setAttributes(wLayoutParams);

			progressDialog
					.setOnKeyListener(new DialogInterface.OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							// TODO Auto-generated method stub
							if (keyCode == KeyEvent.KEYCODE_BACK) {
								dialog.dismiss();
							}
							return false;
						}
					});
			if (getIntent() != null) {
				urlString = getIntent().getStringExtra(MEDIA_PATH);
				mediatype = getIntent().getStringExtra(MEDIA_TYPE);
				mediatitle = getIntent().getStringExtra(MEDIA_TITLE);
			}
			setContentView(R.layout.mediaplayer);
			init();
			surfaceView.setBackgroundResource(R.drawable.music);
			surfaceHolder.setFixedSize(400, 400);
			if (TextUtils.isEmpty(mediatitle)) {
				if (urlString.contains("/")) {
					title.setText(urlString.substring(urlString
							.lastIndexOf("/") + 1));
				}
			} else {
				title.setText(mediatitle);
			}

			mediaPlayer = new MediaPlayer(context);

			mediaPlayer
					.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

						@Override
						public void onBufferingUpdate(MediaPlayer mp,
								int percent) {
							// TODO Auto-generated method stub

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
					mediaPlayer.start();
					playButton.setBackgroundResource(R.drawable.parsestyle);
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
					int length = (int) (position * duration / seekBar.getMax() / 1000);
					mediaprogress.setText(decimalFormat.format(length / 60)
							+ ":" + decimalFormat.format(length % 60));
				}
			});

			mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					System.out.println("-------onPrepared---------");
					mediaplayerbuffer.setText("正在播放");
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					mediaPlayer.start();
					if (urlString.contains("mms://")) {
						videolength.setVisibility(View.GONE);
						mediaprogress.setVisibility(View.GONE);
						seekBar.setVisibility(View.INVISIBLE);
					} else {
						duration = mediaPlayer.getDuration();
						videolength.setText(decimalFormat
								.format(duration / 60000)
								+ ":"
								+ decimalFormat.format((duration / 1000) % 60));
						timer.schedule(task, 0, 1000);
					}
					playButton.setBackgroundResource(R.drawable.parsestyle);
				}
			});

			mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

				@Override
				public void onSeekComplete(MediaPlayer mp) {
					// TODO Auto-generated method stub
					isseek = true;
				}
			});

			mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
				}
			});

			mediaPlayer.setOnErrorListener(new OnErrorListener() {

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					// TODO Auto-generated method stub
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
					Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
					return false;
				}
			});

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					Looper.prepare();
					try {
						mediaPlayer.setDisplay(surfaceHolder);
						mediaPlayer.setDataSource(context, Uri.parse(urlString));
						mediaPlayer.prepare();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						error();
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
		} else {
			finish();
		}

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

	private void init() {
		// TODO Auto-generated method stub
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
		mediaplayerbuffer.setText("加载中...");
		seekBar = (SeekBar) findViewById(R.id.medaplayervideoseekbar);
		lastButton = (ImageButton) findViewById(R.id.mediaplayerlast);
		nextButton = (ImageButton) findViewById(R.id.mediaplayernext);
		playButton = (ImageButton) findViewById(R.id.mediaplayerplay);
		bottomLayout = (RelativeLayout) findViewById(R.id.mediaplayerbottom);
		surfaceView = (SurfaceView) findViewById(R.id.mediaplayersurface);

		playButton.setOnClickListener(listener);

		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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
				} else {
					mediaPlayer.start();
					playButton.setBackgroundResource(R.drawable.parsestyle);
					taskrun = true;
				}
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		if (mediaPlayer != null) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					timer.cancel();
					task.cancel();
					mediaPlayer.stop();
					mediaPlayer.reset();
					mediaPlayer.release();
				}
			}).start();
		}
		System.gc();
	}

}

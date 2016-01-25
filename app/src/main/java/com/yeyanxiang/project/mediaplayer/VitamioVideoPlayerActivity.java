package com.yeyanxiang.project.mediaplayer;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yeyanxiang.project.R;

import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnBufferingUpdateListener;
import io.vov.vitamio.MediaPlayer.OnCompletionListener;
import io.vov.vitamio.MediaPlayer.OnErrorListener;
import io.vov.vitamio.MediaPlayer.OnInfoListener;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import io.vov.vitamio.MediaPlayer.OnSeekCompleteListener;
import io.vov.vitamio.widget.VideoView;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView.OnSeekStartListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
public class VitamioVideoPlayerActivity extends Activity implements
		View.OnClickListener {
	public static final String VIDEOSTREAM = "VIDEOSTREAM";
	public static final String FILENAME = "FILENAME";
	private Context context;
	private boolean flag = false;
	private ProgressDialog progressDialog;
	private DbUtil dbUtil;
	private String stream;
	private String filename;
	private int progress = -1;

	@ViewInject(R.id.loadlayout)
	private LinearLayout loadlayout;
	@ViewInject(R.id.loadingprogress)
	private TextView loadprgress;
	@ViewInject(R.id.vitamio_videoview)
	private VideoView videoView;
	@ViewInject(R.id.vitamio_mediacontroller)
	private View mediacontrollerview;
	private MediaController mediaController;
	@ViewInject(R.id.rew)
	private ImageButton rew;
	@ViewInject(R.id.ffwd)
	private ImageButton ffwd;
	@ViewInject(R.id.operation_volume_brightness)
	private View mVolumeBrightnessLayout;
	@ViewInject(R.id.operation_bg)
	private ImageView mOperationBg;
	@ViewInject(R.id.operation_percent)
	private ImageView mOperationPercent;
	private AudioManager mAudioManager;
	/** 最大声音 */
	private int mMaxVolume;
	/** 当前声音 */
	private int mVolume = -1;
	/** 当前亮度 */
	private float mBrightness = -1f;
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		dbUtil = new DbUtil(this);
		if (LibsChecker.checkVitamioLibs(this)) {
			if (getIntent() != null) {
				stream = getIntent().getStringExtra(VIDEOSTREAM);
				filename = getIntent().getStringExtra(FILENAME);
				if (TextUtils.isEmpty(stream)) {
					error("参数有误");
				} else {
					flag = true;
					setContentView(R.layout.vitamiovideoview);
					ViewUtils.inject(this);

					mediaController = new MediaController(context,
							mediacontrollerview);
					mediaController.setInstantSeeking(false);
					videoView.setMediaController(mediaController);
					if (!TextUtils.isEmpty(filename)) {
						mediaController.setFileName(filename);
					}
					rew.setOnClickListener(this);
					ffwd.setOnClickListener(this);

					mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					mMaxVolume = mAudioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

					mGestureDetector = new GestureDetector(this,
							new MyGestureListener());

					videoView.setTouchable(false);

					progressDialog = new ProgressDialog(context);
					progressDialog
							.setProgressStyle(ProgressDialog.STYLE_SPINNER);
					progressDialog.setCancelable(true);
					progressDialog.setTitle("视频加载中");
					progressDialog.setMessage("请稍后...");
					progressDialog.show();
					Window window = progressDialog.getWindow();
					WindowManager.LayoutParams wLayoutParams = window
							.getAttributes();
					wLayoutParams.gravity = Gravity.CENTER;
					wLayoutParams.alpha = 0.7f;
					wLayoutParams.width = LayoutParams.WRAP_CONTENT;
					wLayoutParams.height = LayoutParams.WRAP_CONTENT;
					window.setAttributes(wLayoutParams);

					videoView.setVideoPath(stream);
					final long process = dbUtil.getvideoprocess(stream);
					if (process > 0) {
						videoView.seekTo(process);
					}
					videoView.requestFocus();
					// videoView.start();

					videoView
							.setOnCompletionListener(new OnCompletionListener() {

								@Override
								public void onCompletion(MediaPlayer mp) {
									// TODO Auto-generated method stub
									System.out.println("----onCompletion-----");
									finish();
								}
							});

					loadprgress.setVisibility(View.VISIBLE);
					videoView
							.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

								@Override
								public void onBufferingUpdate(MediaPlayer mp,
										int percent) {
									// TODO Auto-generated method stub

									if (percent != progress
											&& percent > progress) {
										progress = percent;
										loadprgress.setText(percent + " %");
									}
								}
							});

					videoView.setOnPreparedListener(new OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer mp) {
							// TODO Auto-generated method stub
							System.out.println("-------onPrepared--------");
							if (progressDialog.isShowing()) {
								progressDialog.dismiss();
							}

							loadlayout.setVisibility(View.GONE);
						}
					});
					videoView.setOnErrorListener(new OnErrorListener() {

						@Override
						public boolean onError(MediaPlayer mp, int what,
								int extra) {
							// TODO Auto-generated method stub
							if (progressDialog != null
									&& progressDialog.isShowing()) {
								progressDialog.dismiss();
							}
							System.out.println("------onError--" + what + "--"
									+ extra + "--");
							return false;
						}
					});

					videoView.setOnInfoListener(new OnInfoListener() {

						@Override
						public boolean onInfo(MediaPlayer mp, int what,
								int extra) {
							// TODO Auto-generated method stub
							if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
								progress = -1;
								loadlayout.setVisibility(View.VISIBLE);
							} else if (what == MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED) {
								// loadlayout.setVisibility(View.GONE);
							} else if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
								loadlayout.setVisibility(View.GONE);
							} else if (what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
								loadlayout.setVisibility(View.GONE);
							}
							return false;
						}
					});

					videoView.setOnSeekStartListener(new OnSeekStartListener() {

						@Override
						public void onSeekStart(MediaPlayer mp) {
							// TODO Auto-generated method stub
							progress = -1;
							loadlayout.setVisibility(View.VISIBLE);
						}
					});

					videoView
							.setOnSeekCompleteListener(new OnSeekCompleteListener() {

								@Override
								public void onSeekComplete(MediaPlayer mp) {
									// TODO Auto-generated method stub
									progress = -1;
									loadlayout.setVisibility(View.GONE);
								}
							});
				}
			} else {
				error("参数有误");
			}
		} else {
			// error("插件初始化失败");
		}
	}

	private void error(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		mGestureDetector.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_UP) {
			endGesture();
		}
		return true;
	}

	/** 手势结束 */
	private void endGesture() {
		mVolume = -1;
		mBrightness = -1f;

		// 隐藏
		mDismissHandler.removeMessages(0);
		// mDismissHandler.sendEmptyMessage(0);
		mDismissHandler.sendEmptyMessageDelayed(0, 500);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.rew) {
			videoView.reverse(-1);
		} else if (v.getId() == R.id.ffwd) {
			videoView.advance(-1);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (flag) {
			videoView.resume();
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (flag) {
			long process = videoView.getCurrentPosition();
			if (process > 0) {
				if (process == videoView.getDuration()) {
					dbUtil.updatevideoprocess(stream, 0);
				} else {
					dbUtil.updatevideoprocess(stream, process);
				}
			}
			videoView.pause();
		}
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (videoView != null) {
			videoView.pause();
			videoView.stopPlayback();
		}
	}

	private class MyGestureListener extends SimpleOnGestureListener {

		/** 双击 */
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return super.onDoubleTap(e);
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			videoView.toggleMediaControlsVisiblity();
			return false;
		}

		/** 滑动 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			try {
				float mOldX = e1.getX(), mOldY = e1.getY();
				int y = (int) e2.getRawY();

				Display disp = getWindowManager().getDefaultDisplay();
				int windowWidth = disp.getWidth();
				int windowHeight = disp.getHeight();

				if (mOldX > windowWidth * 3.0 / 5)// 右边滑动
					onVolumeSlide((mOldY - y) / windowHeight);
				else if (mOldX < windowWidth * 2.0 / 5.0)// 左边滑动
					onBrightnessSlide((mOldY - y) / windowHeight);
			} catch (Exception e) {
				// TODO: handle exception
			}
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	/** 定时隐藏 */
	private Handler mDismissHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mVolumeBrightnessLayout.setVisibility(View.GONE);
		}
	};

	/**
	 * 滑动改变声音大小
	 * 
	 * @param percent
	 */
	private void onVolumeSlide(float percent) {
		if (mVolume == -1) {
			mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (mVolume < 0)
				mVolume = 0;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_volumn_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}

		int index = (int) (percent * mMaxVolume) + mVolume;
		if (index > mMaxVolume)
			index = mMaxVolume;
		else if (index < 0)
			index = 0;

		// 变更声音
		mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

		// 变更进度条
		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = findViewById(R.id.operation_full).getLayoutParams().width
				* index / mMaxVolume;
		mOperationPercent.setLayoutParams(lp);
	}

	/**
	 * 滑动改变亮度
	 * 
	 * @param percent
	 */
	private void onBrightnessSlide(float percent) {
		if (mBrightness < 0) {
			mBrightness = getWindow().getAttributes().screenBrightness;
			if (mBrightness <= 0.00f)
				mBrightness = 0.50f;
			if (mBrightness < 0.01f)
				mBrightness = 0.01f;

			// 显示
			mOperationBg.setImageResource(R.drawable.video_brightness_bg);
			mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
		}
		WindowManager.LayoutParams lpa = getWindow().getAttributes();
		lpa.screenBrightness = mBrightness + percent;
		if (lpa.screenBrightness > 1.0f)
			lpa.screenBrightness = 1.0f;
		else if (lpa.screenBrightness < 0.01f)
			lpa.screenBrightness = 0.01f;
		getWindow().setAttributes(lpa);

		ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
		lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
		mOperationPercent.setLayoutParams(lp);
	}
}

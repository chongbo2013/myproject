package com.yeyanxiang.project.mediaplayer;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;
import com.yeyanxiang.util.SharedPfsUtil;
import com.weiny.MmsPlayerActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

/**
 * 
 * Create on 2013-5-7 上午10:22:41 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 多媒体播放(支持 http、rtsp、rtmp、m3u8、mms协议)
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class MediaPlayActivity extends Activity {
	private Context context;
	private Button LocalMediaplay;
	private SharedPfsUtil sharedPfsUtil;

	private EditText httpVideoEditText;
	private ImageButton httpVideoplay;

	private EditText httpAudioEditText;
	private ImageButton httpAudioplay;

	private EditText RtspVideoEditText;
	private ImageButton RtspVideoplay;

	private EditText RtspAudioEditText;
	private ImageButton RtspAudioplay;

	private EditText RtmpVideoEditText;
	private ImageButton RtmpVideoplay;

	private EditText RtmpAudioEditText;
	private ImageButton RtmpAudioplay;

	private EditText m3u8VideoEditText;
	private ImageButton m3u8Videoplay;

	private EditText m3u8AudioEditText;
	private ImageButton m3u8Audioplay;

	private EditText MmsAudioEditText;
	private ImageButton MmsAudioplay;

	private final String HTTPVideo = "HTTPVideo";
	private final String HTTPAudio = "HTTPAudio";
	private final String RTSPVideo = "RTSPVideo";
	private final String RTSPAudio = "RTSPAudio";
	private final String RTMPVideo = "RTMPVideo";
	private final String RTMPAudio = "RTMPAudio";
	private final String M3U8Video = "M3U8Video";
	private final String M3U8Audio = "M3U8Audio";
	private final String MMSAudio = "MMSAudio";

	private String[] player;
	private String url = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mediaplay);
		context = this;
		sharedPfsUtil = new SharedPfsUtil(context, PubUtil.USER_DATA);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		LocalMediaplay = (Button) findViewById(R.id.LocalMediaplay);

		httpVideoEditText = (EditText) findViewById(R.id.httpVideoEditText);
		httpAudioEditText = (EditText) findViewById(R.id.httpAudioEditText);
		RtspVideoEditText = (EditText) findViewById(R.id.rtspVideoEditText);
		RtspAudioEditText = (EditText) findViewById(R.id.rtspAudioEditText);
		RtmpVideoEditText = (EditText) findViewById(R.id.rtmpVideoEditText);
		RtmpAudioEditText = (EditText) findViewById(R.id.rtmpAudioEditText);
		m3u8VideoEditText = (EditText) findViewById(R.id.M3u8VideoEditText);
		m3u8AudioEditText = (EditText) findViewById(R.id.m3u8AudioEditText);
		MmsAudioEditText = (EditText) findViewById(R.id.MmsAudioEditText);

		httpVideoplay = (ImageButton) findViewById(R.id.httpVideoplay);
		httpAudioplay = (ImageButton) findViewById(R.id.httpAudioplay);
		RtspVideoplay = (ImageButton) findViewById(R.id.rtspVideoplay);
		RtspAudioplay = (ImageButton) findViewById(R.id.rtspAudioplay);
		RtmpVideoplay = (ImageButton) findViewById(R.id.rtmpVideoplay);
		RtmpAudioplay = (ImageButton) findViewById(R.id.rtmpAudioplay);
		m3u8Videoplay = (ImageButton) findViewById(R.id.M3u8Videoplay);
		m3u8Audioplay = (ImageButton) findViewById(R.id.m3u8Audioplay);
		MmsAudioplay = (ImageButton) findViewById(R.id.mmsAudioplay);

		LocalMediaplay.setOnClickListener(listener);
		httpVideoplay.setOnClickListener(listener);
		httpAudioplay.setOnClickListener(listener);
		RtspVideoplay.setOnClickListener(listener);
		RtspAudioplay.setOnClickListener(listener);
		RtmpVideoplay.setOnClickListener(listener);
		RtmpAudioplay.setOnClickListener(listener);
		m3u8Videoplay.setOnClickListener(listener);
		m3u8Audioplay.setOnClickListener(listener);
		MmsAudioplay.setOnClickListener(listener);

		httpVideoEditText.setText(sharedPfsUtil.getValue(HTTPVideo, "http://"));
		httpAudioEditText.setText(sharedPfsUtil.getValue(HTTPAudio, "http://"));
		RtspVideoEditText.setText(sharedPfsUtil.getValue(RTSPVideo, "rtsp://"));
		RtspAudioEditText.setText(sharedPfsUtil.getValue(RTSPAudio, "rtsp://"));
		RtmpVideoEditText.setText(sharedPfsUtil.getValue(RTMPVideo, "rtmp://"));
		RtmpAudioEditText.setText(sharedPfsUtil.getValue(RTMPAudio, "rtmp://"));
		m3u8VideoEditText.setText(sharedPfsUtil.getValue(M3U8Video, "http://"));
		m3u8AudioEditText.setText(sharedPfsUtil.getValue(M3U8Audio, "http://"));
		MmsAudioEditText.setText(sharedPfsUtil.getValue(MMSAudio, "mms://"));
	}

	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (v.getId() == R.id.LocalMediaplay) {
				startActivity(new Intent(context, FileListActivity.class));
			} else {
				switch (v.getId()) {
				case R.id.httpVideoplay:
					url = httpVideoEditText.getText().toString().trim();
					sharedPfsUtil.putValue(HTTPVideo, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.httpAudioplay:
					url = httpAudioEditText.getText().toString().trim();
					sharedPfsUtil.putValue(HTTPAudio, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.rtspVideoplay:
					url = RtspVideoEditText.getText().toString().trim();
					sharedPfsUtil.putValue(RTSPVideo, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.rtspAudioplay:
					url = RtspAudioEditText.getText().toString().trim();
					sharedPfsUtil.putValue(RTSPAudio, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.rtmpVideoplay:
					url = RtmpVideoEditText.getText().toString().trim();
					sharedPfsUtil.putValue(RTMPVideo, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.rtmpAudioplay:
					url = RtmpAudioEditText.getText().toString().trim();
					sharedPfsUtil.putValue(RTMPAudio, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.M3u8Videoplay:
					url = m3u8VideoEditText.getText().toString().trim();
					sharedPfsUtil.putValue(M3U8Video, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.m3u8Audioplay:
					url = m3u8AudioEditText.getText().toString().trim();
					sharedPfsUtil.putValue(M3U8Audio, url);
					player = new String[] { "流媒体播放器", "MediaPlayer 播放",
							"Vitamio 播放" };
					break;
				case R.id.mmsAudioplay:
					url = MmsAudioEditText.getText().toString().trim();
					sharedPfsUtil.putValue(MMSAudio, url);
					player = new String[] { "MMSPlayer 播放", "Vitamio 播放" };
					break;

				default:
					break;
				}
				onCreateDialog(0).show();
			}
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		return new AlertDialog.Builder(context).setItems(player,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if ("流媒体播放器".equals(player[which])) {
							streamplayer();
						} else if ("MediaPlayer 播放".equals(player[which])) {
							mediaplayer();
						} else if ("Vitamio 播放".equals(player[which])) {
							vitamioplayer();
						} else if ("MMSPlayer 播放".equals(player[which])) {
							mmsplayer();
						} else if ("Vitamio2 播放".equals(player[which])) {
							// vitamioplayer1();
						}
					}

				}).create();
	}

	private void streamplayer() {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (url.contains("rtsp://")) {
				intent.setDataAndType(Uri.parse(url), null);
			} else {
				if (url.contains(".mp3")) {
					intent.setDataAndType(Uri.parse(url), "audio/*");
				} else {
					intent.setDataAndType(Uri.parse(url), "video/*");
				}
			}
			startActivity(intent);
		} catch (Exception e) {
			// TODO: handle exception
			PubUtil.showText(context, "播放失败");
		}

	}

	private void mediaplayer() {
		Intent intent2 = new Intent(context, MediaPlayerActivity.class);
		intent2.putExtra(MediaPlayerActivity.MEDIA_PATH, url);
		if (url.contains(".mp3")) {
			intent2.putExtra(MediaPlayerActivity.MEDIA_TYPE, "audio");
		} else {
			intent2.putExtra(MediaPlayerActivity.MEDIA_TYPE, "video");
		}
		startActivity(intent2);
	}

	private void vitamioplayer() {
		if (url.contains(".mp3") || url.contains("mms://")) {
			Intent intent2 = new Intent(context, VitamioAudioPlayerActivity.class);
			intent2.putExtra(MediaPlayerActivity.MEDIA_PATH, url);
			intent2.putExtra(MediaPlayerActivity.MEDIA_TYPE, "audio");
			startActivity(intent2);
		} else {
			Intent intent4 = new Intent(context,
					VitamioVideoPlayerActivity.class);
			intent4.putExtra(VitamioVideoPlayerActivity.VIDEOSTREAM, url);
			startActivity(intent4);
		}
	}

	private void vitamioplayer1() {
		if (url.contains(".mp3") || url.contains("mms://")) {
			Intent intent2 = new Intent(context, VitamioAudioPlayerActivity.class);
			intent2.putExtra(MediaPlayerActivity.MEDIA_PATH, url);
			intent2.putExtra(MediaPlayerActivity.MEDIA_TYPE, "audio");
			startActivity(intent2);
		} else {
			// Intent intent4 = new Intent(context, VideoActivity.class);
			// intent4.setData(Uri.parse(url));
			// startActivity(intent4);
		}
	}

	private void mmsplayer() {
		Intent intent = new Intent(context, MmsPlayerActivity.class);
		intent.putExtra(MmsPlayerActivity.MMSPATH, url);
		startActivity(intent);
	}
}

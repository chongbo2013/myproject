package com.yeyanxiang.project.mediaplayer;

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

import com.weiny.MmsPlayerActivity;
import com.yeyanxiang.project.R;
import com.yeyanxiang.project.activity.BaseActivity;
import com.yeyanxiang.project.pub.util.PubUtil;

/**
 * Create on 2013-5-7 上午10:22:41 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * <p/>
 * 简介: 多媒体播放(支持 http、rtsp、rtmp、m3u8、mms协议)
 *
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 */
public class MediaPlayActivity extends BaseActivity {
    private Context context;
    private Button localMediaPlayBtn;

    private EditText httpVideoEditText;
    private ImageButton httpVideoPlayBtn;

    private EditText httpAudioEditText;
    private ImageButton httpAudioPlayBtn;

    private EditText rtspVideoEditText;
    private ImageButton RtspVideoPlayBtn;

    private EditText rtspAudioEditText;
    private ImageButton RtspAudioPlayBtn;

    private EditText rtmpVideoEditText;
    private ImageButton RtmpVideoPlayBtn;

    private EditText rtmpAudioEditText;
    private ImageButton RtmpAudioPlayBtn;

    private EditText m3u8VideoEditText;
    private ImageButton m3u8VideoPlayBtn;

    private EditText m3u8AudioEditText;
    private ImageButton m3u8AudioPlayBtn;

    private EditText mmsAudioEditText;
    private ImageButton mmsAudioPlayBtn;

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
    public String getTAG() {
        return "MediaPlayActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mediaplay);
        context = this;
        init();
    }

    private void init() {
        // TODO Auto-generated method stub
        localMediaPlayBtn = (Button) findViewById(R.id.LocalMediaplay);

        httpVideoEditText = (EditText) findViewById(R.id.httpVideoEditText);
        httpAudioEditText = (EditText) findViewById(R.id.httpAudioEditText);
        rtspVideoEditText = (EditText) findViewById(R.id.rtspVideoEditText);
        rtspAudioEditText = (EditText) findViewById(R.id.rtspAudioEditText);
        rtmpVideoEditText = (EditText) findViewById(R.id.rtmpVideoEditText);
        rtmpAudioEditText = (EditText) findViewById(R.id.rtmpAudioEditText);
        m3u8VideoEditText = (EditText) findViewById(R.id.M3u8VideoEditText);
        m3u8AudioEditText = (EditText) findViewById(R.id.m3u8AudioEditText);
        mmsAudioEditText = (EditText) findViewById(R.id.MmsAudioEditText);

        httpVideoPlayBtn = (ImageButton) findViewById(R.id.httpVideoplay);
        httpAudioPlayBtn = (ImageButton) findViewById(R.id.httpAudioplay);
        RtspVideoPlayBtn = (ImageButton) findViewById(R.id.rtspVideoplay);
        RtspAudioPlayBtn = (ImageButton) findViewById(R.id.rtspAudioplay);
        RtmpVideoPlayBtn = (ImageButton) findViewById(R.id.rtmpVideoplay);
        RtmpAudioPlayBtn = (ImageButton) findViewById(R.id.rtmpAudioplay);
        m3u8VideoPlayBtn = (ImageButton) findViewById(R.id.M3u8Videoplay);
        m3u8AudioPlayBtn = (ImageButton) findViewById(R.id.m3u8Audioplay);
        mmsAudioPlayBtn = (ImageButton) findViewById(R.id.mmsAudioplay);

        localMediaPlayBtn.setOnClickListener(listener);
        httpVideoPlayBtn.setOnClickListener(listener);
        httpAudioPlayBtn.setOnClickListener(listener);
        RtspVideoPlayBtn.setOnClickListener(listener);
        RtspAudioPlayBtn.setOnClickListener(listener);
        RtmpVideoPlayBtn.setOnClickListener(listener);
        RtmpAudioPlayBtn.setOnClickListener(listener);
        m3u8VideoPlayBtn.setOnClickListener(listener);
        m3u8AudioPlayBtn.setOnClickListener(listener);
        mmsAudioPlayBtn.setOnClickListener(listener);

        httpVideoEditText.setText(mSharedPfsUtil.getValue(HTTPVideo, "http://"));
        httpAudioEditText.setText(mSharedPfsUtil.getValue(HTTPAudio, "http://"));
        rtspVideoEditText.setText(mSharedPfsUtil.getValue(RTSPVideo, "rtsp://"));
        rtspAudioEditText.setText(mSharedPfsUtil.getValue(RTSPAudio, "rtsp://"));
        rtmpVideoEditText.setText(mSharedPfsUtil.getValue(RTMPVideo, "rtmp://"));
        rtmpAudioEditText.setText(mSharedPfsUtil.getValue(RTMPAudio, "rtmp://"));
        m3u8VideoEditText.setText(mSharedPfsUtil.getValue(M3U8Video, "http://"));
        m3u8AudioEditText.setText(mSharedPfsUtil.getValue(M3U8Audio, "http://"));
        mmsAudioEditText.setText(mSharedPfsUtil.getValue(MMSAudio, "mms://"));
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
                        mSharedPfsUtil.putValue(HTTPVideo, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.httpAudioplay:
                        url = httpAudioEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(HTTPAudio, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.rtspVideoplay:
                        url = rtspVideoEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(RTSPVideo, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.rtspAudioplay:
                        url = rtspAudioEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(RTSPAudio, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.rtmpVideoplay:
                        url = rtmpVideoEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(RTMPVideo, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.rtmpAudioplay:
                        url = rtmpAudioEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(RTMPAudio, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.M3u8Videoplay:
                        url = m3u8VideoEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(M3U8Video, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.m3u8Audioplay:
                        url = m3u8AudioEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(M3U8Audio, url);
                        player = new String[]{"流媒体播放器", "MediaPlayer 播放", "Vitamio 播放"};
                        break;
                    case R.id.mmsAudioplay:
                        url = mmsAudioEditText.getText().toString().trim();
                        mSharedPfsUtil.putValue(MMSAudio, url);
                        player = new String[]{"MMSPlayer 播放", "Vitamio 播放"};
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
        return new AlertDialog.Builder(context).setItems(player, new DialogInterface.OnClickListener() {

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
            Intent intent4 = new Intent(context, VitamioVideoPlayerActivity.class);
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

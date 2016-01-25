package com.yeyanxiang.project.webview;

import java.util.Date;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yeyanxiang.project.R;
import com.yeyanxiang.util.WebViewUtil;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
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
public class WebViewActivity extends Activity implements
		WebViewUtil.WebViewCallBack {
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webviewlayout);
		ViewUtils.inject(this);
		context = this;
		initwebview();
		initview();
		initpopuwindow();
	}

	private PopupWindow exitmenuwindow;
	private LayoutInflater inflater;

	private void initpopuwindow() {
		// TODO Auto-generated method stub
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.exitmenu, null);
		view.findViewById(R.id.exitbtn).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						finish();
					}
				});
		exitmenuwindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		exitmenuwindow.setAnimationStyle(R.style.popuStyle);
		exitmenuwindow.setBackgroundDrawable(new BitmapDrawable());

		view.setFocusableInTouchMode(true);
		view.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if ((keyCode == KeyEvent.KEYCODE_MENU)
						&& (exitmenuwindow.isShowing())) {
					exitmenuwindow.dismiss();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		// TODO Auto-generated method stub
		if (exitmenuwindow != null && !exitmenuwindow.isShowing()) {
			exitmenuwindow.showAtLocation(findViewById(R.id.webviewlayout),
					Gravity.BOTTOM, 0, 0);
		}
		return false;
	}

	private @ViewInject(R.id.urledit)
	EditText urlEdit;
	private @ViewInject(R.id.loadbtn)
	Button LoadBtn;
	private @ViewInject(R.id.loadprogress)
	ProgressBar progressBar;

	private void initview() {
		// TODO Auto-generated method stub
		LoadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String url = urlEdit.getText().toString().trim();
				if (!TextUtils.isEmpty(url)) {
					if (!url.startsWith("http://")
							&& !url.startsWith("rtsp://")) {
						url = "http://" + url;
						urlEdit.setText(url);
					}
					loadurl(url, false);
				}
			}
		});

		LoadBtn.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				String url = webView.getUrl();
				if (!TextUtils.isEmpty(url)) {
					loadurl(url, true);
				}
				return true;
			}
		});

		Toast.makeText(context, "长按Load刷新当前页", Toast.LENGTH_SHORT).show();
	}

	private @ViewInject(R.id.webview)
	WebView webView;
	private @ViewInject(R.id.webview_fullscreen_custom_content)
	FrameLayout mFullscreenContainer;
	private @ViewInject(R.id.webview_main_content)
	FrameLayout mContentView;
	private Intent intent;
	private WebViewUtil webViewUtil;

	private void initwebview() {
		intent = new Intent(Intent.ACTION_VIEW);
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		webView.setBackgroundColor(Color.TRANSPARENT);
		webViewUtil = new WebViewUtil(context);
		webViewUtil.initWebview(webView, mFullscreenContainer, mContentView,
				this, this);
	}

	private void loadurl(String url, boolean newflag) {
		if (url.startsWith("rtsp://")) {
			intent.setDataAndType(Uri.parse(url), null);
			if (play(intent)) {
				return;
			}
		} else if (url.startsWith("http://")) {
			if (url.endsWith(".mp4") || url.endsWith(".flv")
					|| url.endsWith(".mp4/playlist.m3u8")) {
				intent.setDataAndType(Uri.parse(url), "video/*");
				if (play(intent)) {
					return;
				}
			} else if (url.endsWith(".mp3")
					|| url.endsWith(".mp3/playlist.m3u8")) {
				intent.setDataAndType(Uri.parse(url), "audio/*");
				if (play(intent)) {
					return;
				}
			} else {
				if (newflag) {
					if (url.contains("?")) {
						if (!url.contains("time=")) {
							url = url + "&time=" + new Date();
						}
					} else {
						url = url + "?time=" + new Date();
					}
				}
				progressBar.setVisibility(View.VISIBLE);
				webView.loadUrl(url);
			}
		}
		System.out.println(url);
	}

	private boolean play(Intent intent) {
		try {
			startActivity(intent);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (!webViewUtil.onBack(webView, progressBar)) {
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		webView.setVisibility(View.GONE);
		webView.destroy();
		super.onDestroy();
	}

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// TODO Auto-generated method stub
		urlEdit.setText(url);
		loadurl(url, false);
		return true;
	}

	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		// TODO Auto-generated method stub
		switch (errorCode) {
		case 404:
			Toast.makeText(context, "找不到网页", Toast.LENGTH_SHORT).show();
			break;
		case 500:
			Toast.makeText(context, "网页内部错误", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		// TODO Auto-generated method stub
		progressBar.setProgress(newProgress);
		if (newProgress >= 100) {
			progressBar.setVisibility(View.GONE);
		}
	}

	@Override
	public void getContentWidth(String value) {
		// TODO Auto-generated method stub
		
	}

}

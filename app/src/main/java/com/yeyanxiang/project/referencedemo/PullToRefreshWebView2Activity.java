package com.yeyanxiang.project.referencedemo;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;
import com.yeyanxiang.project.R;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年4月10日
 * 
 * @简介
 */
public final class PullToRefreshWebView2Activity extends Activity implements
		OnRefreshListener<WebView> {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ptr_webview2);

		PullToRefreshWebView2 pullRefreshWebView = (PullToRefreshWebView2) findViewById(R.id.pull_refresh_webview2);
		pullRefreshWebView.setOnRefreshListener(this);

		WebView webView = pullRefreshWebView.getRefreshableView();
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new SampleWebViewClient());

		// We just load a prepared HTML page from the assets folder for this
		// sample, see that file for the Javascript implementation
		webView.loadUrl("file:///android_asset/ptr_webview2_sample.html");
	}

	private static class SampleWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
	}

	@Override
	public void onRefresh(final PullToRefreshBase<WebView> refreshView) {
		// This is very contrived example, we just wait 2 seconds, then call
		// onRefreshComplete()
		refreshView.postDelayed(new Runnable() {
			@Override
			public void run() {
				refreshView.onRefreshComplete();
			}
		}, 2 * 1000);
	}
}

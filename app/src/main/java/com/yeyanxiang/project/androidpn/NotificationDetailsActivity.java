package com.yeyanxiang.project.androidpn;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
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
public class NotificationDetailsActivity extends Activity {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationDetailsActivity.class);

	private String callbackActivityPackageName;

	private String callbackActivityClassName;

	public NotificationDetailsActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPrefs = this.getSharedPreferences(
				AndroidpnMainActivity.USERDATA, Context.MODE_PRIVATE);
		callbackActivityPackageName = sharedPrefs.getString(
				Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
		callbackActivityClassName = sharedPrefs.getString(
				Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");

		Intent intent = getIntent();
		String notificationId = intent
				.getStringExtra(Constants.NOTIFICATION_ID);
		String notificationApiKey = intent
				.getStringExtra(Constants.NOTIFICATION_API_KEY);
		String notificationTitle = intent
				.getStringExtra(Constants.NOTIFICATION_TITLE);
		String notificationMessage = intent
				.getStringExtra(Constants.NOTIFICATION_MESSAGE);
		String notificationUri = intent
				.getStringExtra(Constants.NOTIFICATION_URI);

		// Log.d(LOGTAG, "notificationId=" + notificationId);
		// Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
		// Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
		// Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
		// Log.d(LOGTAG, "notificationUri=" + notificationUri);

		// Display display = getWindowManager().getDefaultDisplay();
		// View rootView;
		// if (display.getWidth() > display.getHeight()) {
		// rootView = null;
		// } else {
		// rootView = null;
		// }

		View rootView = createView(notificationTitle, notificationMessage,
				notificationUri);
		setContentView(rootView);
	}

	private View createView(final String title, final String message,
			final String uri) {

		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(0xffeeeeee);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(5, 5, 5, 5);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		linearLayout.setLayoutParams(layoutParams);

		TextView textTitle = new TextView(this);
		textTitle.setText(title);
		textTitle.setTextSize(18);
		// textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		textTitle.setTextColor(0xff000000);
		textTitle.setGravity(Gravity.CENTER);

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 30, 30, 0);
		textTitle.setLayoutParams(layoutParams);
		linearLayout.addView(textTitle);

		TextView textDetails = new TextView(this);
		textDetails.setText(message);
		textDetails.setTextSize(14);
		// textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		textDetails.setTextColor(0xff333333);
		textDetails.setGravity(Gravity.CENTER);

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 10, 30, 20);
		textDetails.setLayoutParams(layoutParams);
		linearLayout.addView(textDetails);

		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				if (id == 1) {
					if (uri != null
							&& uri.length() > 0
							&& (uri.startsWith("http:")
									|| uri.startsWith("https:")
									|| uri.startsWith("tel:") || uri
										.startsWith("geo:"))) {
						NotificationDetailsActivity.this
								.startActivity(new Intent(Intent.ACTION_VIEW,
										Uri.parse(uri)));
					}
					NotificationDetailsActivity.this.finish();
				} else {
					// 获取剪贴板管理服务
					ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					// 将文本数据复制到剪贴板
					if (id == 2) {
						// 将文本数据复制到剪贴板
						cm.setText(title);
					} else if (id == 3) {
						cm.setText(message);
					} else if (id == 4) {
						cm.setText(uri);
					}

					Toast.makeText(NotificationDetailsActivity.this, "已复制",
							Toast.LENGTH_SHORT).show();
				}
			}
		};

		Button okButton = new Button(this);
		okButton.setId(1);
		okButton.setText("Ok");
		okButton.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		okButton.setOnClickListener(listener);
		okButton.setPadding(50, 10, 50, 10);
		linearLayout.addView(okButton);

		Button copytitleButton = new Button(this);
		copytitleButton.setId(2);
		copytitleButton.setText("copy Title");
		copytitleButton.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		copytitleButton.setOnClickListener(listener);

		Button copymessageButton = new Button(this);
		copymessageButton.setId(3);
		copymessageButton.setText("copy Message");
		copymessageButton.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		copymessageButton.setOnClickListener(listener);

		Button copyuriButton = new Button(this);
		copyuriButton.setId(4);
		copyuriButton.setText("copy Uri");
		copyuriButton.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		copyuriButton.setOnClickListener(listener);

		LinearLayout innerLayout = new LinearLayout(this);
		innerLayout.setOrientation(LinearLayout.HORIZONTAL);
		innerLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		innerLayout.addView(copytitleButton);
		innerLayout.addView(copymessageButton);
		innerLayout.addView(copyuriButton);

		linearLayout.addView(innerLayout);

		return linearLayout;
	}

}

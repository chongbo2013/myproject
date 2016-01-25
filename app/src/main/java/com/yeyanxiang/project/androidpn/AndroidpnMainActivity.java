package com.yeyanxiang.project.androidpn;

import org.androidpn.client.Constants;
import com.yeyanxiang.project.R;
import com.yeyanxiang.util.SharedPfsUtil;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
public class AndroidpnMainActivity extends Activity {
	public static final String USERDATA = "com.androidpn.copy.userdata";
	public static final String USERNAME = "USERNAME";
	public static final String SERVERIP = "serverip";
	public static final String SERVERPROT = "serverport";

	private EditText usernameEditText;
	private EditText serveripEditText;
	private EditText serverportEditText;
	private Button logButton;
	private SharedPfsUtil sharedPfsUtil;

	private static StringBuilder stringBuilder;
	private static TextView infoTextView;
	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				if (msg.what == Constants.START_CONNECT) {
					stringBuilder.append("连接服务器...");
				} else if (msg.what == Constants.CONNECT_SUCCESS) {
					stringBuilder.append("\n服务器连接成功。");
				} else if (msg.what == Constants.STSRT_LOGIN) {
					stringBuilder.append("\n开始登陆...");
				} else if (msg.what == Constants.LOGIN_SUCCESS) {
					stringBuilder.append("\n登陆成功。");
				}
				infoTextView.setText(stringBuilder);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.androidpn);

		serveripEditText = (EditText) findViewById(R.id.serveripedittexr);
		usernameEditText = (EditText) findViewById(R.id.usernameedittext);
		serverportEditText = (EditText) findViewById(R.id.serverportedittexr);
		logButton = (Button) findViewById(R.id.login);
		infoTextView = (TextView) findViewById(R.id.info);
		stringBuilder = new StringBuilder();

		sharedPfsUtil = new SharedPfsUtil(this, USERDATA);

		serveripEditText.setText(sharedPfsUtil
				.getValue(SERVERIP, "198.9.9.188"));
		serverportEditText.setText(sharedPfsUtil.getValue(SERVERPROT, 5222)
				+ "");

		usernameEditText
				.setText(sharedPfsUtil.getValue(USERNAME, "yeyanxiang"));

		logButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String ip = serveripEditText.getText().toString().trim();
				if (TextUtils.isEmpty(ip)) {
					Toast.makeText(AndroidpnMainActivity.this,
							"Server IP 不能为空", Toast.LENGTH_SHORT).show();
				} else {
					sharedPfsUtil.putValue(SERVERIP, ip);
					sharedPfsUtil.putValue(
							SERVERPROT,
							Integer.parseInt(serverportEditText.getText()
									.toString().trim()));
					sharedPfsUtil.putValue(USERNAME, usernameEditText.getText()
							.toString().trim());
					startService(new Intent(AndroidpnMainActivity.this,
							MyNotificationService.class));
				}
			}
		});
	}

}

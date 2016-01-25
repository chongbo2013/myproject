package com.yeyanxiang.project.xmpp;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class FormLogin extends Activity implements OnClickListener {

	public static Context context;
	private EditText useridText, pwdText, serverText;
	private LinearLayout layout1, layout2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.formlogin);
		context = this;
		// 获取用户和密码
		this.useridText = (EditText) findViewById(R.id.formlogin_userid);
		this.pwdText = (EditText) findViewById(R.id.formlogin_pwd);
		this.serverText = (EditText) findViewById(R.id.formlogin_serverip);

		// 正在登录
		this.layout1 = (LinearLayout) findViewById(R.id.formlogin_layout1);
		// 登录界面
		this.layout2 = (LinearLayout) findViewById(R.id.formlogin_layout2);

		Button btsave = (Button) findViewById(R.id.formlogin_btsubmit);
		btsave.setOnClickListener(this);
		Button btcancel = (Button) findViewById(R.id.formlogin_btcancel);
		btcancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// 根据ID来进行提交或者取消
		switch (v.getId()) {
		case R.id.formlogin_btsubmit:
			// 取得填入的用户和密码
			final String USERID = this.useridText.getText().toString();
			final String PWD = this.pwdText.getText().toString();
			final String serverip = this.serverText.getText().toString().trim();

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					// sendEmptyMessage:发送一条消息
					handler.sendEmptyMessage(1);

					try {
						// 连接
						XmppTool.setServerip(serverip);
						XmppTool.getConnection().login(USERID, PWD);
						Log.i("XMPPClient", "Logged in as "
								+ XmppTool.getConnection().getUser());

						// 状态
						Presence presence = new Presence(
								Presence.Type.available);
						XmppTool.getConnection().sendPacket(presence);

						Intent intent = new Intent(FormLogin.this,
								FormClient.class);
						intent.putExtra("USERID", USERID);
						FormLogin.this.startActivity(intent);
						FormLogin.this.finish();
					} catch (Exception e) {
						e.printStackTrace();
						XmppTool.closeConnection();
						handler.sendEmptyMessage(2);
					}
				}
			}).start();

			break;
		case R.id.formlogin_btcancel:
			finish();
			break;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			if (msg.what == 1) {
				layout1.setVisibility(View.VISIBLE);
				layout2.setVisibility(View.GONE);
			} else if (msg.what == 2) {
				layout1.setVisibility(View.GONE);
				layout2.setVisibility(View.VISIBLE);
				Toast.makeText(FormLogin.this, "登录失败！", Toast.LENGTH_SHORT)
						.show();
			}
		};
	};
}
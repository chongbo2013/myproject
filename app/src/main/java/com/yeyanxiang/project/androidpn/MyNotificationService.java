package com.yeyanxiang.project.androidpn;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.client.NotificationService;
import org.androidpn.client.XmppManager;
import com.yeyanxiang.util.SharedPfsUtil;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.TelephonyManager;
import android.util.Log;

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
public class MyNotificationService extends NotificationService {

	private static final String LOGTAG = LogUtil
			.makeLogTag(MyNotificationService.class);

	private final String showNotifyAction = "com.project.androidpn.copy.SHOW_NOTIFICATION";

	private BroadcastReceiver notificationReceiver;

	private String version = "0.5.0";

	public MyNotificationService() {
		super();
		notificationReceiver = new NotificationReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		// connectivityManager = (ConnectivityManager)
		// getSystemService(Context.CONNECTIVITY_SERVICE);

		sharedPrefs = new SharedPfsUtil(getApplicationContext(),
				AndroidpnMainActivity.USERDATA).getSharedPreferences();

		Editor editor = sharedPrefs.edit();
		editor.putString(Constants.DEVICE_ID, deviceId);
		editor.putString(Constants.XMPP_USERNAME, sharedPrefs.getString(
				AndroidpnMainActivity.USERNAME, "yeyanxiang"));
		editor.putString(Constants.XMPP_PASSWORD, sharedPrefs.getString(
				AndroidpnMainActivity.USERNAME, "yeyanxiang"));
		editor.putString(Constants.API_KEY, "12345");
		editor.putString(Constants.VERSION, version);
		// editor.putString(Constants.XMPP_HOST, "198.9.9.188");
		editor.putString(Constants.XMPP_HOST, sharedPrefs.getString(
				AndroidpnMainActivity.SERVERIP, "198.9.9.188"));
		editor.putInt(Constants.XMPP_PORT,
				sharedPrefs.getInt(AndroidpnMainActivity.SERVERPROT, 5222));
		editor.commit();

		setDeviceId();

		xmppManager = new XmppManager(this, AndroidpnMainActivity.handler);

		taskSubmitter.submit(new Runnable() {
			public void run() {
				start();
			}
		});
		// flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
		// return START_STICKY;
	}

	@Override
	public void onDestroy() {
		stop();
		Log.d(LOGTAG, "onDestroy()...");
	}

	@Override
	public SharedPreferences getSharedPreferences() {
		return sharedPrefs;
	}

	@Override
	public void registerNotificationReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(showNotifyAction);
		filter.addAction(Constants.ACTION_NOTIFICATION_CLICKED);
		filter.addAction(Constants.ACTION_NOTIFICATION_CLEARED);
		registerReceiver(notificationReceiver, filter);
	}

	@Override
	public void unregisterNotificationReceiver() {
		unregisterReceiver(notificationReceiver);
	}

	@Override
	public String getShowNotifyAction() {
		// TODO Auto-generated method stub
		return showNotifyAction;
	}

}

package com.yeyanxiang.project.applist;

import java.util.ArrayList;
import java.util.List;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.BadgeView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * Create on 2013-5-7 上午10:10:06 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 应用程序列表检索页
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class ApplistActivity extends Activity {
	private Context context;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			BadgeView badgeView = (BadgeView) msg.obj;
			badgeView.setText(msg.what + "");
			badgeView.setBackgroundResource(R.drawable.badge_ifaux);
			badgeView.setTextSize(16);
			badgeView.toggle(true);
		}

	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applist);
		context = this;
		final ArrayList<PackageInfo> systemapps = new ArrayList<PackageInfo>();
		final ArrayList<PackageInfo> otherapps = new ArrayList<PackageInfo>();
		final ArrayList<PackageInfo> sdcardapps = new ArrayList<PackageInfo>();

		final PackageManager pm = getPackageManager();

		final ProgressDialog dialog = ProgressDialog.show(this, "列表获取中",
				"请稍后...");
		Button systemButton = (Button) findViewById(R.id.systemapp);
		Button otherButton = (Button) findViewById(R.id.otherapp);
		Button sdcardButton = (Button) findViewById(R.id.sdcardapp);

		final BadgeView systembadge = new BadgeView(context, systemButton);
		final BadgeView otherbadge = new BadgeView(context, otherButton);
		final BadgeView sdcardbadge = new BadgeView(context, sdcardButton);
		final Intent intent = new Intent(context, AppList.class);

		systemButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.putParcelableArrayListExtra("value", systemapps);
				startActivity(intent);
			}
		});

		otherButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.putParcelableArrayListExtra("value", otherapps);
				startActivity(intent);
			}
		});

		sdcardButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent.putParcelableArrayListExtra("value", sdcardapps);
				startActivity(intent);
			}
		});

		((Button) findViewById(R.id.appmanager))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = pm
								.getLaunchIntentForPackage("com.android.settings");
						intent.setClassName("com.android.settings",
								"com.android.settings.ManageApplications");
						if (intent != null) {
							startActivity(intent);
						}
					}
				});

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 得到所有应用程序信息
				List<PackageInfo> packageInfos = pm
						.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

				for (PackageInfo p : packageInfos) {
					int flags = p.applicationInfo.flags;
					if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
						otherapps.add(p);
						if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
							sdcardapps.add(p);
						}
					} else {
						systemapps.add(p);
					}
				}
				dialog.dismiss();

				Message message = new Message();
				message.what = systemapps.size();
				message.obj = systembadge;
				handler.sendMessage(message);
				Message message1 = new Message();
				message1.what = otherapps.size();
				message1.obj = otherbadge;
				handler.sendMessage(message1);
				Message message2 = new Message();
				message2.what = sdcardapps.size();
				message2.obj = sdcardbadge;
				handler.sendMessage(message2);

				// systembadge.setText(systemapps.size() + "");
				// systembadge
				// .setBackgroundResource(R.drawable.badge_ifaux);
				// systembadge.setTextSize(16);
				// systembadge.toggle(true);
				//
				// otherbadge.setText(otherapps.size() + "");
				// otherbadge
				// .setBackgroundResource(R.drawable.badge_ifaux);
				// otherbadge.setTextSize(16);
				// otherbadge.toggle(true);
				//
				// sdcardbadge.setText(sdcardapps.size() + "");
				// sdcardbadge
				// .setBackgroundResource(R.drawable.badge_ifaux);
				// sdcardbadge.setTextSize(16);
				// sdcardbadge.toggle(true);

			}
		}).start();

	}
}
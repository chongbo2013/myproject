package com.yeyanxiang.project.applist;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @data 2014年1月13日 上午9:44:50
 * 
 * @简介
 */
public class AppListAdapter extends BaseAdapter {
	private List<PackageInfo> apps;
	private ImageCache imageCache;
	private LayoutInflater inflater;
	private int mLcdWidth = 0;
	private float mDensity = 0;
	private PackageManager pm;
	private Context context;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == ImageCache.NOTIFY) {
				notifyDataSetChanged();
			}
		};
	};

	public AppListAdapter(List<PackageInfo> apps, Context context,
			PackageManager pm) {
		inflater = LayoutInflater.from(context);
		this.apps = apps;
		if (apps == null) {
			apps = new ArrayList<PackageInfo>();
		}
		imageCache = new ImageCache(handler, R.drawable.ic_launcher, pm);

		this.context = context;
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		mLcdWidth = dm.widthPixels;
		mDensity = dm.density;
		this.pm = pm;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return apps.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return apps.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.appitem, null);
			viewHolder = new ViewHolder();
			viewHolder.appicon = (ImageView) convertView
					.findViewById(R.id.appicon);
			viewHolder.appname = (TextView) convertView
					.findViewById(R.id.appname);
			viewHolder.apppackage = (TextView) convertView
					.findViewById(R.id.apppackage);
			viewHolder.btnopen = (Button) convertView
					.findViewById(R.id.btnOpen);
			viewHolder.btnview = (Button) convertView
					.findViewById(R.id.btnView);
			viewHolder.footer = (LinearLayout) convertView
					.findViewById(R.id.footer);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final PackageInfo packageInfo = apps.get(position);
		int widthSpec = MeasureSpec.makeMeasureSpec(
				(int) (mLcdWidth - 10 * mDensity), MeasureSpec.EXACTLY);
		viewHolder.footer.measure(widthSpec, 0);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHolder.footer
				.getLayoutParams();
		params.bottomMargin = -viewHolder.footer.getMeasuredHeight();
		viewHolder.footer.setVisibility(View.GONE);

		viewHolder.btnopen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = pm
						.getLaunchIntentForPackage(packageInfo.packageName);
				checkintent(intent, packageInfo);
			}
		});

		viewHolder.btnview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						"android.settings.APPLICATION_DETAILS_SETTINGS", Uri
								.fromParts("package", packageInfo.packageName,
										null));
				checkintent(intent, packageInfo);
			}
		});

		viewHolder.apppackage.setText(packageInfo.packageName);
		viewHolder.setPackageInfo(packageInfo);
		imageCache.loadBitmap(viewHolder);
		return convertView;
	}

	private void checkintent(Intent intent, PackageInfo pkginfo) {
		if (intent != null) {
			try {
				context.startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				PubUtil.showText(context, "null");
			}
		} else {
			Intent intent2 = new Intent(Intent.ACTION_MAIN, null);
			intent2.setPackage(pkginfo.packageName);

			List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent2,
					0);
			if (resolveInfos.size() > 0) {
				ResolveInfo resolveInfo = resolveInfos.get(0);
				startactivity(resolveInfo);
				if (resolveInfos.size() > 1) {
					for (ResolveInfo resolveInfo2 : resolveInfos) {
						System.out
								.println(resolveInfo2.activityInfo.packageName
										+ "---"
										+ resolveInfo2.activityInfo.name);
					}
				}
			} else {
				PubUtil.showText(context, "null");
			}
		}
	}

	private void startactivity(ResolveInfo resolveInfo) {
		if (TextUtils.isEmpty(resolveInfo.activityInfo.name)
				|| TextUtils.isEmpty(resolveInfo.activityInfo.packageName)) {
			PubUtil.showText(context, "null");
		} else {
			Intent intent = new Intent();
			intent.setComponent(new ComponentName(
					resolveInfo.activityInfo.packageName,
					resolveInfo.activityInfo.name));
			try {
				context.startActivity(intent);
			} catch (Exception e) {
				// TODO: handle exception
				PubUtil.showText(context, "null");
			}
		}
	}
}
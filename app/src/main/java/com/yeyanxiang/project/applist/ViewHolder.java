package com.yeyanxiang.project.applist;

import android.content.pm.PackageInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
public class ViewHolder {
	public ImageView appicon;
	public TextView appname;
	public TextView apppackage;
	public PackageInfo packageInfo;
	public Button btnopen;
	public Button btnview;
	public LinearLayout footer;

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

}

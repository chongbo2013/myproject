package com.yeyanxiang.project.slidingmenudemo;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月14日
 * 
 * @简介
 */
public class Util {

	public static void goToGitHub(Context context) {
		Uri uriUrl = Uri.parse("http://github.com/jfeinstein10/slidingmenu");
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		context.startActivity(launchBrowser);
	}

}

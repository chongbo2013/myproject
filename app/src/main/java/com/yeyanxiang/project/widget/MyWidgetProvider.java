package com.yeyanxiang.project.widget;

import java.util.Timer;
import java.util.TimerTask;

import com.yeyanxiang.project.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * 
 * Create on 2013-5-7 上午10:31:34 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 自定义小插件
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class MyWidgetProvider extends AppWidgetProvider {
	private static Timer myTimer;
	private static int index = 0;

	// 定义我们要发送的事件
	private final String broadCastString = "com.project.MyWidgetProvider";

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);

		System.out.println("onDeleted");
	}

	@Override
	public void onEnabled(Context context) {
		System.out.println("onEnabled");
		// TODO Auto-generated method stub
		super.onEnabled(context);

		// 在插件被创建的时候这里会被调用， 所以我们在这里开启一个timer 每秒执行一次
		MyTask mMyTask = new MyTask(context);
		myTimer = new Timer();
		myTimer.schedule(mMyTask, 1000, 1000);
		System.out.println("onEnabled2");
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		System.out.println("onUpdate");
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// 当判断到是该事件发过来时， 我们就获取插件的界面， 然后将index自加后传入到textview中
		System.out.println("onReceive");
		if (intent.getAction().equals(broadCastString)) {
			index++;

			RemoteViews rv = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);
			rv.setTextViewText(R.id.update, Integer.toString(index));

			// 将该界面显示到插件中
			AppWidgetManager appWidgetManager = AppWidgetManager
					.getInstance(context);
			ComponentName componentName = new ComponentName(context,
					MyWidgetProvider.class);
			appWidgetManager.updateAppWidget(componentName, rv);
		}
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
	}

	class MyTask extends TimerTask {

		private Context mcontext = null;
		private Intent intent = null;

		public MyTask(Context context) {

			// 新建一个要发送的Intent
			mcontext = context;
			intent = new Intent();
			intent.setAction(broadCastString);
		}

		@Override
		public void run() {
			System.out.println("2");
			// 发送广播(由onReceive来接收)
			mcontext.sendBroadcast(intent);
		}

	}
}

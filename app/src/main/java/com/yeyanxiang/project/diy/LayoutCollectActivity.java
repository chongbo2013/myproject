package com.yeyanxiang.project.diy;

import java.text.DecimalFormat;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.CircleProgressbar;
import com.yeyanxiang.view.MultipleSpinner;
import com.yeyanxiang.view.marquee.AutoScrollTextView;
import com.yeyanxiang.view.timedatedialog.DateTimeDialog;
import com.yeyanxiang.view.timedatedialog.DateTimePickWheelDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

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
public class LayoutCollectActivity extends Activity {
	private Context context;

	private EditText editText;
	private TextView icon1;
	private TextView icon2;
	private TextView icon3;
	private ToggleButton toggleButton;
	private AutoScrollTextView autoScrollTextView;
	private CircleProgressbar circleProgressbar;
	private Button circleButton;
	private int progress = 0;
	private boolean circlerun = false;
	private CircleRunThread circleRunThread;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				circleButton.setText("run");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = LayoutCollectActivity.this;
		setContentView(R.layout.layoutcollect);
		init();

	}

	private void init() {
		editText = (EditText) findViewById(R.id.layoutcollectexittext);
		icon1 = (TextView) findViewById(R.id.layoutcollectfunction1);
		icon2 = (TextView) findViewById(R.id.layoutcollectfunction2);
		icon3 = (TextView) findViewById(R.id.layoutcollectfunction3);

		icon1.setOnClickListener(listener);
		icon2.setOnClickListener(listener);
		icon3.setOnClickListener(listener);

		toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
		toggleButton.setOnCheckedChangeListener(changeListener);

		MultipleSpinner multipleSpinner = (MultipleSpinner) findViewById(R.id.MultipleSpinner);
		multipleSpinner.setItems(new String[] { "1", "2", "3" });

		String[] words = new String[] { "平台功能特点", "国际先进专业生命体征采集设备",
				"全国庞大专业的医疗团队为您服务", "人手一套、使用便捷", "随时随地了解自身情况", "整合你身边智能手机、计算机、",
				"电视等设备", "全面身体护理、健康保健、", "疾病预防等功能" };

		autoScrollTextView = (AutoScrollTextView) findViewById(R.id.autoscrolltext);
		autoScrollTextView.setWords(words);
		autoScrollTextView.init(getWindowManager());
		autoScrollTextView.startScroll();

		circleProgressbar = (CircleProgressbar) findViewById(R.id.circleprogress);
		circleProgressbar.setRadius(50);
		circleProgressbar.setStrokeWidth(5);
		circleButton = (Button) findViewById(R.id.circlebutton);
		circleButton.setOnClickListener(listener);

		// circlerun = true;
		// circleRunThread = new CircleRunThread();
		// circleRunThread.start();
		circleButton.setText("run");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		autoScrollTextView.stopScroll();
		super.onDestroy();
	}

	private OnClickListener listener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.layoutcollectfunction1:
				new DateTimeDialog(context, dateTimeSetListener, false).show();
				break;
			case R.id.layoutcollectfunction2:
				final DateTimePickWheelDialog datePickWheelDialog = new DateTimePickWheelDialog(
						context);
				datePickWheelDialog.setTitle("时间选择");
				datePickWheelDialog.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								editText.setText(datePickWheelDialog
										.getFormatTime() + ":00");
							}
						});
				datePickWheelDialog.setNegativeButton("取消", null);
				datePickWheelDialog.show();
				break;
			case R.id.layoutcollectfunction3:

				break;
			case R.id.circlebutton:
				if (circlerun) {
					circleButton.setText("run");
				} else {
					circleButton.setText("stop");
					if (circleRunThread == null) {
						circleRunThread = new CircleRunThread();
						circleRunThread.start();
					} else {
						if (circleRunThread.isAlive()) {
							synchronized (circleRunThread) {
								circleRunThread.notify();
							}
						} else {
							circleRunThread = new CircleRunThread();
							circleRunThread.start();
						}
					}
				}
				circlerun = !circlerun;
				break;

			default:
				break;
			}
		}
	};

	private DateTimeDialog.OnDateTimeSetListener dateTimeSetListener = new DateTimeDialog.OnDateTimeSetListener() {

		@Override
		public void onDateSet(DatePicker datePicker, TimePicker timePicker,
				int year, int monthOfYear, int dayOfMonth, int hourOfDay,
				int minute) {
			// TODO Auto-generated method stub

			DecimalFormat decimal = new DecimalFormat("00");

			editText.setText(year + "-" + decimal.format(monthOfYear + 1) + "-"
					+ decimal.format(dayOfMonth) + " "
					+ decimal.format(hourOfDay) + ":" + decimal.format(minute)
					+ ":00");
		}
	};

	private OnCheckedChangeListener changeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO Auto-generated method stub
			if (isChecked) {
				editText.setText("开");
			} else {
				editText.setText("关");
			}
		}
	};

	private class CircleRunThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			while (progress < circleProgressbar.getTotalProgress()) {
				progress++;
				circleProgressbar.setProgress(progress);
				try {
					Thread.sleep(50);
					if (!circlerun) {
						synchronized (this) {
							wait();
						}
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			progress = 0;
			circlerun = false;
			handler.sendEmptyMessage(0);
		}
	}

}

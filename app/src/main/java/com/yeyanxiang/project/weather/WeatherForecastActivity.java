package com.yeyanxiang.project.weather;

import java.util.List;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;
import com.yeyanxiang.util.SharedPfsUtil;
import com.yeyanxiang.util.weather.Weather;
import com.yeyanxiang.util.weather.WebServiceUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * 
 * Create on 2013-5-7 上午10:30:37 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 全国各省市天气预报
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class WeatherForecastActivity extends Activity {
	private Context context;
	private Spinner provinceSpinner;
	private Spinner citySpinner;
	private ImageView todayWhIcon1;
	private ImageView todayWhIcon2;
	private TextView textWeatherToday;
	private ImageView tomorrowWhIcon1;
	private ImageView tomorrowWhIcon2;
	private TextView textWeatherTomorrow;
	private ImageView afterdayWhIcon1;
	private ImageView afterdayWhIcon2;
	private TextView textWeatherAfterday;
	private TextView textWeatherCurrent;
	private SharedPfsUtil sharedPfsUtil;

	private Weather weather;
	private Weather.DayWeather today;
	private Weather.DayWeather tomorrow;
	private Weather.DayWeather aftertomorrow;

	// 更新当天的天气实况
	private String weatherlive;

	private List<String> provinces;
	private List<String> cities;

	private ProgressDialog dialog;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				PubUtil.showText(context, "网络异常,请稍后再试");
				// finish();
			} else if (msg.what == 1) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						WeatherForecastActivity.this,
						android.R.layout.simple_spinner_item, provinces);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				// 使用Spinner显示省份列表
				provinceSpinner.setAdapter(adapter);
				provinceSpinner.setEnabled(true);
				provinceSpinner.setSelection(sharedPfsUtil.getValue(
						"provinceposition", 0));
			} else if (msg.what == 2) {
				ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(
						WeatherForecastActivity.this,
						android.R.layout.simple_spinner_item, cities);
				cityAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// 使用Spinner显示城市列表
				citySpinner.setAdapter(cityAdapter);
				citySpinner.setEnabled(true);

				if (msg.arg1 == sharedPfsUtil.getValue("provinceposition", 0)) {
					citySpinner.setSelection(sharedPfsUtil.getValue(
							"cityposition", 0));
				} else {
					sharedPfsUtil.putValue("provinceposition", msg.arg1);
				}
			} else if (msg.what == 3) {
				if (dialog != null) {
					if (dialog.isShowing()) {
						dialog.dismiss();
					}
				}
			} else if (msg.what == 4) {
				textWeatherCurrent
						.setText("\n"
								+ weatherlive.substring(0,
										weatherlive.indexOf("：") + 1)
								+ "\n"
								+ weatherlive.substring(
										weatherlive.indexOf("：") + 1).replace(
										"；", "\n") + "\n"
								+ weather.getAirquality().replace("；", "\n")
								+ "\n" + weather.getTravelhelp());
				// 更新显示今天天气的图标和文本框
				textWeatherToday.setText("今天："
						+ today.getWeather().split(" ")[0] + "\n天气："
						+ today.getWeather().split(" ")[1] + "\n气温："
						+ today.getTemperature() + "\n风力：" + today.getWind());
				todayWhIcon1.setImageResource(today.getDrawableid1());
				todayWhIcon2.setImageResource(today.getDrawableid2());
				// 更新显示明天天气的图标和文本框
				textWeatherTomorrow.setText("明天："
						+ tomorrow.getWeather().split(" ")[0] + "\n天气："
						+ tomorrow.getWeather().split(" ")[1] + "\n气温："
						+ tomorrow.getTemperature() + "\n风力："
						+ tomorrow.getWind());
				tomorrowWhIcon1.setImageResource(tomorrow.getDrawableid1());
				tomorrowWhIcon2.setImageResource(tomorrow.getDrawableid2());
				// 更新显示后天天气的图标和文本框
				textWeatherAfterday.setText("后天："
						+ aftertomorrow.getWeather().split(" ")[0] + "\n天气："
						+ aftertomorrow.getWeather().split(" ")[1] + "\n气温："
						+ aftertomorrow.getTemperature() + "\n风力："
						+ aftertomorrow.getWind());
				afterdayWhIcon1
						.setImageResource(aftertomorrow.getDrawableid1());
				afterdayWhIcon2
						.setImageResource(aftertomorrow.getDrawableid2());
			} else if (msg.what == 5) {
				dialog.show();
			} else if (msg.what == 6) {
				textWeatherToday.setText("系统维护，请稍后再试");
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather);

		context = this;
		sharedPfsUtil = new SharedPfsUtil(context, PubUtil.USER_DATA);

		todayWhIcon1 = (ImageView) findViewById(R.id.todayWhIcon1);
		todayWhIcon2 = (ImageView) findViewById(R.id.todayWhIcon2);
		textWeatherToday = (TextView) findViewById(R.id.weatherToday);
		tomorrowWhIcon1 = (ImageView) findViewById(R.id.tomorrowWhIcon1);
		tomorrowWhIcon2 = (ImageView) findViewById(R.id.tomorrowWhIcon2);
		textWeatherTomorrow = (TextView) findViewById(R.id.weatherTomorrow);
		afterdayWhIcon1 = (ImageView) findViewById(R.id.afterdayWhIcon1);
		afterdayWhIcon2 = (ImageView) findViewById(R.id.afterdayWhIcon2);
		textWeatherAfterday = (TextView) findViewById(R.id.weatherAfterday);
		textWeatherCurrent = (TextView) findViewById(R.id.weatherCurrent);

		// 获取程序界面中选择省份、城市的Spinner组件
		provinceSpinner = (Spinner) findViewById(R.id.province);
		citySpinner = (Spinner) findViewById(R.id.city);
		provinceSpinner.setEnabled(false);
		citySpinner.setEnabled(false);

		dialog = ProgressDialog.show(this, null, "加载中，请稍后");

		dialog.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dialog.dismiss();
				}
				return false;
			}
		});

		// 当省份Spinner的选择项被改变时
		provinceSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
					final int position, long id) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						cities = WebServiceUtil
								.getCityListByProvince(provinceSpinner
										.getSelectedItem().toString());
						if (cities != null) {
							Message message = new Message();
							message.what = 2;
							message.arg1 = position;
							handler.sendMessage(message);
						}
					}
				}).start();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		// 当城市Spinner的选择项被改变时
		citySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> source, View parent,
					int position, long id) {
				sharedPfsUtil.putValue("cityposition", position);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						showWeather(citySpinner.getSelectedItem().toString());
					}
				}).start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 调用远程Web Service获取省份列表

				provinces = WebServiceUtil.getProvinceList();
				handler.sendEmptyMessage(3);
				if (provinces == null) {
					handler.sendEmptyMessage(0);
				} else {
					handler.sendEmptyMessage(1);
				}

			}
		}).start();
	}

	private void showWeather(String city) {
		// 获取远程Web Service返回的对象
		try {
			handler.sendEmptyMessage(5);
			weather = WebServiceUtil.getWeatherByCity1(city);
			today = weather.getWeather_of_today();
			tomorrow = weather.getWeather_of_tomorrow();
			aftertomorrow = weather.getWeather_of_aftertomorrow();

			// 更新当天的天气实况
			weatherlive = weather.getWeatherlive();
			handler.sendEmptyMessage(4);
		} catch (Exception e) {
			// TODO: handle exception
			handler.sendEmptyMessage(6);
		}

		handler.sendEmptyMessage(3);
	}

}
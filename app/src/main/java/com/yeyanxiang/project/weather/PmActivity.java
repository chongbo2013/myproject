package com.yeyanxiang.project.weather;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;
import com.yeyanxiang.util.HttpUtil;
import com.yeyanxiang.util.SharedPfsUtil;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @date 2013-8-28 上午11:34:35
 * 
 * @简介
 */
public class PmActivity extends Activity {
	private TextView contentTextView;
	private final String appkey = "DBrsCRYdNvExxyHi9trR";
	private final String pmurl = "http://www.pm25.in/api/querys/pm2_5.json";
	private final String cityurl = "http://www.pm25.in/api/querys.json";
	private final String CITYSELECTION = "pmcityselection";

	private SharedPfsUtil sharedPfsUtil;

	private Spinner pmcity;
	private ArrayList<String> citys;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				contentTextView.setText((String) msg.obj);
			} else if (msg.what == 1) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						PmActivity.this, android.R.layout.simple_spinner_item,
						citys);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

				pmcity.setAdapter(adapter);
				pmcity.setEnabled(true);

				pmcity.setSelection(sharedPfsUtil.getValue(CITYSELECTION, 0));
			} else if (msg.what == 2) {
				contentTextView.setText("加载中...");
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pmlayout);
		sharedPfsUtil = new SharedPfsUtil(this, PubUtil.USER_DATA);
		contentTextView = (TextView) findViewById(R.id.pmcontent);
		pmcity = (Spinner) findViewById(R.id.pmcity);
		citys = new ArrayList<String>();
		pmcity.setEnabled(false);
		contentTextView.setText("加载中...");

		pmcity.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				sharedPfsUtil.putValue(CITYSELECTION, position);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						getpm(citys.get(position));
					}
				}).start();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});

		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String value = HttpUtil.sendGetRequest(cityurl + "?token="
						+ appkey, 10000, false);

				try {
					JSONArray jsonarray = new JSONObject(value)
							.getJSONArray("cities");
					StringBuffer stringBuffer = new StringBuffer();
					for (int i = 0; i < jsonarray.length(); i++) {
						citys.add(jsonarray.getString(i));
					}

					handler.sendEmptyMessage(1);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					String error = e.getLocalizedMessage();
					Message message = new Message();
					message.what = 0;
					message.obj = error;
					handler.sendMessage(message);
				}
			}
		}).start();

	}

	private void getpm(String cityname) {
		handler.sendEmptyMessage(2);
		String value = HttpUtil.sendGetRequest(pmurl + "?city=" + cityname
				+ "&token=" + appkey, 10000, false);

		try {
			// JSONObject jsonObject = new JSONObject(value);

			JSONArray jsonarray = new JSONObject("{\"pmvalue\":" + value + "}")
					.getJSONArray("pmvalue");
			StringBuffer stringBuffer = new StringBuffer();
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonObject = (JSONObject) jsonarray.opt(i);

				stringBuffer
						.append("城市：" + jsonObject.getString("area") + "\n");
				if (!jsonObject.isNull("position_name")) {
					stringBuffer.append("监测点名称："
							+ jsonObject.getString("position_name") + "\n");
				}
				if (!jsonObject.isNull("station_code")) {
					stringBuffer.append("监测点编码："
							+ jsonObject.getString("station_code") + "\n");
				}
				stringBuffer.append("空气质量指数：" + jsonObject.getString("aqi")
						+ "\n");
				if (!jsonObject.isNull("primary_pollutant")) {
					stringBuffer.append("首要污染物："
							+ jsonObject.getString("primary_pollutant") + "\n");
				}
				stringBuffer.append("空气质量指数类别："
						+ jsonObject.getString("quality") + "\n");
				stringBuffer.append("颗粒物（粒径小于等于2.5μm）1小时平均："
						+ jsonObject.getString("pm2_5") + "\n");
				stringBuffer.append("颗粒物（粒径小于等于2.5μm）24小时滑动平均："
						+ jsonObject.getString("pm2_5_24h") + "\n");
				stringBuffer.append("数据发布的时间："
						+ jsonObject.getString("time_point").replace("T", " ")
								.replace("Z", "") + "\n\n");
			}

			Message message = new Message();
			message.what = 0;
			message.obj = stringBuffer.toString();
			handler.sendMessage(message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String error = e.getLocalizedMessage();
			Message message = new Message();
			message.what = 0;
			message.obj = error;
			handler.sendMessage(message);
		}
	}
}

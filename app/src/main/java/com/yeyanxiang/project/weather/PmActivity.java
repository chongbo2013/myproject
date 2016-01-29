package com.yeyanxiang.project.weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.yeyanxiang.project.R;
import com.yeyanxiang.project.activity.BaseActivity;
import com.yeyanxiang.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author 叶雁翔
 * @version 1.0
 * @Email yanxiang1120@gmail.com
 * @date 2013-8-28 上午11:34:35
 * @简介
 */
public class PmActivity extends BaseActivity {
    private TextView contentTextView;
    private final String appKey = "DBrsCRYdNvExxyHi9trR";
    private final String pmUrl = "http://www.pm25.in/api/querys/pm2_5.json";
    private final String cityUrl = "http://www.pm25.in/api/querys.json";
    private final String CITY_SELECTION = "CITY_SELECTION";

    private Spinner pmCity;
    private ArrayList<String> cityLists;

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                contentTextView.setText((String) msg.obj);
            } else if (msg.what == 1) {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(PmActivity.this, android.R.layout.simple_spinner_item, cityLists);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                pmCity.setAdapter(adapter);
                pmCity.setEnabled(true);

                pmCity.setSelection(mSharedPfsUtil.getValue(CITY_SELECTION, 0));
            } else if (msg.what == 2) {
                contentTextView.setText("加载中...");
            }
        }

        ;
    };

    @Override
    public String getTAG() {
        return "PmActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pmlayout);
        contentTextView = (TextView) findViewById(R.id.pmcontent);
        pmCity = (Spinner) findViewById(R.id.pmcity);
        cityLists = new ArrayList<String>();
        pmCity.setEnabled(false);
        contentTextView.setText("加载中...");

        pmCity.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                // TODO Auto-generated method stub
                mSharedPfsUtil.putValue(CITY_SELECTION, position);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        getpm(cityLists.get(position));
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
                String value = HttpUtil.sendGetRequest(cityUrl + "?token=" + appKey, 10000, false);

                try {
                    JSONArray jsonarray = new JSONObject(value).getJSONArray("cities");
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        cityLists.add(jsonarray.getString(i));
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
        String value = HttpUtil.sendGetRequest(pmUrl + "?city=" + cityname + "&token=" + appKey, 10000, false);

        try {
            // JSONObject jsonObject = new JSONObject(value);

            JSONArray jsonarray = new JSONObject("{\"pmvalue\":" + value + "}").getJSONArray("pmvalue");
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonarray.opt(i);

                stringBuffer.append("城市：" + jsonObject.getString("area") + "\n");
                if (!jsonObject.isNull("position_name")) {
                    stringBuffer.append("监测点名称：" + jsonObject.getString("position_name") + "\n");
                }
                if (!jsonObject.isNull("station_code")) {
                    stringBuffer.append("监测点编码：" + jsonObject.getString("station_code") + "\n");
                }
                stringBuffer.append("空气质量指数：" + jsonObject.getString("aqi") + "\n");
                if (!jsonObject.isNull("primary_pollutant")) {
                    stringBuffer.append("首要污染物：" + jsonObject.getString("primary_pollutant") + "\n");
                }
                stringBuffer.append("空气质量指数类别：" + jsonObject.getString("quality") + "\n");
                stringBuffer.append("颗粒物（粒径小于等于2.5μm）1小时平均：" + jsonObject.getString("pm2_5") + "\n");
                stringBuffer.append("颗粒物（粒径小于等于2.5μm）24小时滑动平均：" + jsonObject.getString("pm2_5_24h") + "\n");
                stringBuffer.append("数据发布的时间：" + jsonObject.getString("time_point").replace("T", " ").replace("Z", "") + "\n\n");
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

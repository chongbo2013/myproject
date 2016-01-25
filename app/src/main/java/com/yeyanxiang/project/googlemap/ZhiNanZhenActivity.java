package com.yeyanxiang.project.googlemap;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * 
 * Create on 2013-5-7 上午10:12:28 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 指南针
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class ZhiNanZhenActivity extends Activity implements SensorEventListener {

	ImageView image; // 指南针图片
	float currentDegree = 0f; // 指南针图片转过的角度

	SensorManager mSensorManager; // 管理器

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhinanzhen);

		image = (ImageView) findViewById(R.id.znzImage);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); // 获取管理服务
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 注册监听器
		mSensorManager.registerListener(this,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
				SensorManager.SENSOR_DELAY_GAME);
	}

	// 取消注册
	@Override
	protected void onPause() {
		mSensorManager.unregisterListener(this);
		super.onPause();

	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(this);
		super.onStop();

	}

	// 传感器值改变
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	// 精度改变
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		// 获取触发event的传感器类型
		int sensorType = event.sensor.getType();

		switch (sensorType) {
		case Sensor.TYPE_ORIENTATION:
			float degree = event.values[0]; // 获取z转过的角度
			// 穿件旋转动画
			RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(100);// 动画持续时间
			image.startAnimation(ra);
			currentDegree = -degree;
			break;

		}
	}
}
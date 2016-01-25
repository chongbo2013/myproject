package com.yeyanxiang.project.sign;

import com.yeyanxiang.project.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CompoundButton;
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
public class ElectrocardioOffLineActivity extends Activity {

	private static ToggleButton alarmbutton;
	private GECGWaveFormOffLine gecgWaveFormOffLine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.electrocardiooffline);

		gecgWaveFormOffLine = (GECGWaveFormOffLine) findViewById(R.id.gECGWaveFormoffline);
		alarmbutton = (ToggleButton) findViewById(R.id.alarmbutton);

		alarmbutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					gecgWaveFormOffLine.alarm();
				} else {
					gecgWaveFormOffLine.dismissalarm();
				}
			}
		});
	}

	public static void dismissalarm() {
		alarmbutton.setChecked(false);
	}
}

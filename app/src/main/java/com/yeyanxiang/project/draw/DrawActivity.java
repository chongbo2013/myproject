package com.yeyanxiang.project.draw;

import android.app.Activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;
import com.yeyanxiang.view.drawview.DrawView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DrawActivity extends Activity {

	private Context context;
	@ViewInject(R.id.shapegroup)
	private RadioGroup shapegroup;
	@ViewInject(R.id.linebtn)
	private RadioButton linebtn;
	@ViewInject(R.id.rectanglebtn)
	private RadioButton rectanglebtn;
	@ViewInject(R.id.circlebtn)
	private RadioButton circlebtn;
	@ViewInject(R.id.scrawlbtn)
	private RadioButton scrawlbtn;
	@ViewInject(R.id.eraserbtn)
	private RadioButton eraserbtn;
	@ViewInject(R.id.colorgroup)
	private RadioGroup colorgroup;
	@ViewInject(R.id.colorblackbtn)
	private RadioButton colorblackbtn;
	@ViewInject(R.id.colorbluebtn)
	private RadioButton colorbluebtn;
	@ViewInject(R.id.colorredbtn)
	private RadioButton colorredbtn;
	@ViewInject(R.id.colorgreenbtn)
	private RadioButton colorgreenbtn;
	@ViewInject(R.id.paintsizeseekbar)
	private SeekBar paintsizeseekbar;
	@ViewInject(R.id.myview)
	private DrawView myView;
	private SimpleDateFormat format;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blockboardlayout);
		context = this;
		ViewUtils.inject(this);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		shapegroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.linebtn:
					System.out.println("-------直线--------");
					myView.setDrawTool(0);
					break;
				case R.id.rectanglebtn:
					System.out.println("------矩形---------");
					myView.setDrawTool(1);
					break;
				case R.id.circlebtn:
					System.out.println("--------圆形-------");
					myView.setDrawTool(2);
					break;
				case R.id.scrawlbtn:
					System.out.println("---------涂鸦------");
					myView.setDrawTool(3);
					break;
				case R.id.eraserbtn:
					System.out.println("--------橡皮------");
					myView.setDrawTool(10);
					break;

				default:
					break;
				}
			}
		});
		colorgroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.colorblackbtn:
					System.out.println("-------黑色--------");
					myView.setColorTool(Color.BLACK);
					break;
				case R.id.colorbluebtn:
					System.out.println("---------蓝色------");
					myView.setColorTool(Color.BLUE);
					break;
				case R.id.colorredbtn:
					System.out.println("--------红色-------");
					myView.setColorTool(Color.RED);
					break;
				case R.id.colorgreenbtn:
					System.out.println("---------绿色------");
					myView.setColorTool(Color.GREEN);
					break;

				default:
					break;
				}
			}
		});

		paintsizeseekbar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						System.out
								.println("-------onStopTrackingTouch--------");
						myView.setSizeTool(seekBar.getProgress());
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						System.out
								.println("-------onStartTrackingTouch--------");
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						System.out.println("-----onProgressChanged----------"
								+ progress + "---" + fromUser);
					}
				});

		shapegroup.check(R.id.scrawlbtn);
		colorgroup.check(R.id.colorblackbtn);
	}

	@OnClick(R.id.clearbtn)
	private void clear(View view) {
		myView.clear();
	}

	@OnClick(R.id.savebtn)
	private void save(View view) {
		if (myView.savePicture("/mnt/sdcard/myproject/",
				format.format(new Date()), 0)) {
			PubUtil.showText(context, "保存成功");
		} else {
			PubUtil.showText(context, "保存失败");
		}
	}

}

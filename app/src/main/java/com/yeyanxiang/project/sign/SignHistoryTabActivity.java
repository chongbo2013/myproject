package com.yeyanxiang.project.sign;

import com.yeyanxiang.project.R;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

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
public class SignHistoryTabActivity extends TabActivity {
	private Context context;
	private TabHost tabHost;
	private RadioGroup group;
	private static RadioGroup historygroup;
	private static int currentstate = 0;

	public static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			// System.out.println("tab---" + msg.what);
			currentstate = msg.what;
			if (msg.what == 0) {
				historygroup.check(R.id.weekhistory);
			} else if (msg.what == 1) {
				historygroup.check(R.id.monthhistory);
			} else if (msg.what == 2) {
				historygroup.check(R.id.threemonthhistory);
			} else if (msg.what == 3) {
				historygroup.check(R.id.sixmonthhistory);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.signtab);
		context = this;

		tabHost = getTabHost();

		tabHost.addTab(tabHost
				.newTabSpec("xindian")
				.setIndicator("心电")
				.setContent(
						new Intent(context, ElectrocardioHistoryActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("xueya").setIndicator("血压")
				.setContent(new Intent(context, XueYaHistoryActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("xueyang").setIndicator("血氧")
				.setContent(new Intent(this, XueYangHistoryActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("xinlv").setIndicator("心率")
				.setContent(new Intent(context, XinLvHistoryActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tiwen").setIndicator("体温")
				.setContent(new Intent(context, TempHistoryActivity.class)));

		init();
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		// if (group.hasFocus()) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
				switch (tabHost.getCurrentTab()) {
				case 0:
					group.check(R.id.tiwentab);
					break;
				case 1:
					group.check(R.id.xindiantab);
					break;
				case 2:
					group.check(R.id.xueyatab);
					break;
				case 3:
					group.check(R.id.xueyangtab);
					break;
				case 4:
					group.check(R.id.xinlvtab);
					break;

				default:
					break;
				}
			} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
				switch (tabHost.getCurrentTab()) {
				case 0:
					group.check(R.id.xueyatab);
					break;
				case 1:
					group.check(R.id.xueyangtab);
					break;
				case 2:
					group.check(R.id.xinlvtab);
					break;
				case 3:
					group.check(R.id.tiwentab);
					break;
				case 4:
					group.check(R.id.xindiantab);
					break;

				default:
					break;
				}
			} else {
				if (historygroup.getVisibility() == View.VISIBLE) {
					if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
						switch (currentstate) {
						case 0:
							currentstate = 3;
							historygroup.check(R.id.sixmonthhistory);
							break;
						case 1:
							currentstate--;
							historygroup.check(R.id.weekhistory);
							break;
						case 2:
							currentstate--;
							historygroup.check(R.id.monthhistory);
							break;
						case 3:
							currentstate--;
							historygroup.check(R.id.threemonthhistory);
							break;

						default:
							break;
						}
					} else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
						switch (currentstate) {
						case 0:
							currentstate++;
							historygroup.check(R.id.monthhistory);
							break;
						case 1:
							currentstate++;
							historygroup.check(R.id.threemonthhistory);
							break;
						case 2:
							currentstate++;
							historygroup.check(R.id.sixmonthhistory);
							break;
						case 3:
							currentstate = 0;
							historygroup.check(R.id.weekhistory);
							break;

						default:
							break;
						}
					}

					if (tabHost.getCurrentTab() == 1) {
						if (XueYaHistoryActivity.handler != null) {
							XueYaHistoryActivity.handler
									.sendEmptyMessage(currentstate);
						}
					} else if (tabHost.getCurrentTab() == 3) {
						if (XinLvHistoryActivity.handler != null) {
							XinLvHistoryActivity.handler
									.sendEmptyMessage(currentstate);
						}
					}
				}
			}
		}
		// }
		return super.dispatchKeyEvent(event);
	}

	private void init() {
		// TODO Auto-generated method stub
		group = (RadioGroup) findViewById(R.id.hometab);
		historygroup = (RadioGroup) findViewById(R.id.historyselect);
		group.check(R.id.xindiantab);

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.xindiantab:
					tabHost.setCurrentTab(0);
					historygroup.setVisibility(View.GONE);
					break;
				case R.id.xueyatab:
					tabHost.setCurrentTab(1);
					historygroup.setVisibility(View.VISIBLE);
					// historygroup.check(R.id.weekhistory);
					break;
				case R.id.xueyangtab:
					tabHost.setCurrentTab(2);
					historygroup.setVisibility(View.GONE);
					break;
				case R.id.xinlvtab:
					tabHost.setCurrentTab(3);
					historygroup.setVisibility(View.VISIBLE);
					// historygroup.check(R.id.weekhistory);
					break;
				case R.id.tiwentab:
					tabHost.setCurrentTab(4);
					historygroup.setVisibility(View.GONE);
					break;

				default:
					break;
				}
			}
		});

		((RadioButton) findViewById(R.id.weekhistory))
				.setOnClickListener(listener);
		((RadioButton) findViewById(R.id.monthhistory))
				.setOnClickListener(listener);
		((RadioButton) findViewById(R.id.threemonthhistory))
				.setOnClickListener(listener);
		((RadioButton) findViewById(R.id.sixmonthhistory))
				.setOnClickListener(listener);
		// historygroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
		// {
		//
		// @Override
		// public void onCheckedChanged(RadioGroup group, int checkedId) {
		// // TODO Auto-generated method stub
		// if (historygroup.getVisibility() == View.VISIBLE) {
		// switch (checkedId) {
		// case R.id.weekhistory:
		// currentstate = 0;
		// break;
		// case R.id.monthhistory:
		// currentstate = 1;
		// break;
		// case R.id.threemonthhistory:
		// currentstate = 2;
		// break;
		// case R.id.sixmonthhistory:
		// currentstate = 3;
		// break;
		//
		// default:
		// break;
		// }
		// }
		// }
		// });
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.weekhistory:
				currentstate = 0;
				break;
			case R.id.monthhistory:
				currentstate = 1;
				break;
			case R.id.threemonthhistory:
				currentstate = 2;
				break;
			case R.id.sixmonthhistory:
				currentstate = 3;
				break;

			default:
				break;
			}

			if (tabHost.getCurrentTab() == 1) {
				if (XueYaHistoryActivity.handler != null) {
					XueYaHistoryActivity.handler.sendEmptyMessage(currentstate);
				}
			} else if (tabHost.getCurrentTab() == 3) {
				if (XinLvHistoryActivity.handler != null) {
					XinLvHistoryActivity.handler.sendEmptyMessage(currentstate);
				}
			}
		}
	};
}

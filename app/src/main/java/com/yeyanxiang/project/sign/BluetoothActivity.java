package com.yeyanxiang.project.sign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.ContinueDBUtil;
import com.yeyanxiang.project.pub.util.PubUtil;
import com.yeyanxiang.util.SharedPfsUtil;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.ToggleButton;

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
public class BluetoothActivity extends Activity {
	private Context context;
	private Button search;
	private ToggleButton bluetoothopen;
	private ListView listView;
	private BluetoothAdapter bluetoothAdapter;
	private static ToggleButton savebutton;
	private static boolean savedata = false;

	private SimpleAdapter bluetootharrayadapter;
	private List<HashMap<String, String>> data;
	public static BluetoothSocket bluetoothSocket = null;
	// private final String blueaddress = "00:12:04:05:92:50";
	private final String blueaddress = "00:12:09:12:00:37";
	private final String DEVICESADDRESS = "devicesaddress";

	private LinearLayout bluetoothlLayout;
	private RelativeLayout electrolayout;
	private long starttime = 0;
	private ContinueDBUtil dbUtil;
	private boolean ending = true;

	private static List<ContentValues> savelist;
	private SharedPfsUtil sharedPfsUtil;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				// setContentView(R.layout.electrocardio);
				electrolayout.setVisibility(View.VISIBLE);
				bluetoothlLayout.setVisibility(View.GONE);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetoothlist);
		context = this;
		sharedPfsUtil = new SharedPfsUtil(context, PubUtil.USER_DATA);
		init();
		dbUtil = new ContinueDBUtil(context);

		if (bluetoothAdapter != null) {
			if (bluetoothAdapter.isEnabled()) {
				final ProgressDialog dialog = ProgressDialog.show(context,
						"设备连接", "正在连接设备，请稍后...");
				dialog.setOnKeyListener(new OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							dialog.dismiss();
							return true;
						}
						return false;
					}
				});
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							if (bluetoothSocket != null) {
								bluetoothSocket = null;
							}
							BluetoothDevice device = bluetoothAdapter
									.getRemoteDevice(sharedPfsUtil.getValue(
											DEVICESADDRESS, blueaddress));
							bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID
									.fromString("00001101-0000-1000-8000-00805F9B34FB"));
							bluetoothSocket.connect();
							handler.sendEmptyMessage(1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (bluetoothSocket != null) {
								try {
									bluetoothSocket.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
							System.out.println("IOException");
						}

						dialog.dismiss();
					}
				}).start();
			}
		}

	}

	private void init() {
		// TODO Auto-generated method stub
		search = (Button) findViewById(R.id.bluetoothsearch);
		bluetoothopen = (ToggleButton) findViewById(R.id.bluetoothopen);
		listView = (ListView) findViewById(R.id.bluetoothlist);
		bluetoothlLayout = (LinearLayout) findViewById(R.id.bluetoothlayout);
		electrolayout = (RelativeLayout) findViewById(R.id.electrolayout);
		savebutton = (ToggleButton) findViewById(R.id.savedatabutton);

		savebutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				savedata = isChecked;
				if (isChecked) {
					if (savelist == null) {
						savelist = new ArrayList<ContentValues>();
					}
					starttime = System.currentTimeMillis();
				} else {
					final long stoptime = System.currentTimeMillis();
					ContentValues contentValues = new ContentValues();
					// contentValues.put(ContinueDBUtil.sign_time,
					// System.currentTimeMillis());
					contentValues
							.put(ContinueDBUtil.sign_start_time, starttime);
					contentValues.put(ContinueDBUtil.sign_stop_time, stoptime);
					contentValues.put(ContinueDBUtil.sign_endflag, 1);

					dbUtil.insert(contentValues, ContinueDBUtil.sign_TABLE_NAME);

					// savelist.add(contentValues);

					if (ending) {
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								ending = false;
								while (savelist.size() > 0) {
									ContentValues values = savelist.remove(0);
									System.out.println("本地"
											+ dbUtil.insert(
													values,
													ContinueDBUtil.sign_TABLE_NAME));
									// System.out.println(dbUtil.insert(values)
									// + "---" + savelist.size());
								}
								ending = true;
							}
						}).start();
					}
				}
			}
		});

		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (bluetoothAdapter == null) {
					showtext(context, "没有找到蓝牙设备");
				} else {
					data = new ArrayList<HashMap<String, String>>();
					if (bluetoothAdapter.isEnabled()) {
						if (bluetoothAdapter.isDiscovering()) {
							showtext(context, "搜索中");
						} else {
							Set<BluetoothDevice> devices = bluetoothAdapter
									.getBondedDevices();
							if (devices.size() > 0) {
								for (BluetoothDevice device : devices) {
									HashMap<String, String> map = new HashMap<String, String>();
									map.put("name", device.getName()
											+ "\t(已配对)");
									map.put("address", device.getAddress());
									data.add(map);
								}
							}

							bluetootharrayadapter = new SimpleAdapter(context,
									data, android.R.layout.simple_list_item_2,
									new String[] { "name", "address" },
									new int[] { android.R.id.text1,
											android.R.id.text2 });
							listView.setAdapter(bluetootharrayadapter);
							bluetoothAdapter.startDiscovery();
						}
					} else {
						showtext(context, "请先启用蓝牙设备");
					}
				}
			}
		});

		bluetoothopen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (bluetoothAdapter == null) {
					showtext(context, "没有找到蓝牙设备");
				} else {
					if (isChecked) {
						if (!bluetoothAdapter.isEnabled()) {
							bluetoothAdapter.enable();
						}
					} else {
						if (bluetoothAdapter.isEnabled()) {
							bluetoothAdapter.disable();
						}
					}
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				if (bluetoothAdapter != null) {
					if (bluetoothAdapter.isDiscovering()) {
						bluetoothAdapter.cancelDiscovery();
					}
				}

				final BluetoothDevice device = bluetoothAdapter
						.getRemoteDevice(data.get(position).get("address"));

				if (bluetoothSocket != null) {
					try {
						bluetoothSocket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					bluetoothSocket = null;
				}

				final ProgressDialog dialog = ProgressDialog.show(context,
						"设备连接", "正在连接设备，请稍后...");

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID
									.fromString("00001101-0000-1000-8000-00805F9B34FB"));
							bluetoothSocket.connect();
							// startActivity(new Intent(context,
							// ElectrocardioActivity.class));
							sharedPfsUtil.putValue(DEVICESADDRESS,
									data.get(position).get("address"));
							handler.sendEmptyMessage(1);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							if (bluetoothSocket != null) {
								try {
									bluetoothSocket.close();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
							}
						}

						dialog.dismiss();
					}
				}).start();
			}
		});

		IntentFilter filter = new IntentFilter();
		filter.addAction(BluetoothDevice.ACTION_FOUND);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
		filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(mReceiver, filter);
	}

	public static boolean getsavedata() {
		return savedata;
	}

	public static void stopsavedata() {
		if (savebutton != null) {
			savebutton.setChecked(false);
		}
	}

	public static List<ContentValues> getsavelist() {
		return savelist;
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
				showtext(context, "搜索结束!");
			} else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {
				showtext(context, "开始搜索设备");
			} else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("name", device.getName());
					map.put("address", device.getAddress());
					data.add(map);
					bluetootharrayadapter.notifyDataSetChanged();
				}
			}
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (bluetoothAdapter == null) {
			showtext(context, "没有找到蓝牙设备");
		} else {
			if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
				bluetoothopen.setChecked(true);
			} else if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_OFF) {
				bluetoothopen.setChecked(false);
			}
		}
	}

	private void showtext(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mReceiver);
		dbUtil.close();
		if (bluetoothSocket != null) {
			try {
				bluetoothSocket.close();
				bluetoothSocket = null;
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}

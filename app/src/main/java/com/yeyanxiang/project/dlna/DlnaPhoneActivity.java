package com.yeyanxiang.project.dlna;

import java.util.ArrayList;
import java.util.List;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.DeviceChangeListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import com.yeyanxiang.project.R;
import com.yeyanxiang.project.pub.util.PubUtil;
import com.yeyanxiang.util.dlna.CommonUtil;
import com.yeyanxiang.util.dlna.DLNAUtil;
import com.yeyanxiang.util.dlna.Item;
import com.yeyanxiang.util.dlna.UpnpUtil;
import com.yeyanxiang.view.activity.ImageShowActivity;

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
public class DlnaPhoneActivity extends Activity {
	private Context context;
	private Button searchButton;
	private ProgressBar progressBar;
	private ExpandableListView deviceListView;
	private List<TreeNode> list;
	private DeviceListAdapter adapter;
	private ProgressDialog progressDialog;
	private DLNAUtil dlnaUtil;
	private List<Item> items;
	private Item item;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				CommonUtil.showToask(context, "网络异常");
				progressBar.setVisibility(View.GONE);
			} else if (msg.what == 1) {
				Device device = (Device) msg.obj;
				if (UpnpUtil.isValidDevice(device)) {
					// if (progressDialog != null && progressDialog.isShowing())
					// {
					// progressDialog.dismiss();
					// }
					progressBar.setVisibility(View.GONE);

					TreeNode treeNode = new TreeNode();
					treeNode.device = device;
					list.add(treeNode);
					adapter.notifyDataSetChanged();
				}
			} else if (msg.what == 2) {
				progressBar.setVisibility(View.GONE);
				if (items != null && items.size() > 0) {
					list.get(msg.arg1).stack.add(items);
					list.get(msg.arg1).items = items;
					adapter.notifyDataSetChanged();
				} else {
					CommonUtil.showToask(context, "获取列表失败");
				}
			} else if (msg.what == 3) {
				progressBar.setVisibility(View.GONE);
				if (items != null && items.size() > 0) {
					items.add(0, item);
					list.get(msg.arg1).stack.add(items);
					list.get(msg.arg1).items = items;
					adapter.notifyDataSetChanged();
				} else {
					CommonUtil.showToask(context, "获取列表失败");
				}
			} else if (msg.what == 4) {
				progressBar.setVisibility(View.GONE);
				if (items != null && items.size() > 0) {
					list.get(msg.arg1).items = items;
					adapter.notifyDataSetChanged();
				} else {
					CommonUtil.showToask(context, "获取列表失败");
				}
			} else if (msg.what == 5) {
				progressBar.setVisibility(View.GONE);
			} else if (msg.what == 6) {
				list.get(msg.arg1).stack.clear();
				list.remove(msg.arg1);
				adapter.notifyDataSetChanged();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlnaphone);
		context = this;
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		searchButton = (Button) findViewById(R.id.searchbutton);
		progressBar = (ProgressBar) findViewById(R.id.progressBar2);
		deviceListView = (ExpandableListView) findViewById(R.id.devicelist);

		item = new Item();
		item.setTitle("..");

		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				adapter.clear();
				search();
			}
		});

		// progressDialog = ProgressDialog.show(context, null, "Loding...");
		list = new ArrayList<TreeNode>();
		adapter = new DeviceListAdapter(context, list, 50);
		deviceListView.setAdapter(adapter);

		deviceListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					final int groupPosition, final int childPosition, long id) {
				// TODO Auto-generated method stub
				// System.out.println("------onChildClick--------");
				progressBar.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Looper.prepare();
						Item item = list.get(groupPosition).items
								.get(childPosition);
						// System.out.println(item.getShowString());

						if (UpnpUtil.ismedia(item)) {
							handler.sendEmptyMessage(5);
							try {
								Intent intent = new Intent(Intent.ACTION_VIEW);
								if (UpnpUtil.isAudioItem(item)) {
									intent.setDataAndType(
											Uri.parse(item.getRes()), "audio/*");
								} else if (UpnpUtil.isVideoItem(item)) {
									intent.setDataAndType(
											Uri.parse(item.getRes()), "video/*");
								} else if (UpnpUtil.isPictureItem(item)) {
									// intent.setDataAndType(
									// Uri.parse(item.getRes()), "image/*");
									intent = new Intent(context,
											ImageShowActivity.class);
									intent.putExtra(ImageShowActivity.IMAGEURL,
											item.getRes());
								}

								if (intent != null) {
									startActivity(intent);
								}
							} catch (Exception e) {
								// TODO: handle exception
								PubUtil.showText(context, "播放失败");
							}
						} else {
							if ("..".equals(item.getTitle())) {
								if (!list.get(groupPosition).stack.isEmpty()) {
									list.get(groupPosition).stack.pop();
								}
								if (list.get(groupPosition).stack.isEmpty()) {
									items = null;
								} else {
									items = list.get(groupPosition).stack
											.peek();
								}
								Message message = new Message();
								message.what = 4;
								message.arg1 = groupPosition;
								handler.sendMessage(message);
							} else {
								items = dlnaUtil.getItems(
										list.get(groupPosition).device,
										item.getStringid());
								Message message = new Message();
								message.what = 3;
								message.arg1 = groupPosition;
								handler.sendMessage(message);
							}
						}

						Looper.loop();
					}
				}).start();
				return false;
			}
		});

		deviceListView
				.setOnGroupCollapseListener(new OnGroupCollapseListener() {

					@Override
					public void onGroupCollapse(int groupPosition) {
						// TODO Auto-generated method stub
						// 合
						// System.out.println("------onGroupCollapse--------");
						list.get(groupPosition).stack.clear();
						list.get(groupPosition).items.clear();
						adapter.notifyDataSetChanged();
					}
				});
		deviceListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(final int groupPosition) {
				// TODO Auto-generated method stub
				// 开
				// System.out.println("------onGroupExpand--------");
				progressBar.setVisibility(View.VISIBLE);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						items = dlnaUtil.getDirectory(adapter
								.getGroup(groupPosition));
						Message message = new Message();
						message.what = 2;
						message.arg1 = groupPosition;
						handler.sendMessage(message);
					}
				}).start();
			}
		});

		dlnaUtil = new DLNAUtil();
		dlnaUtil.setDeviceChangeListener(changeListener);
		search();
	}

	private void search() {
		progressBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (CommonUtil.checkNetState(context)) {
					try {
						dlnaUtil.search();
					} catch (Exception e) {
						// TODO Auto-generated catch block
					}
				} else {
					handler.sendEmptyMessage(0);
				}
			}
		}).start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dlnaUtil.stop();
	}

	private DeviceChangeListener changeListener = new DeviceChangeListener() {

		@Override
		public void deviceRemoved(Device arg0) {
			// TODO Auto-generated method stub
			if (UpnpUtil.isValidDevice(arg0)) {
				String uid = arg0.getUDN();
				for (int i = 0; i < list.size(); i++) {
					if (uid.equalsIgnoreCase(list.get(i).device.getUDN())) {
						Message message = new Message();
						message.what = 6;
						message.arg1 = i;
						handler.sendMessage(message);
						break;
					}
				}
			}
		}

		@Override
		public void deviceAdded(Device arg0) {
			// TODO Auto-generated method stub
			Message message = new Message();
			message.what = 1;
			message.obj = arg0;
			handler.sendMessage(message);
		}
	};

}

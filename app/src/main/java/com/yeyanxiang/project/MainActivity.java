package com.yeyanxiang.project;

import java.util.ArrayList;

import com.yeyanxiang.project.androidpn.AndroidpnMainActivity;
import com.yeyanxiang.project.applist.ApplistActivity;
import com.yeyanxiang.project.badgeview.BadgeViewActivity;
import com.yeyanxiang.project.circlemenu.CircleMenuActivity;
import com.yeyanxiang.project.diy.LayoutCollectActivity;
import com.yeyanxiang.project.diy.RadialMenuActivity;
import com.yeyanxiang.project.diy.TextVerticalActivity;
import com.yeyanxiang.project.dlna.DlnaPhoneActivity;
import com.yeyanxiang.project.draw.DrawActivity;
import com.yeyanxiang.project.gag.GaGMainActivity;
import com.yeyanxiang.project.gallery3d.Gallery3DActivity;
import com.yeyanxiang.project.googlemap.ZhiNanZhenActivity;
import com.yeyanxiang.project.image.BitmapFragmentActivity;
import com.yeyanxiang.project.image.ImageActivity1;
import com.yeyanxiang.project.image.ImageActivity2;
import com.yeyanxiang.project.image.ImgGroupActivity;
import com.yeyanxiang.project.indicator.IndicatorListSamples;
import com.yeyanxiang.project.listviewanimations.ListViewAnimMainActivity;
import com.yeyanxiang.project.mediaplayer.MediaPlayActivity;
import com.yeyanxiang.project.mylist.CircleListViewActivity;
import com.yeyanxiang.project.mylist.ElasticityListViewActivity;
import com.yeyanxiang.project.mylist.ExpandableListViewActivity;
import com.yeyanxiang.project.mylist.ListView3DActivity;
import com.yeyanxiang.project.mylist.SlideCutListActivity;
import com.yeyanxiang.project.mylist.SystemExpandableListActivity;
import com.yeyanxiang.project.phoneinfo.PhoneInfoActivity;
import com.yeyanxiang.project.pickerview.PickerViewActivity;
import com.yeyanxiang.project.progressbar.SmoothProgressbarActivity;
import com.yeyanxiang.project.pub.util.MyEntry;
import com.yeyanxiang.project.pub.util.PubUtil;
import com.yeyanxiang.project.reference.PullListActivity;
import com.yeyanxiang.project.reference.PullRefreshListActivity;
import com.yeyanxiang.project.reference.ReferenceActivity;
import com.yeyanxiang.project.referencedemo.PullToRefreshLauncherActivity;
import com.yeyanxiang.project.resideMenu.ResideMenuActivity;
import com.yeyanxiang.project.setting.SettingActivity;
import com.yeyanxiang.project.sign.BluetoothActivity;
import com.yeyanxiang.project.sign.ElectrocardioOffLineActivity;
import com.yeyanxiang.project.sign.SignHistoryTabActivity;
import com.yeyanxiang.project.slidingmenu.SlidingMenuActivity;
import com.yeyanxiang.project.slidingmenudemo.SlideExampleListActivity;
import com.yeyanxiang.project.tab.FleepTabActivity;
import com.yeyanxiang.project.tab.MyTabActivity;
import com.yeyanxiang.project.timeline.TimeLineActivity;
import com.yeyanxiang.project.weather.PmActivity;
import com.yeyanxiang.project.weather.WeatherForecastActivity;
import com.yeyanxiang.project.webview.WebViewActivity;
import com.yeyanxiang.project.xmpp.FormLogin;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.BadgeView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
public class MainActivity extends Activity {
	private ListView listView;
	private EditText editText;
	private ArrayList<MyEntry> list;
	private ArrayList<MyEntry> searchlist;
	private Context context;
	private PackageManager pm;
	private String search = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = MainActivity.this;
		pm = getPackageManager();
		init();
	}

	private void init() {
		listView = (ListView) findViewById(R.id.functionlist);
		listView.setLayoutAnimation(PubUtil.getListAnim());
		editText = (EditText) findViewById(R.id.function);

		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				// System.out.println("---onTextChanged---" + s + "---" + start
				// + "----" + before + "---" + count);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				// System.out.println("---beforeTextChanged---" + s + "---"
				// + start + "----" + after + "---" + count);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				// System.out.println("---afterTextChanged---" + s.toString());
				search = s.toString().trim().toLowerCase();
				if (TextUtils.isEmpty(search)) {
					listView.setAdapter(new Myadapter(list));
				} else {
					searchlist.clear();
					for (int i = 0; i < list.size(); i++) {
						if (list.get(i).getKey().toLowerCase().contains(search)) {
							searchlist.add(list.get(i));
						}
					}
					listView.setAdapter(new Myadapter(searchlist));
				}
			}
		});

		listView.setOnItemClickListener(itemClickListener);
		listView.setOnItemSelectedListener(itemSelectedListener);
		list = new ArrayList<MyEntry>();

		list.add(new MyEntry("DIY View", new Intent(context,
				LayoutCollectActivity.class)));
		list.add(new MyEntry("Badge View", new Intent(context,
				BadgeViewActivity.class)));
		list.add(new MyEntry("APP List", new Intent(context,
				ApplistActivity.class)));
		list.add(new MyEntry("Player", new Intent(context,
				MediaPlayActivity.class)));
		list.add(new MyEntry("Sliding Menu", new Intent(context,
				SlidingMenuActivity.class)));
		list.add(new MyEntry("Sliding Menu Demo", new Intent(context,
				SlideExampleListActivity.class)));
		list.add(new MyEntry("Indicator", new Intent(context,
				IndicatorListSamples.class)));
		list.add(new MyEntry("Circle Menu", new Intent(context,
				CircleMenuActivity.class)));
		list.add(new MyEntry("Radia Menu", new Intent(context,
				RadialMenuActivity.class)));
		list.add(new MyEntry("Reside Menu", new Intent(context,
				ResideMenuActivity.class)));
		list.add(new MyEntry("Picker View", new Intent(context,
				PickerViewActivity.class)));
		list.add(new MyEntry("WebView", new Intent(context,
				WebViewActivity.class)));
		list.add(new MyEntry("SettingLayout", new Intent(context,
				SettingActivity.class)));
		list.add(new MyEntry("SmoothProgressBar", new Intent(context,
				SmoothProgressbarActivity.class)));
		list.add(new MyEntry("VerticalTextView", new Intent(context,
				TextVerticalActivity.class)));
		list.add(new MyEntry("PhoneInfo", new Intent(context,
				PhoneInfoActivity.class)));
		list.add(new MyEntry("ListViewAnimation", new Intent(context,
				ListViewAnimMainActivity.class)));
		list.add(new MyEntry("9GaG", new Intent(context, GaGMainActivity.class)));
		list.add(new MyEntry("时间轴", new Intent(context, TimeLineActivity.class)));
		list.add(new MyEntry("下拉刷新", new Intent(context,
				PullToRefreshLauncherActivity.class)));
		list.add(new MyEntry("Android 自带下拉刷新", new Intent(context,
				ReferenceActivity.class)));
		list.add(new MyEntry("自定义List1(下拉刷新)", new Intent(context,
				PullRefreshListActivity.class)));
		list.add(new MyEntry("自定义List1(下拉刷新2)", new Intent(context,
				PullListActivity.class)));
		list.add(new MyEntry("自定义List2(分层List)", new Intent(context,
				ExpandableListViewActivity.class)));
		list.add(new MyEntry("自定义List3(仿QQ List)", new Intent(context,
				SystemExpandableListActivity.class)));
		list.add(new MyEntry("自定义List4(仿Iphone List带弹性)", new Intent(context,
				ElasticityListViewActivity.class)));
		list.add(new MyEntry("自定义List5(3D效果)", new Intent(context,
				ListView3DActivity.class)));
		list.add(new MyEntry("自定义List6(侧滑删除)", new Intent(context,
				SlideCutListActivity.class)));
		list.add(new MyEntry("自定义List7(圆弧ListView)", new Intent(context,
				CircleListViewActivity.class)));
		list.add(new MyEntry("自定义Gallery", new Intent(context,
				Gallery3DActivity.class)));
		list.add(new MyEntry("自定义画笔", new Intent(context, DrawActivity.class)));
		list.add(new MyEntry("自定义Tab风格", new Intent(context,
				MyTabActivity.class)));
		list.add(new MyEntry("滑动切换Tab", new Intent(context,
				FleepTabActivity.class)));
		list.add(new MyEntry("指南针", new Intent(context,
				ZhiNanZhenActivity.class)));
		list.add(new MyEntry("天气预报", new Intent(context,
				WeatherForecastActivity.class)));
		list.add(new MyEntry("空气质量", new Intent(context, PmActivity.class)));
		list.add(new MyEntry("多点触控1", new Intent(context, ImageActivity1.class)));
		list.add(new MyEntry("多点触控2", new Intent(context, ImageActivity2.class)));
		list.add(new MyEntry("图片组浏览", new Intent(context,
				ImgGroupActivity.class)));
		list.add(new MyEntry("加载网络图片", new Intent(context,
				BitmapFragmentActivity.class)));
		list.add(new MyEntry("体征实时监护", new Intent(context,
				BluetoothActivity.class)));
		list.add(new MyEntry("体征历史图表", new Intent(context,
				SignHistoryTabActivity.class)));
		list.add(new MyEntry("体征模拟数据", new Intent(context,
				ElectrocardioOffLineActivity.class)));
		list.add(new MyEntry("消息推送", new Intent(context,
				AndroidpnMainActivity.class)));
		list.add(new MyEntry("消息推送2 openfire", new Intent(context,
				FormLogin.class)));
		list.add(new MyEntry("DLNA之phone端", new Intent(context,
				DlnaPhoneActivity.class)));
		list.add(new MyEntry(
				"DLNA多媒体共享",
				pm.getLaunchIntentForPackage("com.bubblesoft.android.bubbleupnp"),
				true));
		list.add(new MyEntry("百度地图 Demo", pm
				.getLaunchIntentForPackage("baidumapsdk.demo"), true));
		list.add(new MyEntry("高德地图 Demo", pm
				.getLaunchIntentForPackage("com.amapv2.cn.apis"), true));
		list.add(new MyEntry("Jetsen 通讯录", pm
				.getLaunchIntentForPackage("jetsen.contact"), true));
		list.add(new MyEntry("移动政务", pm
				.getLaunchIntentForPackage("mobile.gov.show"), true));
		list.add(new MyEntry("南宁报料", pm
				.getLaunchIntentForPackage("com.jetsen.phone"), true));
		list.add(new MyEntry("天安门应急现场", pm
				.getLaunchIntentForPackage("tam.emergency"), true));
		list.add(new MyEntry("天安门微视频审核", pm
				.getLaunchIntentForPackage("com.jetsen.tam.mobile.audit"), true));
		list.add(new MyEntry("北京台", pm
				.getLaunchIntentForPackage("com.beijing.tv"), true));
		list.add(new MyEntry("北京台（演示版）", pm
				.getLaunchIntentForPackage("com.beijing.tv.show"), true));
		list.add(new MyEntry("智慧交通手机端", pm
				.getLaunchIntentForPackage("com.jetsen.smart.traffic.phone"),
				true));
		list.add(new MyEntry("智慧教育", pm
				.getLaunchIntentForPackage("com.jetsen.education"), true));
		list.add(new MyEntry("美院作品展示", pm
				.getLaunchIntentForPackage("com.jetsen.education.art"), true));
		listView.setAdapter(new Myadapter(list));
		searchlist = new ArrayList<MyEntry>();
	}

	private OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent intent = ((MyEntry) listView.getAdapter().getItem(position))
					.getIntent();
			if (intent == null) {
				PubUtil.showText(context, "程序未安装");
			} else {
				startActivity(intent);
			}
		}
	};

	private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			System.out.println("slect");
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private class Myadapter extends BaseAdapter {
		private ArrayList<MyEntry> list;
		private LayoutInflater inflater;
		private final int droidGreen = Color.parseColor("#A4C639");

		public Myadapter(ArrayList<MyEntry> list) {
			super();
			this.list = list;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.mainlistitem, null);
				viewHolder = new ViewHolder();
				viewHolder.textView = (TextView) convertView
						.findViewById(R.id.listappname);
				viewHolder.badge = new BadgeView(context, viewHolder.textView);
				viewHolder.badge.setBadgeBackgroundColor(droidGreen);
				viewHolder.badge.setTextColor(Color.BLACK);
				viewHolder.badge.setText("app");
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			if (list.get(position).Isapp()) {
				viewHolder.badge.show();
			} else {
				viewHolder.badge.hide();
			}
			String name = list.get(position).getKey();
			if (TextUtils.isEmpty(search)) {
				viewHolder.textView.setText(name);
			} else {
				if (name.toLowerCase().contains(search)) {
					int start = name.toLowerCase().indexOf(search);
					SpannableStringBuilder stytle = new SpannableStringBuilder(
							name);
					stytle.setSpan(new BackgroundColorSpan(Color.BLUE), start,
							start + search.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					viewHolder.textView.setText(stytle);
				} else {
					viewHolder.textView.setText(name);
				}
			}
			return convertView;
		}

		class ViewHolder {
			TextView textView;
			BadgeView badge;
		}
	}
}

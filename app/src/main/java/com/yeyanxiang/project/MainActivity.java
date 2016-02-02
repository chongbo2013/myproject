package com.yeyanxiang.project;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
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

import com.yeyanxiang.project.androidpn.AndroidpnMainActivity;
import com.yeyanxiang.project.applist.ApplistActivity;
import com.yeyanxiang.project.badgeview.BadgeViewActivity;
import com.yeyanxiang.project.circlemenu.CircleMenuActivity;
import com.yeyanxiang.project.circular.CircularProgressActivity;
import com.yeyanxiang.project.diy.LayoutCollectActivity;
import com.yeyanxiang.project.diy.RadialMenuActivity;
import com.yeyanxiang.project.diy.TextVerticalActivity;
import com.yeyanxiang.project.dlna.DlnaPhoneActivity;
import com.yeyanxiang.project.dragsort.DragSortActivity;
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
import com.yeyanxiang.project.slider.SliderActivity;
import com.yeyanxiang.project.slidingmenu.SlidingMenuActivity;
import com.yeyanxiang.project.slidingmenudemo.SlideExampleListActivity;
import com.yeyanxiang.project.tab.FleepTabActivity;
import com.yeyanxiang.project.tab.MyTabActivity;
import com.yeyanxiang.project.timeline.TimeLineActivity;
import com.yeyanxiang.project.toast.ToastActivity;
import com.yeyanxiang.project.weather.PmActivity;
import com.yeyanxiang.project.weather.WeatherForecastActivity;
import com.yeyanxiang.project.webview.WebViewActivity;
import com.yeyanxiang.project.xmpp.FormLogin;
import com.yeyanxiang.util.ToastUtil;
import com.yeyanxiang.util.entry.Entry;
import com.yeyanxiang.view.BadgeView;

import java.util.ArrayList;

/**
 * @author 叶雁翔
 * @version 1.0
 * @Email yanxiang1120@gmail.com
 * @update 2014年3月13日
 * @简介
 */
public class MainActivity extends com.yeyanxiang.project.activity.BaseActivity {
    private ListView listView;
    private EditText editText;
    private ArrayList<Entry> functionList;
    private ArrayList<Entry> searchList;
    private Context mContext;
    private PackageManager pm;
    private String search = "";

    @Override
    public String getTAG() {
        return "MainActivity";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        pm = getPackageManager();
        init();
    }

    private void init() {
        listView = (ListView) findViewById(R.id.functionlist);
        listView.setLayoutAnimation(PubUtil.getListAnim());
        editText = (EditText) findViewById(R.id.function);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                // System.out.println("---onTextChanged---" + s + "---" + start
                // + "----" + before + "---" + count);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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
                    listView.setAdapter(new FunctionListAdapter(functionList));
                } else {
                    searchList.clear();
                    for (int i = 0; i < functionList.size(); i++) {
                        if (functionList.get(i).getKey().toLowerCase().contains(search)) {
                            searchList.add(functionList.get(i));
                        }
                    }
                    listView.setAdapter(new FunctionListAdapter(searchList));
                }
            }
        });

        listView.setOnItemClickListener(itemClickListener);
        listView.setOnItemSelectedListener(itemSelectedListener);
        functionList = new ArrayList<>();

        functionList.add(new Entry<Class>("DIY View", LayoutCollectActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Badge View", BadgeViewActivity.class, Entry.Type.CLASS));//https://github.com/elevenetc/BadgeView.git
        functionList.add(new Entry<Class>("APP List", ApplistActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Player", MediaPlayActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Sliding Menu", SlidingMenuActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Sliding Menu Demo", SlideExampleListActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Indicator", IndicatorListSamples.class, Entry.Type.CLASS));//https://github.com/JakeWharton/ViewPagerIndicator.git
        functionList.add(new Entry<Class>("Circle Menu", CircleMenuActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Radia Menu", RadialMenuActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Reside Menu", ResideMenuActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Picker View", PickerViewActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("WebView", WebViewActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("SettingLayout", SettingActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("SmoothProgressBar", SmoothProgressbarActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("VerticalTextView", TextVerticalActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("PhoneInfo", PhoneInfoActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("ListViewAnimation", ListViewAnimMainActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("9GaG", GaGMainActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("时间轴", TimeLineActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("下拉刷新", PullToRefreshLauncherActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("Android 自带下拉刷新", ReferenceActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List1(下拉刷新)", PullRefreshListActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List1(下拉刷新2)", PullListActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List2(分层List)", ExpandableListViewActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List3(仿QQ List)", SystemExpandableListActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List4(仿Iphone List带弹性)", ElasticityListViewActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List5(3D效果)", ListView3DActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List6(侧滑删除)", SlideCutListActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义List7(圆弧ListView)", CircleListViewActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义Gallery", Gallery3DActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义画笔", DrawActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("自定义Tab风格", MyTabActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("滑动切换Tab", FleepTabActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("指南针", ZhiNanZhenActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("天气预报", WeatherForecastActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("空气质量", PmActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("多点触控1", ImageActivity1.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("多点触控2", ImageActivity2.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("图片组浏览", ImgGroupActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("加载网络图片", BitmapFragmentActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("体征实时监护", BluetoothActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("体征历史图表", SignHistoryTabActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("体征模拟数据", ElectrocardioOffLineActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("消息推送", AndroidpnMainActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("消息推送2 openfire", FormLogin.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("DLNA之phone端", DlnaPhoneActivity.class, Entry.Type.CLASS));
        functionList.add(new Entry<Class>("LoadToast", ToastActivity.class, Entry.Type.CLASS));//https://github.com/code-mc/loadtoast.git
        functionList.add(new Entry<Class>("DragSort", DragSortActivity.class, Entry.Type.CLASS));//https://github.com/vinc3m1/DragSortAdapter.git
        functionList.add(new Entry<Class>("Circular Progress", CircularProgressActivity.class, Entry.Type.CLASS));//https://github.com/dmytrodanylyk/circular-progress-button.git
        functionList.add(new Entry<Class>("Slider Animations", SliderActivity.class, Entry.Type.CLASS));//https://github.com/daimajia/AndroidImageSlider.git

//        functionList.add(new Entry<String>("DLNA多媒体共享", "com.bubblesoft.android.bubbleupnp", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("百度地图 Demo", "baidumapsdk.demo", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("高德地图 Demo", "com.amapv2.cn.apis", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("Jetsen 通讯录", "jetsen.contact", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("移动政务", "mobile.gov.show", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("南宁报料", "com.jetsen.phone", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("天安门应急现场", "tam.emergency", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("天安门微视频审核", "com.jetsen.tam.mobile.audit", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("北京台", "com.beijing.tv", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("北京台（演示版）", "com.beijing.tv.show", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("智慧交通手机端", "com.jetsen.smart.traffic.phone", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("智慧教育", "com.jetsen.education", Entry.Type.PACKAGE));
//        functionList.add(new Entry<String>("美院作品展示", "com.jetsen.education.art", Entry.Type.PACKAGE));
        listView.setAdapter(new FunctionListAdapter(functionList));
        searchList = new ArrayList<>();
    }

    private OnItemClickListener itemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub
            Intent intent = null;
            Entry entry = (Entry) listView.getAdapter().getItem(position);
            if (entry != null) {
                Object value = entry.getValue();
                if (value instanceof Class) {
                    logi("jump to Activity ");
                    intent = new Intent(MainActivity.this, (Class<?>) value);
                } else if (value instanceof String) {
                    if (entry.getType() == Entry.Type.PACKAGE) {
                        logi("jump to package");
                        intent = pm.getLaunchIntentForPackage((String) value);
                    } else {
                        logi("jump value " + value);
                    }
                }

                if (intent == null) {
                    ToastUtil.showText(MainActivity.this, "not found application!");
                } else {
                    startActivity(intent);
                }
            } else {
                ToastUtil.showText(MainActivity.this, "参数有误!");
            }
        }
    };

    private OnItemSelectedListener itemSelectedListener = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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

    private class FunctionListAdapter extends BaseAdapter {
        private ArrayList<Entry> list;
        private LayoutInflater inflater;
        private final int droidGreen = Color.parseColor("#A4C639");

        public FunctionListAdapter(ArrayList<Entry> list) {
            super();
            this.list = list;
            inflater = LayoutInflater.from(mContext);
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
                viewHolder.textView = (TextView) convertView.findViewById(R.id.listappname);
                viewHolder.badge = new BadgeView(mContext, viewHolder.textView);
                viewHolder.badge.setBadgeBackgroundColor(droidGreen);
                viewHolder.badge.setTextColor(Color.BLACK);
                viewHolder.badge.setText("app");
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Entry value = list.get(position);
            if (value != null && value.getType() == Entry.Type.PACKAGE) {
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
                    SpannableStringBuilder stytle = new SpannableStringBuilder(name);
                    stytle.setSpan(new BackgroundColorSpan(Color.BLUE), start, start + search.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
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

package com.yeyanxiang.project.applist;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.animation.ListItemAnimation;
import com.yeyanxiang.view.animation.OnViewScrollListener;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 
 * Create on 2013-5-7 上午10:09:10 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 应用程序列表页
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class AppList extends Activity {
	private Context context;
	private PackageManager pm;
	private HashMap<String, SoftReference<Bitmap>> bitmaps;
	@ViewInject(R.id.functionlist)
	private ListView listView;
	@ViewInject(R.id.function)
	private EditText editText;
	private String search = "";
	private ArrayList<PackageInfo> apps;
	private ArrayList<PackageInfo> searchlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = this;
		ViewUtils.inject(this);

		if (getIntent() != null) {
			pm = getPackageManager();
			apps = getIntent().getParcelableArrayListExtra("value");
			searchlist = new ArrayList<PackageInfo>();
			listView.setAdapter(new AppListAdapter(apps, this, pm));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(final AdapterView<?> parent, View view,
						final int position, long id) {
					// TODO Auto-generated method stub

					View footer = view.findViewById(R.id.footer);
					ListItemAnimation animation = new ListItemAnimation(footer,
							300);
					footer.startAnimation(animation);
					if (position == parent.getCount() - 1) {
						animation
								.setOnViewScrollListener(new OnViewScrollListener() {

									@Override
									public void scroll(ListItemAnimation arg0) {
										// TODO Auto-generated method stub
										listView.setSelection(position);
									}
								});
					}
				}
			});

			editText.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					try {
						search = s.toString().trim().toLowerCase();
						if (TextUtils.isEmpty(search)) {
							listView.setAdapter(new AppListAdapter(apps,
									context, pm));
						} else {
							searchlist.clear();
							for (int i = 0; i < apps.size(); i++) {
								if (apps.get(i).applicationInfo.loadLabel(pm)
										.toString().trim().toLowerCase()
										.contains(search)) {
									searchlist.add(apps.get(i));
								}
							}
							listView.setAdapter(new AppListAdapter(searchlist,
									context, pm));
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
		} else {
			finish();
		}
	}
}

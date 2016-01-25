package com.yeyanxiang.project.mylist;

import java.lang.reflect.Field;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.MatrixView;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AbsListView.OnScrollListener;

public class CircleListViewActivity extends ListActivity {
	private int[] images = new int[] { R.drawable.p1, R.drawable.p2,
			R.drawable.p3 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getListView().setBackgroundColor(Color.WHITE);
		getListView().setDividerHeight(0);

		setListAdapter(new MyAdapter());
		try {
			changeGroupFlag(getListView());
		} catch (Exception e) {
			e.printStackTrace();
		}

		getListView().setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				for (int i = 0; i < getListView().getChildCount(); i++) {
					getListView().getChildAt(i).invalidate();
				}
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return 9999;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				MatrixView m = (MatrixView) LayoutInflater.from(
						CircleListViewActivity.this).inflate(
						R.layout.view_list_item, null);
				m.setParentHeight(getListView().getHeight());
				convertView = m;
			}
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.image);
			imageView.setImageResource(images[position % images.length]);

			return convertView;
		}

	}

	public void changeGroupFlag(Object obj) throws Exception// 反射替换对所有String进行替换
	{
		Field[] f = obj.getClass().getSuperclass().getSuperclass()
				.getSuperclass().getDeclaredFields(); // 获得成员映射数组
		for (Field tem : f) // 迭代for循环
		{
			if (tem.getName().equals("mGroupFlags")) {
				tem.setAccessible(true);
				Integer mGroupFlags = (Integer) tem.get(obj); // 返回内容
				int newGroupFlags = mGroupFlags & 0xfffff8;
				tem.set(obj, newGroupFlags);// 替换成员值
			}
		}
	}
}

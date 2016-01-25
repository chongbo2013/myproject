package com.yeyanxiang.project.mylist;

import java.util.ArrayList;

import com.yeyanxiang.project.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<String> datas;

	public ItemAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	public void setData(ArrayList<String> datas) {
		this.datas = datas;
	}

	public int getCount() {
		return datas.size();
	}

	public Object getItem(int position) {
		return datas.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.slidecutlist_item, null);
			holder.textView = (TextView) convertView.findViewById(R.id.tv_item);
			holder.coating = (TextView) convertView
					.findViewById(R.id.tv_coating);
			holder.functions = (TextView) convertView
					.findViewById(R.id.tv_functions);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView.setText(datas.get(position));
		holder.coating.setVisibility(View.VISIBLE);

		holder.functions.setClickable(false);

		return convertView;
	}

	public final class ViewHolder {
		public TextView textView;
		public TextView coating;
		public TextView functions;
	}
}

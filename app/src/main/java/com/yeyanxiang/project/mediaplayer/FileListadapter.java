package com.yeyanxiang.project.mediaplayer;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import com.yeyanxiang.project.R;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Create on 2013-5-7 上午10:21:47 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * 
 * 简介: 文件检索适配器
 * 
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 * 
 * 
 */
public class FileListadapter extends BaseAdapter {
	private Context context;
	private List<HashMap<String, Object>> filearray;
	private String search;

	public FileListadapter(Context context,
			List<HashMap<String, Object>> filearray, String search) {
		super();
		this.context = context;
		this.filearray = filearray;
		this.search = search;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filearray.size();
	}

	@Override
	public HashMap<String, Object> getItem(int position) {
		// TODO Auto-generated method stub
		return filearray.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.filelistitem, null);
			viewHolder = new ViewHolder();
			viewHolder.filename = (TextView) convertView
					.findViewById(R.id.fileName);
			viewHolder.filetime = (TextView) convertView
					.findViewById(R.id.filetime);
			// viewHolder.pic = (ImageView) convertView
			// .findViewById(R.id.fileimage);
			viewHolder.video = (ImageView) convertView
					.findViewById(R.id.filevideo);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		File file = (File) filearray.get(position).get("file");
		int picid = (Integer) filearray.get(position).get("picid");
		if (TextUtils.isEmpty(search)) {
			viewHolder.filename.setText(file.getName());
		} else {
			String name = file.getName();
			if (name.toLowerCase().contains(search)) {
				int start = name.toLowerCase().indexOf(search);
				SpannableStringBuilder stytle = new SpannableStringBuilder(name);
				stytle.setSpan(new ForegroundColorSpan(Color.BLUE), start,
						start + search.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				viewHolder.filename.setText(stytle);
			} else {
				viewHolder.filename.setText(name);
			}

		}
		viewHolder.filetime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date(file.lastModified()))
				+ "\t"
				+ ((int) (file.length() / 10000)) / 100.00 + "M");

		viewHolder.video.setVisibility(View.VISIBLE);
		viewHolder.video.setImageResource(picid);
		if (picid == R.drawable.folder) {
			viewHolder.filetime
					.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(new Date(file.lastModified())));
		}

		return convertView;
	}

	static class ViewHolder {
		public TextView filename;
		public TextView filetime;
		// public ImageView pic;
		public ImageView video;
	}

	public void remove(int position) {
		// TODO Auto-generated method stub
		filearray.remove(position);
		notifyDataSetChanged();
	}
}

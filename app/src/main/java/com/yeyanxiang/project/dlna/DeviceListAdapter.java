package com.yeyanxiang.project.dlna;

import java.util.List;
import org.cybergarage.upnp.Device;
import com.lidroid.xutils.BitmapUtils;
import com.yeyanxiang.project.R;
import com.yeyanxiang.util.dlna.Item;
import com.yeyanxiang.util.dlna.UpnpUtil;
import com.yeyanxiang.view.marquee.MarqueeText;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
public class DeviceListAdapter extends BaseExpandableListAdapter {

	private Context context;
	List<TreeNode> list;
	private int myPaddingLeft = 0;
	private BitmapUtils bitmapUtils;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			notifyDataSetChanged();
		};
	};

	public DeviceListAdapter(Context context, List<TreeNode> list,
			int myPaddingLeft) {
		super();
		this.context = context;
		this.list = list;
		this.myPaddingLeft = myPaddingLeft;
		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultLoadingImage(R.drawable.media);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.ic_launcher);
		bitmapUtils.configDefaultBitmapMaxSize(72, 72);
	}

	public int getMyPaddingLeft() {
		return myPaddingLeft;
	}

	public void setMyPaddingLeft(int myPaddingLeft) {
		this.myPaddingLeft = myPaddingLeft;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		try {
			return list.get(groupPosition).items.size();
		} catch (Exception e) {
			// TODO: handle exception
			return 0;
		}
	}

	@Override
	public Device getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).device;
	}

	@Override
	public Item getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition).items.get(childPosition);
	}

	public List<Item> getchildlist(int groupPosition) {
		return list.get(groupPosition).items;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.deviceslistitem, null);
			viewHolder = new ViewHolder();
			viewHolder.devicepic = (ImageView) convertView
					.findViewById(R.id.devicepic);
			viewHolder.deviceName = (MarqueeText) convertView
					.findViewById(R.id.deviceName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.devicepic.setImageResource(R.drawable.mediaserver);
		viewHolder.deviceName.setTextSize(20);
		viewHolder.deviceName.getPaint().setFakeBoldText(true);
		viewHolder.deviceName
				.setText(getGroup(groupPosition).getFriendlyName());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.deviceslistitem, null);
			viewHolder = new ViewHolder();
			viewHolder.devicepic = (ImageView) convertView
					.findViewById(R.id.devicepic);
			viewHolder.deviceName = (MarqueeText) convertView
					.findViewById(R.id.deviceName);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (getChildrenCount(groupPosition) > 0) {

			viewHolder.devicepic.setLayoutParams(new LinearLayout.LayoutParams(
					128, 72));

			Item item = getChild(groupPosition, childPosition);
			if (UpnpUtil.ismedia(item)) {
				viewHolder.devicepic.setImageResource(R.drawable.media);
				if (!TextUtils.isEmpty(item.getAlbumUri())) {
					bitmapUtils.display(viewHolder.devicepic,
							item.getAlbumUri());
				}
			} else {
				viewHolder.devicepic.setImageResource(R.drawable.fouder);
			}
			viewHolder.deviceName.setTextSize(15);
			viewHolder.deviceName.setText(item.getTitle());
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	private class ViewHolder {
		private ImageView devicepic;
		private MarqueeText deviceName;
	}

	public void clear() {
		// TODO Auto-generated method stub
		list.clear();
		notifyDataSetChanged();
	}
}

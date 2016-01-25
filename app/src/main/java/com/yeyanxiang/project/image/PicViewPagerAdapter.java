package com.yeyanxiang.project.image;

import java.util.ArrayList;
import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.img.ImageViewTouch;
import com.yeyanxiang.view.img.PagerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

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
public class PicViewPagerAdapter extends PagerAdapter {
	private List<Integer> resid;
	private List<ImageViewTouch> views;
	private Context context;
	private BitmapUtils bitmapUtils;

	public PicViewPagerAdapter(List<Integer> resid, final Context context) {
		super();
		this.resid = resid;
		this.context = context;
		views = new ArrayList<ImageViewTouch>();
		for (int i = 0; i < resid.size(); i++) {
			ImageViewTouch imageview = new ImageViewTouch(context);
			imageview.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			views.add(imageview);
		}

		bitmapUtils = new BitmapUtils(context);
		bitmapUtils.configDefaultBitmapConfig(Bitmap.Config.RGB_565);
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.icon);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return resid.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		ImageViewTouch imageView = views.get(position);
		imageView.setImageResource(resid.get(position));
		imageView.setFocusableInTouchMode(true);
		container.addView(imageView, 0);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		// super.destroyItem(container, position, object);
		container.removeView(views.get(position));
	}

	@Override
	public ImageViewTouch getImageView(int position) {
		// TODO Auto-generated method stub
		return views.get(position);
	}

	@Override
	public void startUpdate(View container) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishUpdate(View container) {
		// TODO Auto-generated method stub

	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		// TODO Auto-generated method stub

	}

}

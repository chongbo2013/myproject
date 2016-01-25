package com.yeyanxiang.project.image;

import java.util.ArrayList;
import java.util.List;
import com.yeyanxiang.project.R;
import com.yeyanxiang.view.img.ImageViewGroup;
import com.yeyanxiang.view.img.ImageViewGroup.ImgPageChangeListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
public class ImgGroupActivity extends Activity {
	private Context context;
	private ImageViewGroup imageViewGroup;
	private TextView count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.imggrouplayout);
		initview();
	}

	private void initview() {
		// TODO Auto-generated method stub
		count = (TextView) findViewById(R.id.count);
		imageViewGroup = (ImageViewGroup) findViewById(R.id.imggroup);
		List<Integer> resid = new ArrayList<Integer>();
		resid.add(R.drawable.img01);
		resid.add(R.drawable.img02);
		resid.add(R.drawable.img03);
		resid.add(R.drawable.img04);
		resid.add(R.drawable.img05);
		resid.add(R.drawable.img06);
		resid.add(R.drawable.img07);
		resid.add(R.drawable.img08);
		resid.add(R.drawable.img09);
		final int sum = resid.size();
		count.setText((1) + "/" + sum);
		imageViewGroup.setAdapter(new PicViewPagerAdapter(resid, context));
		imageViewGroup.setOnPageChangeListener(new ImgPageChangeListener() {
			@Override
			public void onPageSelected(int position, int prePosition) {
				// TODO Auto-generated method stub
				super.onPageSelected(position, prePosition);
				count.setText((position + 1) + "/" + sum);
			}
		});
	}
}
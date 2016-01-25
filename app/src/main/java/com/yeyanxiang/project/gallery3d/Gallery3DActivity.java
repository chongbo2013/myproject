package com.yeyanxiang.project.gallery3d;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.GalleryFlow3D;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

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
public class Gallery3DActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		GalleryFlow3D galleryFlow = new GalleryFlow3D(this);
		galleryFlow.setMaxRotationAngle(40);
		galleryFlow.setMaxZoom(-90);
		setContentView(galleryFlow);
		getWindow().setBackgroundDrawableResource(R.drawable.gallery3dbg);
		Integer[] images = { R.drawable.img01, R.drawable.img02,
				R.drawable.img03, R.drawable.img04, R.drawable.img05,
				R.drawable.img06, R.drawable.img07, R.drawable.img08 };

		ImageAdapter adapter = new ImageAdapter(this, images);
		adapter.createReflectedImages();// 创建倒影效果

		galleryFlow.setFadingEdgeLength(0);
		galleryFlow.setSpacing(-100); // 图片之间的间距
		galleryFlow.setAdapter(adapter);

		galleryFlow.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		galleryFlow.setSelection(firstSelect(images.length,
				galleryFlow.getCount()));
	}

	private int firstSelect(int size, int count) {
		if (size > 5) {
			int selection = ((count % 2) == 0) ? count / 2 : (count / 2) + 1;
			int y = selection % size;
			if (y != 0) {
				selection = selection + (size - y);
			}
			return selection;
		}
		return 0;
	}
}

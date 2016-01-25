package com.yeyanxiang.project.slidingmenudemo;

import android.graphics.Canvas;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.slidemenu.SlidingMenu.CanvasTransformer;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月14日
 * 
 * @简介
 */
public class CustomZoomAnimation extends CustomAnimation {

	public CustomZoomAnimation() {
		// see the class CustomAnimation for how to attach
		// the CanvasTransformer to the SlidingMenu
		super(R.string.anim_zoom, new CanvasTransformer() {
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {
				float scale = (float) (percentOpen * 0.25 + 0.75);
				canvas.scale(scale, scale, canvas.getWidth() / 2,
						canvas.getHeight() / 2);
			}
		});
	}

}

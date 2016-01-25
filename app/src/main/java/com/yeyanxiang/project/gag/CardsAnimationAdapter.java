package com.yeyanxiang.project.gag;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年6月26日
 * 
 * @简介
 */
public class CardsAnimationAdapter extends AnimationAdapter {
	private float mTranslationY = 400;

	private float mRotationX = 15;

	private long mDuration = 400;

	public CardsAnimationAdapter(BaseAdapter baseAdapter) {
		super(baseAdapter);
	}

	@Override
	protected long getAnimationDelayMillis() {
		return 30;
	}

	@Override
	protected long getAnimationDurationMillis() {
		return mDuration;
	}

	@Override
	public Animator[] getAnimators(ViewGroup parent, View view) {
		return new Animator[] {
				ObjectAnimator.ofFloat(view, "translationY", mTranslationY, 0),
				ObjectAnimator.ofFloat(view, "rotationX", mRotationX, 0) };
	}
}

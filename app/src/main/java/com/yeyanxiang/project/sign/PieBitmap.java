package com.yeyanxiang.project.sign;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import java.util.HashMap;
import java.util.Map;

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
public class PieBitmap extends View {

	private int mWidth;

	private int mHeight;

	private int mOffset = 50;

	private int mLayers = 30;

	private float mSpeed;

	private int mRadia;

	private float mStartDg;

	private RectF mRectF;

	private float[] mRates;

	private int[] mColors;

	private String[] descs;

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

	private Paint mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);

	private RectF mBflingRect;

	private RectF mTflingRect;

	private float mh;

	private float xPos;

	private float mDx;

	private float mDeceleration;

	private boolean isFinish;

	private int mMaximumVelocity;

	private static final int MAXIMUM_FLING_VELOCITY = 4000;

	private long mStartTime;

	private int mDuration;

	private int mDirection;

	private VelocityTracker mVelocityTracker;

	private boolean canTurn;

	private int len;

	private Map<Integer, Arc> mDataMap;

	public PieBitmap(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub

	}

	public PieBitmap(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mRadia = 0;
		isFinish = true;
		canTurn = false;
		float ppi = context.getResources().getDisplayMetrics().density * 160.0f;
		mDeceleration = SensorManager.GRAVITY_EARTH // g (m/s^2)
				* 39.37f // inch/meter
				* ppi // pixels per inch
				* ViewConfiguration.getScrollFriction();
		final DisplayMetrics metrics = context.getResources()
				.getDisplayMetrics();
		final float density = metrics.density;
		mMaximumVelocity = (int) (density * MAXIMUM_FLING_VELOCITY + 0.5f);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (mWidth < 5 || mHeight < 5 || mRectF == null)
			return;
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAlpha(100);

		mPaint2.setAntiAlias(true);
		mPaint2.setStyle(Paint.Style.STROKE);
		mPaint2.setStrokeWidth(1);
		if (mRadia == 0) {
			getRadia();
		}
		drawPies(canvas);

		try {
			mPaint.setTextSize(20);
			for (int j = 0; j < len; j++) {
				mPaint.setColor(Color.WHITE);
				canvas.drawText(descs[j], mWidth - 100, (j + 1) * 30, mPaint);
				mPaint.setColor(mColors[j]);
				canvas.drawRect(mWidth - 130, (j + 1) * 30 - 18, mWidth - 110,
						(j + 1) * 30 + 2, mPaint);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void drawPies(Canvas g) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mLayers; i++) {
			int color;
			float sweep = 0;
			float start = mStartDg;
			RectF oval = new RectF(mRectF.left, mRectF.top + mLayers - i,
					mRectF.left + 2 * mRadia - 2, mRectF.top + mRadia * 1.2f
							- i);
			mh = oval.bottom - oval.top;
			for (int j = 0; j < len; j++) {
				if (j >= mColors.length)
					color = Color.WHITE;
				else {
					color = mColors[j];
				}
				sweep = mDataMap.get(j).getmSw();
				mPaint.setColor(color);
				if (i == mLayers - 1 || i == 0) {
					int c = Color.rgb(Color.red(color) / 5 * 4,
							Color.green(color) / 5 * 4,
							Color.blue(color) / 5 * 4);
					mPaint2.setColor(c);
					mPaint.setColor(c);
				} else {
					mPaint2.setColor(color);
				}
				g.drawArc(oval, start, sweep, true, mPaint);
				g.drawArc(oval, start, sweep, true, mPaint2);
				mDataMap.get(j).setmSt(start);
				start = start + sweep;
				if (start > 360) {
					start -= 360;
				}
			}
		}

		if (mBflingRect == null) {
			mTflingRect = new RectF(mRectF.left, mRectF.top, mRectF.left + 2
					* mRadia - 2, mRectF.top + mh / 2);
			mBflingRect = new RectF(mRectF.left, mTflingRect.bottom,
					mRectF.left + 2 * mRadia - 2, mTflingRect.bottom + mh / 2
							+ mLayers);
		}

	}

	public void setData(float[] rates, int[] colors, String[] descs) {
		mStartDg = 0;
		// mStartDg = (float) Math.random() * 360;
		this.mRates = rates;
		this.mColors = colors;
		this.descs = descs;
		len = mRates.length > mColors.length ? mColors.length : mRates.length;
		float st = mStartDg;
		mDataMap = new HashMap<Integer, PieBitmap.Arc>();
		for (int i = 0; i < len; i++) {
			float sweep = mRates[i] * 3.6f;
			Arc arc = new Arc();
			arc.setRates(mRates[i]);
			arc.setmSt(st);
			arc.setmSw(sweep);
			mDataMap.put(i, arc);
			st = st + sweep;
			if (st > 360) {
				st -= 360;
			}
		}
		postInvalidate();
	}

	private void getRadia() {
		// TODO Auto-generated method stub
		float tempR = (mHeight - mLayers - 2 * mOffset) / 1.2f;
		if (tempR > (mWidth - 2 * mOffset) / 2) {
			mRadia = (mWidth - 2 * mOffset) / 2;
		} else {
			mRadia = (int) tempR;
		}

		float xex = mWidth / 2 - mRadia;
		float yex = mHeight / 2 - mRadia * 0.6f;

		mRectF = new RectF(mOffset, yex, 2 * (mRadia + xex) - mOffset, yex
				+ mRadia * 1.2f);
		// mRectF = new RectF(mOffset, mOffset, mOffset + 2 * mRadia, mOffset
		// + mRadia * 1.2f);
		// System.out.println(mRectF.left + "---" + mRectF.right + "---"
		// + mRectF.top + "---" + mRectF.bottom + "---");
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		this.mWidth = right - left;
		this.mHeight = bottom - top;
		getRadia();
		postInvalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mBflingRect == null || mTflingRect == null)
			return false;
		if (!canTurn)
			return false;
		float x = event.getX();
		float y = event.getY();

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_MOVE:
			if (mBflingRect.contains(x, y)) {
				mStartDg = mStartDg + (xPos - x) / 1.8f;
				if (mStartDg > 360) {
					mStartDg -= 360;
				} else if (mStartDg < 0) {
					mStartDg += 360;
				}
			} else if (mTflingRect.contains(x, y)) {
				mStartDg = mStartDg + (x - xPos) / 3.6f;
				if (mStartDg > 360) {
					mStartDg -= 360;
				} else if (mStartDg < 0) {
					mStartDg += 360;
				}
			}
			xPos = x;
			break;
		case MotionEvent.ACTION_DOWN:
			if (!isFinished()) {
				abortAnimation();
			}
			xPos = event.getX();
			mDx = xPos;
			break;
		case MotionEvent.ACTION_UP:
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
			int initialVelocity_x = (int) velocityTracker.getXVelocity();
			int initialVelocity_y = (int) velocityTracker.getYVelocity();
			if (Math.abs(initialVelocity_x) > Math.abs(initialVelocity_y)) {
				if (mBflingRect.contains(x, y)) {
					if (x > mDx) {
						fling(initialVelocity_x, initialVelocity_y, 1);
					} else {
						fling(initialVelocity_x, initialVelocity_y, 0);
					}
				}
				if (mTflingRect.contains(x, y)) {
					if (x > mDx) {
						fling(initialVelocity_x, initialVelocity_y, 0);
					} else {
						fling(initialVelocity_x, initialVelocity_y, 1);
					}
				}

			}
			break;
		}
		postInvalidate();
		return true;
	}

	public void fling(int velocityX, int velocityY, int dre) {
		this.mDirection = dre;
		mStartTime = AnimationUtils.currentAnimationTimeMillis();
		mSpeed = Math.abs(velocityX) / 200;
		isFinish = false;
		float velocity = (float) Math.hypot(velocityX, velocityY);
		mDuration = (int) (1000 * velocity / mDeceleration) / 2;
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (computeScrollOffset()) {
			postInvalidate();
		}
		super.computeScroll();
	}

	private boolean computeScrollOffset() {
		if (isFinish)
			return false;
		int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);
		if (timePassed < mDuration) {
			if (mDirection == 0) {
				mStartDg += mSpeed;
			} else {
				mStartDg -= mSpeed;
			}
			if (mStartDg > 360) {
				mStartDg -= 360;
			} else if (mStartDg < 0) {
				mStartDg += 360;
			}
		} else {
			isFinish = true;
		}
		return true;
	}

	private void abortAnimation() {
		isFinish = true;
	}

	private boolean isFinished() {
		return isFinish;
	}

	public void canTrun(boolean b) {
		this.canTurn = b;
	}

	class Arc {
		float mSt;
		float mSw;
		float rates;

		public float getmSt() {
			return mSt;
		}

		public void setmSt(float mSt) {
			this.mSt = mSt;
		}

		public float getmSw() {
			return mSw;
		}

		public void setmSw(float mSw) {
			this.mSw = mSw;
		}

		public void setRates(float rat) {
			this.rates = rat;
		}

		public float getRates() {
			return this.rates;
		}

	}

}

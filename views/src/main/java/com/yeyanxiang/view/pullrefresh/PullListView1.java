package com.yeyanxiang.view.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年3月13日
 * 
 * @简介 <p>
 *     一个可以监听ListView是否滚动到最顶部或最底部的自定义控件
 *     </p>
 *     只能监听由触摸产生的，如果是ListView本身Flying导致的，则不能监听</br> 如果加以改进，可以实现监听scroll滚动的具体位置等
 */
public class PullListView1 extends ListView {
	private int mLastY;
	private int mTopPosition;
	private int mBottomPosition;

	private void init() {
		mTopPosition = 0;
		mBottomPosition = 0;
	}

	public PullListView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	public PullListView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public PullListView1(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	private OnScrollOverListener onscrollOverListener = new OnScrollOverListener() {

		@Override
		public boolean onMotionUp(MotionEvent ev) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onMotionMove(MotionEvent ev, int delta) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onMotionDown(MotionEvent ev) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onListViewTopAndPullDown(int delta) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onListViewBottomAndPullUp(int delta) {
			// TODO Auto-generated method stub
			return false;
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int y = (int) ev.getRawY();

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = y;
			final boolean isHandled = onscrollOverListener.onMotionDown(ev);
			if (isHandled) {
				return isHandled;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			final int childCount = getChildCount();
			final int deltaY = y - mLastY;

			int itemCount = 0;

			try {
				itemCount = getAdapter().getCount() - mBottomPosition;
			} catch (Exception e) {
				// TODO: handle exception
			}

			// DLog.d("lastY=%d y=%d", mLastY, y);

			final int listPadding = getListPaddingTop();

			final int end = getHeight() - getPaddingBottom();

			final int firstVisiblePosition = getFirstVisiblePosition();

			final boolean isHandleMotionMove = onscrollOverListener
					.onMotionMove(ev, deltaY);

			if (isHandleMotionMove) {
				mLastY = y;
				return true;
			}

			int lastBottom = 0;
			int firstTop = 0;
			if (childCount > 0) {
				lastBottom = getChildAt(childCount - 1).getBottom();
				firstTop = getChildAt(0).getTop();
			}

			// DLog.d("firstVisiblePosition=%d firstTop=%d listPaddingTop=%d deltaY=%d",
			// firstVisiblePosition, firstTop, listPadding, deltaY);
			if (firstVisiblePosition <= mTopPosition && firstTop >= listPadding
					&& deltaY > 0) {
				final boolean isHandleOnListViewTopAndPullDown;
				isHandleOnListViewTopAndPullDown = onscrollOverListener
						.onListViewTopAndPullDown(deltaY);
				if (isHandleOnListViewTopAndPullDown) {
					mLastY = y;
					return true;
				}
			}

			// DLog.d("lastBottom=%d end=%d deltaY=%d", lastBottom, end,
			// deltaY);
			if (firstVisiblePosition + childCount >= itemCount
					&& lastBottom <= end + 1 && deltaY < 0) {
				final boolean isHandleOnListViewBottomAndPullDown;
				isHandleOnListViewBottomAndPullDown = onscrollOverListener
						.onListViewBottomAndPullUp(deltaY);
				if (isHandleOnListViewBottomAndPullDown) {
					mLastY = y;
					return true;
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			mLastY = y;
			final boolean isHandlerMotionUp = onscrollOverListener
					.onMotionUp(ev);
			if (isHandlerMotionUp) {
				return true;
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 可以自定义其中一个条目为头部，头部触发的事件将以这个为准，默认为第一个
	 * 
	 * @param index
	 *            正数第几个，必须在条目数范围之内
	 */
	public void setTopPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setTopPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Top position must > 0");

		mTopPosition = index;
	}

	/**
	 * 可以自定义其中一个条目为尾部，尾部触发的事件将以这个为准，默认为最后一个
	 * 
	 * @param index
	 *            倒数第几个，必须在条目数范围之内
	 */
	public void setBottomPosition(int index) {
		if (getAdapter() == null)
			throw new NullPointerException(
					"You must set adapter before setBottonPosition!");
		if (index < 0)
			throw new IllegalArgumentException("Bottom position must > 0");

		mBottomPosition = index;
	}

	/**
	 * 设置这个Listener可以监听是否到达顶端，或者是否到达低端等事件</br>
	 * 
	 * @see OnScrollOverListener
	 */
	public void setOnScrollOverListener(
			OnScrollOverListener onScrollOverListener) {
		onscrollOverListener = onScrollOverListener;
	}
}

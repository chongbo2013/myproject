package com.yeyanxiang.project.gag;

import com.yeyanxiang.view.grid.StaggeredGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;

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
public class PageStaggeredGridView extends StaggeredGridView implements
		AbsListView.OnScrollListener {
	private LoadingFooter mLoadingFooter;

	private OnLoadNextListener mLoadNextListener;

	public PageStaggeredGridView(Context context) {
		super(context);
		init();
	}

	public PageStaggeredGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public PageStaggeredGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mLoadingFooter = new LoadingFooter(getContext());
		addFooterView(mLoadingFooter.getView());
		setOnScrollListener(this);
	}

	public void setLoadNextListener(OnLoadNextListener listener) {
		mLoadNextListener = listener;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (mLoadingFooter.getState() == LoadingFooter.State.Loading
				|| mLoadingFooter.getState() == LoadingFooter.State.TheEnd) {
			return;
		}
		if (firstVisibleItem + visibleItemCount >= totalItemCount
				&& totalItemCount != 0
				&& totalItemCount != getHeaderViewsCount()
						+ getFooterViewsCount() && mLoadNextListener != null) {
			mLoadingFooter.setState(LoadingFooter.State.Loading);
			mLoadNextListener.onLoadNext();
		}
	}

	public void setState(LoadingFooter.State status) {
		mLoadingFooter.setState(status);
	}

	public void setState(LoadingFooter.State status, long delay) {
		mLoadingFooter.setState(status, delay);
	}
}

package com.yeyanxiang.project.slidingmenu;

import com.yeyanxiang.project.R;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
@SuppressLint("ValidFragment")
public class ProductionListFragment extends Fragment {
	private String url;
	private String title;

	private View view;

	@SuppressLint("ValidFragment")
	public ProductionListFragment(String url, String title) {
		super();
		this.url = url;
		this.title = title;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.productionlist, null);
		return view;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}

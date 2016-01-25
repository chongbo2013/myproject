package com.yeyanxiang.project.gag;

import com.yeyanxiang.project.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
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
public class DrawerFragment extends BaseFragment {
	private ListView mListView;

	private DrawerAdapter mAdapter;

	private GaGMainActivity mActivity;

	public DrawerFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (GaGMainActivity) getActivity();
		View contentView = inflater.inflate(R.layout.fragment_drawer, null);
		mListView = (ListView) contentView.findViewById(R.id.listView);
		mAdapter = new DrawerAdapter(mListView);
		mListView.setAdapter(mAdapter);
		mListView.setItemChecked(0, true);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mListView.setItemChecked(position, true);
				mActivity.setCategory(Category.values()[position]);
			}
		});
		return contentView;
	}
}

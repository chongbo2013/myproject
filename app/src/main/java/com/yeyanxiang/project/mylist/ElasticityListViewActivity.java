package com.yeyanxiang.project.mylist;

import java.util.ArrayList;
import java.util.List;

import com.yeyanxiang.view.elasticityview.ListView;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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
public class ElasticityListViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ListView listView = new ListView(this);
		setContentView(listView);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < 20; i++) {
			list.add(i + "");
		}
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, list));
	}

}

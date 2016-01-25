package com.yeyanxiang.project.listviewanimations;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.yeyanxiang.project.listviewanimations.MyListActivity;
import com.yeyanxiang.project.R;
import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.widget.DynamicListView;

/**
 * @author 叶雁翔
 * 
 * @Email yanxiang1120@gmail.com
 * 
 * @version 1.0
 * 
 * @update 2014年5月20日
 * 
 * @简介
 */
public class DragAndDropActivity extends MyListActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draganddrop);

		DynamicListView listView = (DynamicListView) findViewById(R.id.activity_draganddrop_listview);
		listView.setDivider(null);

		TextView headerView = new TextView(this);
		headerView.setText("HEADER");
		listView.addHeaderView(headerView);

		final ArrayAdapter<Integer> adapter = createListAdapter();
		AlphaInAnimationAdapter animAdapter = new AlphaInAnimationAdapter(
				adapter);
		animAdapter.setInitialDelayMillis(300);
		animAdapter.setAbsListView(listView);
		listView.setAdapter(animAdapter);

		Toast.makeText(this, "Long press an item to start dragging",
				Toast.LENGTH_LONG).show();
		listView.setOnItemMovedListener(new DynamicListView.OnItemMovedListener() {
			@Override
			public void onItemMoved(final int newPosition) {
				try {
					Toast.makeText(
							getApplicationContext(),
							adapter.getItem(newPosition)
									+ " moved to position " + newPosition,
							Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	}
}

package com.yeyanxiang.project.listviewanimations;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.AnimateDismissAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.OnDismissCallback;
import com.yeyanxiang.project.R;
import com.yeyanxiang.project.listviewanimations.BaseActivity;
import com.yeyanxiang.project.listviewanimations.MyListActivity;

import java.util.ArrayList;
import java.util.List;

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
public class AnimateDismissActivity extends BaseActivity {

    private List<Integer> mSelectedPositions;
    private MyListAdapter mAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animateremoval);

        mSelectedPositions = new ArrayList<Integer>();

        ListView listView = (ListView) findViewById(R.id.activity_animateremoval_listview);
        mAdapter = new MyListAdapter(MyListActivity.getItems());
        final AnimateDismissAdapter animateDismissAdapter = new AnimateDismissAdapter(mAdapter, new MyOnDismissCallback());
        animateDismissAdapter.setAbsListView(listView);
        listView.setAdapter(animateDismissAdapter);

        Button button = (Button) findViewById(R.id.activity_animateremoval_button);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                animateDismissAdapter.animateDismiss(mSelectedPositions);
                mSelectedPositions.clear();
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                CheckedTextView tv = (CheckedTextView) view;
                tv.toggle();
                if (tv.isChecked()) {
                    mSelectedPositions.add(position);
                } else {
                    mSelectedPositions.remove((Integer) position);
                }
            }
        });
    }

    private class MyOnDismissCallback implements OnDismissCallback {

        @Override
        public void onDismiss(final AbsListView listView, final int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                mAdapter.remove(position);
            }
        }
    }

    private class MyListAdapter extends ArrayAdapter<Integer> {

        public MyListAdapter(final ArrayList<Integer> items) {
            super(items);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            CheckedTextView tv = (CheckedTextView) convertView;
            if (tv == null) {
                tv = (CheckedTextView) LayoutInflater.from(AnimateDismissActivity.this).inflate(R.layout.activity_animateremoval_row, parent, false);
            }
            tv.setText(String.valueOf(getItem(position)));
            tv.setChecked(mSelectedPositions.contains(position));
            return tv;
        }
    }
}

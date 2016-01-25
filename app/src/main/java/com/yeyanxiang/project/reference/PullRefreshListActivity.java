package com.yeyanxiang.project.reference;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.pullrefresh.OnPullDownListener;
import com.yeyanxiang.view.pullrefresh.PullRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Create on 2013-5-7 上午10:26:15 </br> Copyright: Copyright(c) 2013 by 叶雁翔</br>
 * <p/>
 * 简介: 可下拉刷新的List
 *
 * @Version 1.0
 * @Author <a href="mailto:yanxiang1120@126.com">叶雁翔</a>
 */
public class PullRefreshListActivity extends Activity implements
        OnPullDownListener {
    private List<String> mStrings = new ArrayList<String>();
    private ArrayAdapter<String> mAdapter;
    private PullRefreshView pullRefreshView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylist);

		/*
         * 1.使用PullDownView 2.设置OnPullDownListener 3.从mPullDownView里面获取ListView
		 */
        pullRefreshView = (PullRefreshView) findViewById(R.id.pull_down_view);
        pullRefreshView.setOnPullDownListener(this);
        pullRefreshView.setTextColor(Color.WHITE);
        listView = pullRefreshView.getListView();

        mAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_1,
                mStrings);
        listView.setAdapter(mAdapter);

        pullRefreshView.enableAutoFetchMore(true, 2);
        // pullRefreshView.enableAutoFetchMore(false, 2);
        loadData();
    }

    private void loadData() {
        for (int i = 0; i < 15; i++) {
            mStrings.add(i + "");
        }
        mAdapter.notifyDataSetChanged();
        pullRefreshView.notifyDidLoad();
    }

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mStrings.add(0, new Date().toLocaleString());
                mAdapter.notifyDataSetChanged();
                pullRefreshView.notifyDidRefresh();
            }
        }, 1000);
    }

    @Override
    public void onMore() {
        // TODO Auto-generated method stub
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mStrings.add(new Date().toLocaleString());
                mAdapter.notifyDataSetChanged();
                pullRefreshView.notifyDidMore();
            }
        }, 1000);
    }

}

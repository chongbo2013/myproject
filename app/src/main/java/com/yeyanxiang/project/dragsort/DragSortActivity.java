package com.yeyanxiang.project.dragsort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.yeyanxiang.project.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DragSortActivity extends AppCompatActivity {

    @InjectView(android.R.id.list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drag_sort_layout);
        ButterKnife.inject(this);

        int dataSize = 100;
        List<Integer> data = new ArrayList<>(dataSize);
        for (int i = 1; i < dataSize + 1; i++) {
            data.add(i);
        }

        recyclerView.setAdapter(new SimpleDragSortAdapter(recyclerView, data));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.drag_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_layout_grid:
                item.setChecked(true);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
                break;
            case R.id.action_layout_linear:
                item.setChecked(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.action_layout_staggered:
                item.setChecked(true);
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.yeyanxiang.project.listviewanimations;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;

import com.yeyanxiang.project.R;

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
public class ItemManipulationsExamplesActivity extends ActionBarActivity {

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examples_itemmanipulations);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void onDragAndDropClicked(final View view) {
        Intent intent = new Intent(this, DragAndDropActivity.class);
        startActivity(intent);
    }

    public void onSwipeDismissClicked(final View view) {
        Intent intent = new Intent(this, SwipeDismissActivity.class);
        startActivity(intent);
    }

    public void onAnimateRemovalClicked(final View view) {
        Intent intent = new Intent(this, AnimateDismissActivity.class);
        startActivity(intent);
    }

    public void onAnimateAdditionClicked(final View view) {
        Intent intent = new Intent(this, AnimateAdditionActivity.class);
        startActivity(intent);
    }

    public void onExpandListItemAdapterClicked(final View view) {
        Intent intent = new Intent(this, ExpandableListItemActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

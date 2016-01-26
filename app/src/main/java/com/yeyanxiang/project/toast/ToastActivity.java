package com.yeyanxiang.project.toast;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.yeyanxiang.project.R;
import com.yeyanxiang.view.loadtoast.LoadToast;

public class ToastActivity extends Activity {

    // Example activity

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toast_main);

        final String text = "Sending reply...";
        final LoadToast lt = new LoadToast(this).setText(text).setTranslationY(100).show();
        final ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View v = new View(this);
        v.setBackgroundColor(Color.RED);
        root.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
        findViewById(R.id.show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt.show();
            }
        });
        findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt.error();
            }
        });
        findViewById(R.id.success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lt.success();
            }
        });
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = new View(ToastActivity.this);
                v.setBackgroundColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
                root.addView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
            }
        });
    }
}
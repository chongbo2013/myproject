package com.yeyanxiang.project.inject;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yeyanxiang.project.R;

public class PassiveProxyActivity extends PassiveProxyBaseActivity {
    private static final String TAG = "PassiveProxyActivity";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate " + savedInstanceState);
        setContentView(R.layout.inject_layout);
        textView = (TextView) findViewById(R.id.passive_text);
        textView.setText(savedInstanceState == null ? "test 2" : savedInstanceState.toString());
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop ");
    }

}

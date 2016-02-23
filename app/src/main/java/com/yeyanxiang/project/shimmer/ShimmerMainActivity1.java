package com.yeyanxiang.project.shimmer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerButton;
import com.facebook.shimmer.ShimmerTextView;
import com.yeyanxiang.project.R;

public class ShimmerMainActivity1 extends Activity {

    ShimmerTextView tv;
    ShimmerButton btn;
    Shimmer shimmer;
    Shimmer btnShimmer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shimmer_main1);

        tv = (ShimmerTextView) findViewById(R.id.shimmer_tv);
        btn = (ShimmerButton) findViewById(R.id.shimmer_btn);
    }

    public void toggleAnimation(View target) {
        if (shimmer != null && shimmer.isAnimating()) {
            shimmer.cancel();
        } else {
            shimmer = new Shimmer();
            shimmer.start(tv);
        }

        if (btnShimmer != null && btnShimmer.isAnimating()) {
            btnShimmer.cancel();
        } else {
            btnShimmer = new Shimmer();
            btnShimmer.start(btn);
        }
    }

}

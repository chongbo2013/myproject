package com.yeyanxiang.util.gitv.imageaware;

import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

public class BackgroundImageViewAware extends ImageViewAware {
    public BackgroundImageViewAware(ImageView imageView) {
        this(imageView, true);
    }

    public BackgroundImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        super(imageView, checkActualViewSize);
    }

    @Override
    protected void setImageBitmapInto(Bitmap bitmap, View view) {
        setImageDrawableInto(new BitmapDrawable(view.getResources(), bitmap), view);
    }
    @Override
    public boolean setImageDrawable(Drawable drawable) {
        setImageDrawableInto(drawable, getWrappedView());
        return true;
    }

    @Override
    public boolean setImageBitmap(Bitmap bitmap) {
        setImageBitmapInto(bitmap, getWrappedView());
        return true;
    }

    @Override
    protected void setImageDrawableInto(Drawable drawable, View view) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
        }
    }

}

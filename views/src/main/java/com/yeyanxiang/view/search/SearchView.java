package com.yeyanxiang.view.search;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.yeyanxiang.view.R;

/**
 * Created by yezi on 16-1-29.
 */
public class SearchView extends RelativeLayout {
    private View mView;
    private EditText mSearchEdit;
    private Button mSearchBtn;

    private SearchListener searchListener;

    public SearchView(Context context) {
        super(context);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.search_layout, null);
        mSearchEdit = (EditText) mView.findViewById(R.id.search_edit);
        mSearchBtn = (Button) mView.findViewById(R.id.search_btn);
        addView(mView);
    }

    public void setSearchListener(final SearchListener searchListener) {
        this.searchListener = searchListener;
        if (mSearchBtn != null) {
            mSearchBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String search = null;
                    if (mSearchEdit != null) search = mSearchEdit.getText().toString();
                    if (searchListener != null) searchListener.onSearchClick(search);
                }
            });
        }
    }

    public EditText getmSearchEdit() {
        return mSearchEdit;
    }

    public Button getmSearchBtn() {
        return mSearchBtn;
    }

    public static interface SearchListener {
        void onSearchClick(String search);
    }

}

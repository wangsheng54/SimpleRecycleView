package com.wangsheng.www.simplerecycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by WangSheng on 2017/7/6.
 */

public class SimpleRecycleview extends RecyclerView {
    private static final String TAG = "MyRecycleview";
    private SimpReAdapter mSimpReAdapter;
    private int lastVisiableItem;
    public SimpleRecycleview(Context context) {
        this(context, null);
    }
    public SimpleRecycleview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LayoutManager manager = recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE && manager.getItemCount() == lastVisiableItem + 2) {
                    if (mSimpReAdapter.getDefaultFootView() != null && mSimpReAdapter.getDefaultFootView().getMviewVisibility() != View.VISIBLE) {
                        mSimpReAdapter.getDefaultFootView().setLoadingState();
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisiableItem = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
            }
        });

    }
    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        this.mSimpReAdapter = (SimpReAdapter) adapter;
    }
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mSimpReAdapter.getDefaultPullDownView().onTouchEvent(e);
        return super.onTouchEvent(e);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }


}

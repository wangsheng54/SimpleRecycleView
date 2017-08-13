package com.wangsheng.www.simplerecycleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by WangSheng on 2017/7/5.
 */

public class DefaultFooterView extends LinearLayout {
    private View mView;

    private ProgressBar progressbar;
    private TextView tv_footerstatus;
    private Context mContext;

    public DefaultFooterView(Context context) {
        this(context, null);
        mContext = context;
    }

    public DefaultFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mView = LayoutInflater.from(context).inflate(R.layout.view_defualt_footer_view, null);
        mView.setVisibility(View.GONE);
        initView(mView);
        addView(mView);
    }

    private void initView(View view) {
        progressbar = (ProgressBar) view.findViewById(R.id.progressbar);
        tv_footerstatus = (TextView) view.findViewById(R.id.tv_footerstatus);
    }

    public void setMviewVisibility(boolean visibility) {
        mView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    public void setHaveNoDataState() {
        setMviewVisibility(true);
        progressbar.setVisibility(View.GONE);
        tv_footerstatus.setText(mContext.getResources().getString(R.string.nodata));

    }

    public void setLoadingState() {
        setMviewVisibility(true);
        progressbar.setVisibility(View.VISIBLE);
        tv_footerstatus.setText(mContext.getResources().getString(R.string.loading));
        if (mLoadMoreListener != null) {
            mLoadMoreListener.loadMore();
        }
    }

    private LoadMoreListener mLoadMoreListener;

    public interface LoadMoreListener {
        void loadMore();
    }

    public void setOnLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    public int getMviewVisibility() {
        return mView.getVisibility();
    }
}

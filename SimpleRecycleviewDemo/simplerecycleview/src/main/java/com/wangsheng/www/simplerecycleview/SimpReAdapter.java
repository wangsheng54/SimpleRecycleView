package com.wangsheng.www.simplerecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by WangSheng on 2017/7/4.
 */

public abstract class SimpReAdapter<T, VH extends SimpReAdapter.MyRVViewHolder> extends RecyclerView.Adapter<VH> implements DefaultPullDownView.RefreshListener, DefaultFooterView.LoadMoreListener {
    Context mContext;
    private static final int PullDown = 1;
    private static final int NOAMAL = 2;
    private static final int PUSHDOUP = 3;
    private static final int HEADVIEW = 4;

    private boolean haveHeader = false;

    private List<T> dataSource;

    private DefaultFooterView defaultFootView;
    private View realFootView;
    private DefaultPullDownView defaultPullDownView;
    private View realPullDownView;


    private int rid;

    public DefaultFooterView getDefaultFootView() {
        return defaultFootView;
    }

    private LayoutInflater mLayoutInflater;

    public SimpReAdapter(Context context, List<T> dataSoruce, int rid) {
        this.dataSource = dataSoruce;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
        this.rid = rid;

    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case PUSHDOUP:
                if (realFootView == null) {
                    defaultFootView = new DefaultFooterView(mContext);
                    defaultFootView.setOnLoadMoreListener(this);
                    return (VH) new PushUpHolder(defaultFootView);
                } else {
                    return (VH) new PushUpHolder(realFootView);
                }
            case PullDown:
                if (realPullDownView == null) {
                    defaultPullDownView = new DefaultPullDownView(mContext);
                    defaultPullDownView.setOnRefreshListener(this);
                    return (VH) new PushUpHolder(defaultPullDownView);
                } else {
                    return (VH) new PushUpHolder(realPullDownView);
                }
            case NOAMAL:
                return (VH) new NormalHolder(mLayoutInflater.inflate(rid, null));
            case HEADVIEW:
                break;


        }
        return null;
    }


    @Override
    public void onBindViewHolder(VH holder, int position) {
        if (holder.getItemViewType() == NOAMAL) {
            if (haveHeader) {
                onBindViewHolder(holder, position, dataSource.get(position - 2));
            } else {
                onBindViewHolder(holder, position, dataSource.get(position - 1));
            }
        }
    }

    public abstract void onBindViewHolder(VH holder, int position, T item);

    @Override
    public int getItemCount() {
        if (dataSource == null) return 0;
        if (haveHeader) {
            return dataSource.size() + 3;
        } else {
            return dataSource.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {//总数+1
            return PullDown;
        }
        if (position == getItemCount() - 1) {//总数+1
            return PUSHDOUP;
        }
        if (haveHeader) {//总数+1
            return HEADVIEW;
        }
        return NOAMAL;
    }

    @Override
    public void refresh() {
        if (mListener != null) {
            mListener.refresh();
        }

    }

    @Override
    public void loadMore() {
        if (mLoadMoreListener != null) {
            mLoadMoreListener.loadMore();
        }
    }

    class NormalHolder extends MyRVViewHolder {
        private View view;

        public NormalHolder(View itemView) {
            super(itemView);
            view = itemView;
        }


    }

    class PullDownHolder extends MyRVViewHolder {

        public PullDownHolder(View itemView) {
            super(itemView);
        }
    }

    class PushUpHolder extends MyRVViewHolder {

        public PushUpHolder(View itemView) {
            super(itemView);
        }
    }


    public class MyRVViewHolder extends RecyclerView.ViewHolder {
        public MyRVViewHolder(View itemView) {
            super(itemView);
        }

    }

    public void setPullDownStyle(int style) {
    }

    public void dsetPushUpStyle(int style) {
    }

    public DefaultPullDownView getDefaultPullDownView() {
        return defaultPullDownView;
    }

    private RefreshListener mListener;

    public void setOnRefreshListener(RefreshListener mListener) {
        this.mListener = mListener;
    }

    public interface RefreshListener {
        void refresh();
    }

    public void refreshCompleted() {
        defaultPullDownView.refreshCompleted();
    }

    private LoadMoreListener mLoadMoreListener;

    public void setOnLoadMoreListener(LoadMoreListener mListener) {
        this.mLoadMoreListener = mListener;
    }

    public interface LoadMoreListener {
        void loadMore();
    }

    public void setLoadMoreCompleted() {
        defaultFootView.setMviewVisibility(false);
    }
}

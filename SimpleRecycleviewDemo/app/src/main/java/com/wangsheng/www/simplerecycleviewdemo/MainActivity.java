package com.wangsheng.www.simplerecycleviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.wangsheng.www.simplerecycleview.SimpReAdapter;
import com.wangsheng.www.simplerecycleview.SimpleRecycleview;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SimpleRecycleview mSimpleRecycleview;
    private SimpReAdapter mSimpReAdapter;
    private List<TextEntity> mDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mDataSource = new ArrayList<>();
        adDataSource();
        mSimpleRecycleview = (SimpleRecycleview) findViewById(R.id.mSimpleRecycleview);
        mSimpleRecycleview.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mSimpReAdapter = new SimpReAdapter(this, mDataSource, R.layout.item_myitem) {

            @Override
            public void onBindViewHolder(MyRVViewHolder holder, int position, Object item) {
                ((TextView) holder.itemView.findViewById(R.id.tv_text)).setText(((TextEntity) item).getText());
            }
        };
        mSimpleRecycleview.setAdapter(mSimpReAdapter);
        mSimpReAdapter.setOnRefreshListener(new SimpReAdapter.RefreshListener() {
            @Override
            public void refresh() {
                simulateHandler.sendEmptyMessageDelayed(REFRESHCOMPLETED, 2000);
            }
        });
        mSimpReAdapter.setOnLoadMoreListener(new SimpReAdapter.LoadMoreListener() {
            @Override
            public void loadMore() {
                simulateHandler.sendEmptyMessageDelayed(LOADMORECOMPLETED, 2000);
            }
        });
//        mSimpReAdapter.notifyDataSetChanged();
    }

    private static final int REFRESHCOMPLETED = 0;
    private static final int LOADMORECOMPLETED = 1;
    private Handler simulateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REFRESHCOMPLETED:
                    mDataSource.clear();
                    adDataSource();
                    mSimpReAdapter.refreshCompleted();
                    mSimpReAdapter.notifyDataSetChanged();
                    break;
                case LOADMORECOMPLETED:
                    adDataSource();
                    mSimpReAdapter.setLoadMoreCompleted();
                    mSimpReAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    private void adDataSource() {
        for (int i = 0; i < 20; i++) {
            TextEntity entity = new TextEntity();
            entity.setText("条目" + i);
            mDataSource.add(entity);
        }
    }
}

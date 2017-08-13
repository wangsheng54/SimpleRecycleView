package com.wangsheng.www.simplerecycleview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * Created by WangSheng on 2017/7/5.
 */
public class DefaultPullDownView extends LinearLayout {
    private static final String TAG = "DefaultPullDownView";
    private View mView;
    private Context mContext;
    private TextView tv_state;
    private ImageView img_arrow;
    private ProgressBar progressbar;
    private ImageView img_refreshcompleted;
    private static final String STRINGPULLDOWNTOREFRESH = "下拉刷新";
    private static final String STRINGRELEASETOREFRESH = "松开立即刷新";
    private static final String STRINGREFRESHCOMPLETED = "刷新完成";
    private static final String STRINGREFRESHING = "正在刷新";


    private static final int STATE_IDLE = 0;
    private static final int STATE_PULLDOWN = 2;
    private static final int STATE_RELEASE = 3;
    private static final int STATE_REFRESH = 4;
    private static final int STATE_REFRESHCOMPLETE = 5;

    private int nowState = 0;

    private int standaredPaddingTop;
    private int maxPaddingTop;

    public DefaultPullDownView(Context context) {
        this(context, null);
    }

    public DefaultPullDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        standaredPaddingTop = dp2px(mContext, 50);
        maxPaddingTop = dp2px(mContext, 100);
        upPadding = -standaredPaddingTop;
        mView = LayoutInflater.from(context).inflate(R.layout.view_defualt_header_view, null);
        initView();
        addView(mView);
    }

    private void initView() {
        tv_state = (TextView) mView.findViewById(R.id.tv_state);
        img_arrow = (ImageView) mView.findViewById(R.id.img_arrow);
        progressbar = (ProgressBar) mView.findViewById(R.id.progressbar);
        img_refreshcompleted = (ImageView) mView.findViewById(R.id.img_refreshcompleted);

        setPaddingTop(-standaredPaddingTop);
        mRotateAnimation = new RotateAnimation(0, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setFillAfter(true);
        mRotateAnimation.setDuration(300);

        mRotateCounterAnimation = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateCounterAnimation.setFillAfter(true);
        mRotateCounterAnimation.setDuration(300);

    }

    public void setPaddingTop(int paddingtop) {
        if (paddingtop < -standaredPaddingTop) {
            paddingtop = -standaredPaddingTop;
        }
        mView.setPadding(0, paddingtop, 0, 0);
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources()
                .getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    private float startY;
    private RotateAnimation mRotateAnimation;
    private RotateAnimation mRotateCounterAnimation;
    private int upPadding;

    /**
     * 注意点
     * 1.高度的控制
     * 2.图标状态控制
     * <p>
     * 3.文字状态控制
     * 4.接口通知
     */
    private boolean counterclockwiseDown = false;
    private boolean clockwiseDown = true;

    private float lastYForMove;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = event.getY();

                boolean orientation = true;
                if (nowY - lastYForMove > 0) {
                    orientation = true;
                } else {
                    orientation = false;
                }
                lastYForMove = nowY;
                float distanceY = nowY - startY;
//                Log.e(TAG, "onTouchEvent: 能向下拉 " + myRecycleview.computeVerticalScrollOffset());
//                Log.e(TAG, "onTouchEvent: totall " + myRecycleview.computeVerticalScrollRange() + "extend" + myRecycleview.computeVerticalScrollExtent());
//                Log.e(TAG, "onTouchEvent: 能向上拉" + myRecycleview.canScrollVertically(1));
                if (mView.getPaddingTop() >= -standaredPaddingTop) {
                    if (mView.getPaddingTop() == -standaredPaddingTop && distanceY < 0) {
                        setPaddingTop(-standaredPaddingTop);
                    } else if ((myRecycleview.computeVerticalScrollOffset() - 135) <= 0) {
                        myRecycleview.scrollToPosition(0);

                        int finalPadding = (int) (distanceY / 3 + upPadding);
                        Log.e(TAG, "finalPadding: " + Math.abs(finalPadding) + "-----orientation" + orientation + "distanceY:" + distanceY);
                        if (nowState != STATE_REFRESH && nowState != STATE_REFRESHCOMPLETE) {
                            if (finalPadding >= 0 && orientation) {
                                if (clockwiseDown) {
                                    clockwiseDown = false;
                                    counterclockwiseDown = true;
                                    img_arrow.startAnimation(mRotateAnimation);
                                    tv_state.setText(STRINGRELEASETOREFRESH);
                                    nowState = STATE_RELEASE;
                                }
                            }
                            if (finalPadding <= 0 && !orientation) {
                                if (counterclockwiseDown) {
                                    nowState = STATE_PULLDOWN;
                                    clockwiseDown = true;
                                    counterclockwiseDown = false;
                                    img_arrow.startAnimation(mRotateCounterAnimation);
                                    tv_state.setText(STRINGPULLDOWNTOREFRESH);
                                }
                            }
                        }
                        setPaddingTop(finalPadding);
                        Log.e(TAG, "onTouchEvent: finalPadding" + finalPadding);

                    }

                }


                break;
            case MotionEvent.ACTION_UP:
                upPadding = mView.getPaddingTop();
//                Log.e(TAG, "onTouchEvent:upPadding " + upPadding);
                if (nowState == STATE_RELEASE) {

                    nowState = STATE_REFRESH;

                    img_arrow.clearAnimation();
                    img_arrow.setVisibility(View.GONE);
                    img_refreshcompleted.setVisibility(View.GONE);
                    progressbar.setVisibility(View.VISIBLE);

                    tv_state.setText(STRINGREFRESHING);

                    if (mListener != null) {
                        mListener.refresh();
                    }


                    setPaddingTop(0);
                } else if (nowState == STATE_PULLDOWN) {
                    nowState = STATE_IDLE;
                    setPaddingTop(-standaredPaddingTop);
                    refreshPullDownView();
                } else if (nowState == STATE_REFRESH) {
                    setPaddingTop(0);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return super.onTouchEvent(event);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            nowState = STATE_IDLE;
            refreshPullDownView();

        }
    };

    private void refreshPullDownView() {
        switch (nowState) {
            case STATE_IDLE:
                lastYForMove = 0;
                clockwiseDown = true;
                counterclockwiseDown = false;
                upPadding = -standaredPaddingTop;
                img_arrow.clearAnimation();
                img_arrow.setVisibility(View.VISIBLE);
                img_refreshcompleted.setVisibility(View.GONE);
                setPaddingTop(-standaredPaddingTop);
                tv_state.setText(STRINGPULLDOWNTOREFRESH);
                break;
            case STATE_PULLDOWN:
                break;
            case STATE_RELEASE:
                break;
            case STATE_REFRESH:
                break;
            case STATE_REFRESHCOMPLETE:
                upPadding = -standaredPaddingTop;
                progressbar.setVisibility(View.GONE);
                img_refreshcompleted.setVisibility(View.VISIBLE);
                tv_state.setText(STRINGREFRESHCOMPLETED);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                }, 300);
                break;
        }
    }

    private RefreshListener mListener;

    public void setOnRefreshListener(RefreshListener mListener) {
        this.mListener = mListener;
    }

    public interface RefreshListener {
        void refresh();
    }

    public void refreshCompleted() {
        nowState = STATE_REFRESHCOMPLETE;
        refreshPullDownView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private SimpleRecycleview myRecycleview;//当前的

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        myRecycleview = (SimpleRecycleview) mView.getParent().getParent();
    }
}

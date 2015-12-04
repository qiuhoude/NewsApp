package com.qiu.newsapp.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.qiu.newsapp.R;
import com.qiu.newsapp.utils.DensityUtils;
import com.qiu.newsapp.utils.Logs;

/**
 * Created by Administrator on 2015/12/4.
 */
public class PointPageIndicator extends RelativeLayout {


    private Drawable mSelectedImg;

    private Drawable mUnSelectedImg;

    private ViewPager mViewpage;

    private PagerAdapter mAdapter;

    /**
     * tab的间距
     */
    private int mTabSpace = 60;
    private int mTabWidth = 30;
    private int mTabHeight = 30;
    private View mCurrentTabView;

    public interface OnIndicatorPageChangeListener {
        void onPageSelected(int position);
    }

    private OnIndicatorPageChangeListener mOnIndicatorListener;

    public void setOnIndicatorPageChangeListener(OnIndicatorPageChangeListener listener){
        mOnIndicatorListener = listener;
    }

    /**
     * viewpage滚动监听
     */
    private ViewPager.OnPageChangeListener mPageListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int len = (int) ((mTabSpace * positionOffset) + position * mTabSpace);
            RelativeLayout.LayoutParams params = (LayoutParams) mCurrentTabView.getLayoutParams();
            params.leftMargin = len;
//            Logs.d("len "+len);
            mCurrentTabView.setLayoutParams(params);

        }

        @Override
        public void onPageSelected(int position) {
            if (mOnIndicatorListener != null)
                mOnIndicatorListener.onPageSelected(position);

        }

        // 滑动状态发生变化
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public PointPageIndicator(Context context) {
        this(context, null);
    }

    public PointPageIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointPageIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PointPageIndicator, defStyleAttr, 0);
        mTabSpace = DensityUtils.dp2px(context, ta.getDimension(R.styleable.PointPageIndicator_tab_space, mTabSpace));
        mTabHeight = DensityUtils.dp2px(context, ta.getDimension(R.styleable.PointPageIndicator_tab_height, mTabHeight));
        mTabWidth = DensityUtils.dp2px(context, ta.getDimension(R.styleable.PointPageIndicator_tab_width, mTabWidth));
//        Logs.d("mTabSpace " + mTabSpace + " mTabWidth " + mTabWidth);
        mSelectedImg = getResources().getDrawable(ta.getResourceId(R.styleable.PointPageIndicator_selectImg, R.drawable.shape_point_red));
        mUnSelectedImg = getResources().getDrawable(ta.getResourceId(R.styleable.PointPageIndicator_selectImg, R.drawable.shape_point_gray));
        ta.recycle();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //布局加载完毕
            @Override
            public void onGlobalLayout() {
//                Logs.d("我的布局加载完成");
                getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

    }

    private void initTab() {
        // 初始化未选择的图片
        int count = mAdapter.getCount();
        Logs.d("count" + count);
        for (int i = 0; i < count; i++) {
            View point = new View(getContext());
            point.setBackgroundDrawable(mUnSelectedImg);// 设置引导页默认圆点
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mTabWidth, mTabHeight);
            if (i > 0) {
                params.leftMargin = mTabSpace * i;// 设置圆点间隔
            }
            point.setLayoutParams(params);// 设置圆点的大小
            addView(point);// 将圆点添加给线性布局
        }
        //添加选择的图片
        mCurrentTabView = new View(getContext());
        mCurrentTabView.setBackgroundDrawable(mSelectedImg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mTabWidth, mTabHeight);
        params.leftMargin = mViewpage.getCurrentItem() * mTabSpace;
        mCurrentTabView.setLayoutParams(params);
        addView(mCurrentTabView);

    }

    public void setupWithViewPager(@NonNull ViewPager viewPager) {
        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalArgumentException("ViewPager does not have a PagerAdapter set");
        } else {
            if (adapter.getCount() < 1) {
                throw new IllegalArgumentException("PagerAdapter count must greater than 2");
            }
            mViewpage = viewPager;
            mAdapter = adapter;
            mViewpage.setOnPageChangeListener(mPageListener);
        }
        initTab();
    }


}

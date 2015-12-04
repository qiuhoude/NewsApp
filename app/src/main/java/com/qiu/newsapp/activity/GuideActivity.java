package com.qiu.newsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qiu.newsapp.R;
import com.qiu.newsapp.ui.PointPageIndicator;
import com.qiu.newsapp.utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideActivity extends AppCompatActivity {

    @Bind(R.id.vp_guide)
    ViewPager vpGuide;
    @Bind(R.id.btn_start)
    Button btnStart;
    @Bind(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @Bind(R.id.view_red_point)
    View viewRedPoint;
    @Bind(R.id.indicator)
    PointPageIndicator indicator;

    private PagerAdapter mAdapter;


    private static final int[] mImageIds = new int[]{R.mipmap.guide_1,
            R.mipmap.guide_2, R.mipmap.guide_3};
    private List<ImageView> mImageViewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        mImageViewList = new ArrayList<ImageView>();
        // 初始化引导页的3个页面
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView image = new ImageView(this);
            image.setBackgroundResource(mImageIds[i]);// 设置引导页背景
            mImageViewList.add(image);
        }

        vpGuide.setAdapter(mAdapter = new GuideAdapter());
        vpGuide.setCurrentItem(1);
        //设置关联
        indicator.setupWithViewPager(vpGuide);
        //设置
        indicator.setOnIndicatorPageChangeListener(new PointPageIndicator.OnIndicatorPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == mImageIds.length - 1) {// 最后一个页面
                    btnStart.setVisibility(View.VISIBLE);// 显示开始体验的按钮
                } else {
                    btnStart.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 更新sp, 表示已经展示了新手引导
                PrefUtils.setBoolean(GuideActivity.this,
                        "is_user_guide_showed", true);
                // 跳转主页面
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}

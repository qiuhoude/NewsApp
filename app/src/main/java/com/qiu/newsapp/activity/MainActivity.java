package com.qiu.newsapp.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.qiu.newsapp.R;
import com.qiu.newsapp.fragment.GovAffairsFragment;
import com.qiu.newsapp.fragment.HomeFragment;
import com.qiu.newsapp.fragment.NewsCenterFragment;
import com.qiu.newsapp.fragment.SettingFragment;
import com.qiu.newsapp.fragment.SmartServiceFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TabHost.OnTabChangeListener {


    @Bind(android.R.id.tabhost)
    FragmentTabHost tabhost;

    private DoubleClickExitHelper mDoubleClickExit;

    private String[] mTabName = {"首页", "新闻中心", "智慧服务", "政务", "设置"};
    private int[] mTabIcon = {R.drawable.btn_tab_home_selector, R.drawable.btn_tab_news_selector,
            R.drawable.btn_tab_smart_selector, R.drawable.btn_tab_gov_selector, R.drawable.btn_tab_setting_selector};
    private Class[] mTabClz = {HomeFragment.class, NewsCenterFragment.class, SmartServiceFragment.class
            , GovAffairsFragment.class, SettingFragment.class};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();

    }

    private void initView() {
        //双击退出
        mDoubleClickExit = new DoubleClickExitHelper(this);
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            tabhost.getTabWidget().setShowDividers(0);
        }
        initTabs();


    }

    private void initTabs() {
        for (int i = 0; i < mTabClz.length; i++) {
            TabHost.TabSpec tab = tabhost.newTabSpec(mTabName[i]);
            View indicator = LayoutInflater.from(getApplicationContext()).
                    inflate(R.layout.tab_indicator, null);
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            Drawable drawable = this.getResources().getDrawable(mTabIcon[i]);
            //设置图片
            title.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null,
                    null);
            title.setText(mTabName[i]);
            tab.setIndicator(indicator);

            tab.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            // 将Tab按钮添加进Tab选项卡中
            tabhost.addTab(tab, mTabClz[i], null);
        }
    }


    @Override
    public void onTabChanged(String tabId) {
        final int size = tabhost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = tabhost.getTabWidget().getChildAt(i);
            if (i == tabhost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }

    }
}

package com.ffxz.cosmetics.ui.activity;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

import com.ffxz.cosmetics.R;
import com.ffxz.cosmetics.base.BaseActivity;
import com.ffxz.cosmetics.ui.adapter.MineOrderTabAdapter;
import com.ffxz.cosmetics.ui.fragment.MineScoringDetailFrag;
import com.ffxz.cosmetics.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Suo on 2017/4/28.
 */

public class MineScoringDetailActivity extends BaseActivity {
    @BindView(R.id.mine_scoring_detial_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.title_view)
    View vLine;
    @BindView(R.id.mine_scoring_detial_rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.mine_scoring_detial_vline)
    View vLine2;


    private List<String> mTitle = new ArrayList<String>();
    private List<Fragment> mFragment = new ArrayList<Fragment>();


    private boolean visible = true;


    @Override
    protected int getContentView() {
        return R.layout.activity_mine_scoring_detail;
    }

    @Override
    protected void initView() {
        setTitleText("积分明细");
        vLine.setVisibility(View.GONE);
        mTitle.add("积分收益");
        mTitle.add("积分支出");

        for(int i =1;i<mTitle.size() +1;i++){
            LogUtils.i("我添加了"+i);
            mFragment.add(MineScoringDetailFrag.instant(i));
        }

        MineOrderTabAdapter adapter = new MineOrderTabAdapter(getSupportFragmentManager(), mTitle, mFragment);
        mViewpager.setAdapter(adapter);
        //为TabLayout设置ViewPager
        tabLayout.setupWithViewPager(mViewpager);
        //使用ViewPager的适配器
        //忘了这句干啥的了. 如果使用过程中有问题.应该就是这句导致的.
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    protected void initData() {
        showShadow();
    }

    private void showShadow(){
        if(Build.VERSION.SDK_INT >= 21){
            rlTitle.setElevation(getResources().getDimension(R.dimen.dis2));
            rlTitle.setOutlineProvider(ViewOutlineProvider.BOUNDS);
            vLine2.setVisibility(View.GONE);
        }
    }
}

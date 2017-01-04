package com.ping.pingword.module.translate.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ping.pingword.R;
import com.ping.pingword.base.BaseFragment;
import com.ping.pingword.module.translate.adapter.TranslatePagerAdapter;


public class Fragment_Translate extends BaseFragment {
    private View contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateViewProxy(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_translate, container, false);
            initViews(contentView);
        }
        return contentView;
    }

    private void initViews(View view) {
        setToolbarVisible(View.GONE);
        TranslatePagerAdapter adapter = new TranslatePagerAdapter(getChildFragmentManager());
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        SmartTabLayout viewPagerTab = (SmartTabLayout) view.findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

}

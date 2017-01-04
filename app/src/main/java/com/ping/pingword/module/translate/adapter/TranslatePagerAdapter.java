package com.ping.pingword.module.translate.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ping.pingword.module.translate.fragment.Fragment_Ciku;
import com.ping.pingword.module.translate.fragment.Fragment_fanyi;

public class TranslatePagerAdapter extends FragmentPagerAdapter {
    private String[] title = {"词库搜索", "在线翻译"};

    public TranslatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return Fragment_Ciku.newInstance();
        } else {
            return Fragment_fanyi.newInstance();
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

    @Override
    public int getCount() {
        return title.length;
    }
}
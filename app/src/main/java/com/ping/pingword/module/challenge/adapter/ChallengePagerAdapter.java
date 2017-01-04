package com.ping.pingword.module.challenge.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ping.pingword.bean.Word;
import com.ping.pingword.module.challenge.fragment.Fragment_challengeDetail;

import java.util.List;

public class ChallengePagerAdapter extends FragmentPagerAdapter {
    private List<Word> list;
    private int guanka;

    public ChallengePagerAdapter(FragmentManager fm, List<Word> list,int guanka) {
        super(fm);
        this.list = list;
        this.guanka = guanka;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = Fragment_challengeDetail.newInstance(position,list.size(), list.get(position),guanka);
        return frag;
    }

    @Override
    public int getCount() {
        return list.size();
    }
}

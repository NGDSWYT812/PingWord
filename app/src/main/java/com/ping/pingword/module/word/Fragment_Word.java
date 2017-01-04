package com.ping.pingword.module.word;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ping.pingword.R;
import com.ping.pingword.base.BaseFragment;
import com.ping.pingword.module.word.activity.WordActivity;
import com.ping.pingword.utils.ToolBarUtils;


public class Fragment_Word extends BaseFragment implements View.OnClickListener {
    private View contentView;
    private ImageView im_base;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateViewProxy(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_word, container, false);
            initViews(contentView);
        }
        return contentView;
    }

    private void initViews(View view) {
        ToolBarUtils.getSettor(getToolbar()).setTitle("词库");
        im_base = (ImageView) view.findViewById(R.id.im_base);
        im_base.setOnClickListener(this);
        view.findViewById(R.id.im_ct4).setOnClickListener(this);
        view.findViewById(R.id.im_ct6).setOnClickListener(this);
        view.findViewById(R.id.im_ct8).setOnClickListener(this);
        view.findViewById(R.id.im_tuofu).setOnClickListener(this);
        view.findViewById(R.id.im_yasi).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final Intent in = new Intent(getActivity(), WordActivity.class);
        switch (view.getId()) {
            case R.id.im_base:
                in.putExtra("title", "基础词库");
                in.putExtra("type", "narmol");
                break;
            case R.id.im_ct4:
                in.putExtra("title", "英语四级");
                in.putExtra("type", "ct4");
                break;
            case R.id.im_ct6:
                in.putExtra("title", "英语六级");
                in.putExtra("type", "ct6");
                break;
            case R.id.im_ct8:
                in.putExtra("title", "英语八级");
                in.putExtra("type", "ct8");
                break;
            case R.id.im_tuofu:
                in.putExtra("title", "托福英语");
                in.putExtra("type", "tuofu");
                break;
            case R.id.im_yasi:
                in.putExtra("title", "雅思英语");
                in.putExtra("type", "ielts");
                break;
        }
        im_base.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(in);
            }
        },100);
    }
}

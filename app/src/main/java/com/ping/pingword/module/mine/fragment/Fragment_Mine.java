package com.ping.pingword.module.mine.fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ping.pingword.R;
import com.ping.pingword.base.BaseFragment;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.mine.activity.AboutWebActivity;
import com.ping.pingword.module.mine.activity.ErrorWordActivity;
import com.ping.pingword.module.mine.activity.NewWordActivity;
import com.ping.pingword.utils.MyEventBus;
import com.ping.pingword.widgets.CommonDialog;
import com.qq.e.ads.appwall.APPWall;

import org.simple.eventbus.EventBus;

import ping.Lib.Utils.ResourceHelper;
import ping.Lib.widgets.pullzoomview.PullToZoomScrollViewEx;


public class Fragment_Mine extends BaseFragment implements View.OnClickListener, CommonDialog.DialogListener {
    private View contentView;
    private CommonDialog commonDialog;
    private int type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateViewProxy(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_mine, container, false);
            initViews(contentView);
        }
        return contentView;
    }

    private void initViews(View view) {
        setToolbarVisible(View.GONE);
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) view.findViewById(R.id.scroll_view);
        scrollView.setParallax(false);
        View headView = ResourceHelper.loadLayout(getActivity(), R.layout.fragment_mine_head_view);
        View zoomView = ResourceHelper.loadLayout(getActivity(), R.layout.fragment_mine_zoom_view);
        View contentView = ResourceHelper.loadLayout(getActivity(), R.layout.fragment_mine_content_view);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        contentView.findViewById(R.id.tv_logout).setOnClickListener(this);
        contentView.findViewById(R.id.rl_rj).setOnClickListener(this);
        contentView.findViewById(R.id.rl_tongji).setOnClickListener(this);
        contentView.findViewById(R.id.reset).setOnClickListener(this);
        contentView.findViewById(R.id.rl_shenci).setOnClickListener(this);
        contentView.findViewById(R.id.rl_upadte).setOnClickListener(this);
        contentView.findViewById(R.id.rl_about).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:
                type = 1;
                dialog("是否退出应用");
                break;
            case R.id.rl_rj:
                APPWall wall = new APPWall(getActivity(), "1105438309", "9070617557949724");
                wall.setScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                wall.doShowAppWall();
                break;
            case R.id.reset:
                type = 2;
                dialog("是否重置闯关");
                break;
            case R.id.rl_tongji:
                startActivity(new Intent(getActivity(), ErrorWordActivity.class));
                break;
            case R.id.rl_about:
                startActivity(new Intent(getActivity(), AboutWebActivity.class));
                break;
            case R.id.rl_shenci:
                startActivity(new Intent(getActivity(), NewWordActivity.class));
                break;
        }
    }

    private void dialog(String msg) {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(getActivity(),null);
            commonDialog.setDialogListener(this);
        }
        commonDialog.setMsg(msg);
        commonDialog.show();
    }

    @Override
    public void OnOkListener() {
        if (type == 1) {
            getActivity().finish();
            System.exit(0);
        } else {
            DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getChallengeDao().deleteAll();
            EventBus.getDefault().post(new MyEventBus(), "tag_notify");
        }
    }
}

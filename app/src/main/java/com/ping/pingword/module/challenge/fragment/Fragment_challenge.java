package com.ping.pingword.module.challenge.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.base.BaseFragment;
import com.ping.pingword.bean.Challenge;
import com.ping.pingword.dao.ChallengeDao;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.challenge.activity.ChallengeActivity;
import com.ping.pingword.module.challenge.adapter.ChallengeGridListAdapter;
import com.ping.pingword.utils.MyEventBus;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;

import ping.Lib.Utils.SnackbarUtil;


public class Fragment_challenge extends BaseFragment implements AdapterView.OnItemClickListener {
    private View contentView;
    private ChallengeGridListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateViewProxy(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_challenge, container, false);
            initViews(contentView);
        }
        return contentView;
    }

    private void initViews(View view) {
        setToolbarVisible(View.GONE);
        EventBus.getDefault().register(this);
        TextView tv_notification = (TextView) view.findViewById(R.id.tv_notification);
        tv_notification.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        tv_notification.setSingleLine(true);
        tv_notification.setSelected(true);
        tv_notification.setFocusable(true);
        tv_notification.setMarqueeRepeatLimit(-1);
        tv_notification.setFocusableInTouchMode(true);
        GridView gv_challenge = (GridView) view.findViewById(R.id.gv_challenge);
        adapter = new ChallengeGridListAdapter(getActivity());
        gv_challenge.setAdapter(adapter);
        gv_challenge.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == 0) {
            Intent intent = new Intent(getActivity(), ChallengeActivity.class);
            intent.putExtra("position", position + 1);
            startActivity(intent);
        } else {
            List<Challenge> list = DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getChallengeDao().queryBuilder().where(ChallengeDao.Properties.Position.eq(position - 1)).list();
            if (list.size() > 0) {
                Intent intent = new Intent(getActivity(), ChallengeActivity.class);
                intent.putExtra("position", position + 1);
                startActivity(intent);
            } else {
                SnackbarUtil.shortShow(contentView, "请您先解锁前面关卡");
            }
        }
    }

    @Subscriber(tag = "tag_notify")
    public void notify(MyEventBus events) {
        adapter.notifyDataSetChanged();
    }
}

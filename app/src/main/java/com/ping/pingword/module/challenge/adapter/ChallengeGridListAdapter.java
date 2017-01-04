package com.ping.pingword.module.challenge.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.bean.Challenge;
import com.ping.pingword.dao.ChallengeDao;
import com.ping.pingword.dbHelper.DBHelper;

import java.util.List;

public class ChallengeGridListAdapter extends BaseAdapter {
    private Context context;

    public ChallengeGridListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 21;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        TextView tv_guan, tv_good;
        ImageView iv_lock;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.gridview_item_challenge, null);
            viewHolder.tv_guan = (TextView) convertView.findViewById(R.id.tv_guan);
            viewHolder.tv_good = (TextView) convertView.findViewById(R.id.tv_good);
            viewHolder.iv_lock = (ImageView) convertView.findViewById(R.id.iv_lock);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        List<Challenge> list = DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getChallengeDao().queryBuilder().where(ChallengeDao.Properties.Position.eq(position)).list();
        viewHolder.tv_guan.setText("第" + (position + 1) + "关");
        Drawable img_top = null;
        if (list.size() > 0 && list.get(0).getRank() == 1) {
            viewHolder.tv_good.setVisibility(View.VISIBLE);
            viewHolder.iv_lock.setImageResource(R.drawable.corner_buled);
            img_top = context.getResources().getDrawable(R.drawable.normal);
            img_top.setBounds(0, 0, img_top.getMinimumWidth(), img_top.getMinimumHeight());
            viewHolder.tv_good.setCompoundDrawables(null, img_top, null, null);
            viewHolder.tv_good.setText("您答对" + list.get(0).getRightNum() + "个");
        } else if (list.size() > 0 && list.get(0).getRank() == 2) {
            viewHolder.tv_good.setVisibility(View.VISIBLE);
            viewHolder.iv_lock.setImageResource(R.drawable.corner_buled);
            img_top = context.getResources().getDrawable(R.drawable.good);
            img_top.setBounds(0, 0, img_top.getMinimumWidth(), img_top.getMinimumHeight());
            viewHolder.tv_good.setCompoundDrawables(null, img_top, null, null);
            viewHolder.tv_good.setText("您答对" + list.get(0).getRightNum() + "个");
        } else if (list.size() > 0 && list.get(0).getRank() == 3) {
            viewHolder.tv_good.setVisibility(View.VISIBLE);
            viewHolder.iv_lock.setImageResource(R.drawable.corner_buled);
            img_top = context.getResources().getDrawable(R.drawable.best);
            img_top.setBounds(0, 0, img_top.getMinimumWidth(), img_top.getMinimumHeight());
            viewHolder.tv_good.setCompoundDrawables(null, img_top, null, null);
            viewHolder.tv_good.setText("您答对" + list.get(0).getRightNum() + "个");
        } else {
            viewHolder.tv_good.setVisibility(View.GONE);
            viewHolder.iv_lock.setImageResource(R.drawable.lock);
        }
        return convertView;
    }
}

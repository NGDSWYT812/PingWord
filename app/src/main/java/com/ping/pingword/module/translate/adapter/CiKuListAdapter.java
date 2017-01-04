package com.ping.pingword.module.translate.adapter;

import android.view.View;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.base.TinyBaseAdapter;
import com.ping.pingword.base.ViewHolder;
import com.ping.pingword.bean.Word;

import java.util.List;

/**
 * 词库搜索列表
 */
public class CiKuListAdapter extends TinyBaseAdapter<Word> {
    public CiKuListAdapter(List<Word> mList) {
        super(mList, R.layout.listview_item_ciku);
    }

    @Override
    public View convert(View convertView, Word info, int position) {
        TextView tv_word = ViewHolder.get(convertView, R.id.tv_word);
        TextView tv_duying = ViewHolder.get(convertView, R.id.tv_duying);
        TextView tv_translate = ViewHolder.get(convertView, R.id.tv_translate);
        if (info != null) {
            tv_word.setText(info.getWord());
            tv_duying.setText(info.getSound());
            tv_translate.setText(info.getExplain());
        }
        return convertView;
    }
}

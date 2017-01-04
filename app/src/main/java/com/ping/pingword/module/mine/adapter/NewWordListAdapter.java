package com.ping.pingword.module.mine.adapter;

import android.view.View;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.base.TinyBaseAdapter;
import com.ping.pingword.base.ViewHolder;
import com.ping.pingword.bean.NewWord;

import java.util.List;

/**
 * 生词本列表
 */
public class NewWordListAdapter extends TinyBaseAdapter<NewWord> {
    public NewWordListAdapter(List<NewWord> mList) {
        super(mList, R.layout.listview_item_ciku);
    }

    @Override
    public View convert(View convertView, NewWord info, int position) {
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

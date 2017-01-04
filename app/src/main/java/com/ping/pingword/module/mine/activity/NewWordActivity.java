package com.ping.pingword.module.mine.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.ping.pingword.R;
import com.ping.pingword.base.BaseActivity;
import com.ping.pingword.bean.Word;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.translate.adapter.CiKuListAdapter;
import com.ping.pingword.module.word.activity.WordDetailActivity;
import com.ping.pingword.utils.ToolBarUtils;

import java.util.List;

import ping.Lib.Utils.DensityUtil;


public class NewWordActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<Word> wordlist;
    private SwipeMenuListView lv_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newword);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        wordlist = DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getWordDao().loadAll();
        lv_search.setAdapter(new CiKuListAdapter(wordlist));
        if (wordlist.size() > 0) {
            hideLayoutEmpty();
        } else {
            showLayoutEmpty(null);
            setEmptyContentText("暂无生词，真棒");
        }
    }

    private void initView() {
        ToolBarUtils.getSettor(this).setBackToolBar("生词本");
        lv_search = (SwipeMenuListView) findViewById(R.id.lv_newword);
        lv_search.setOnItemClickListener(this);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(DensityUtil.dip2px(getApplicationContext(), 90));
                deleteItem.setIcon(R.drawable.icon_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        lv_search.setMenuCreator(creator);
        lv_search.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getWordDao().deleteInTx(wordlist.get(position));
                wordlist.remove(position);
                lv_search.setAdapter(new CiKuListAdapter(wordlist));
                if (wordlist.size() > 0) {
                    hideLayoutEmpty();
                } else {
                    showLayoutEmpty(null);
                    setEmptyContentText("暂无生词，真棒");
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Word word = (Word) adapterView.getAdapter().getItem(position);
        Intent intent = new Intent(this, WordDetailActivity.class);
        intent.putExtra("isearch", true);
        intent.putExtra("word", word);
        startActivity(intent);
    }
}
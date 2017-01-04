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
import com.ping.pingword.bean.NewWord;
import com.ping.pingword.dao.NewWordDao;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.mine.adapter.NewWordListAdapter;
import com.ping.pingword.module.word.activity.WordDetailActivity;
import com.ping.pingword.utils.ChangeBeanUtils;
import com.ping.pingword.utils.ToolBarUtils;

import java.util.List;

import ping.Lib.Utils.DensityUtil;


public class ErrorWordActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<NewWord> wordlist;
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
        wordlist = DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getNewWordDao().queryBuilder().where(NewWordDao.Properties.Newtype.eq(1)).list();
        lv_search.setAdapter(new NewWordListAdapter(wordlist));
        if (wordlist.size() > 0) {
            hideLayoutEmpty();
        } else {
            showLayoutEmpty(null);
            setEmptyContentText("单词难不住我的");
        }
    }

    private void initView() {
        ToolBarUtils.getSettor(this).setBackToolBar("易错单词");
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
                DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getNewWordDao().deleteInTx(wordlist.get(position));
                wordlist.remove(position);
                lv_search.setAdapter(new NewWordListAdapter(wordlist));
                if (wordlist.size() > 0) {
                    hideLayoutEmpty();
                } else {
                    showLayoutEmpty(null);
                    setEmptyContentText("单词难不住我的");
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        NewWord word = (NewWord) adapterView.getAdapter().getItem(position);
        Intent intent = new Intent(this, WordDetailActivity.class);
        intent.putExtra("isearch", true);
        intent.putExtra("word", ChangeBeanUtils.WordToNewWord(word));
        startActivity(intent);
    }
}
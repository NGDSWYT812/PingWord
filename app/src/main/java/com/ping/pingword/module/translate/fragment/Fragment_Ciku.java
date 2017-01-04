package com.ping.pingword.module.translate.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.base.BaseFragment;
import com.ping.pingword.bean.Word;
import com.ping.pingword.dao.WordDao;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.translate.adapter.CiKuListAdapter;
import com.ping.pingword.module.word.activity.WordDetailActivity;

import java.util.List;


public class Fragment_Ciku extends BaseFragment implements View.OnClickListener, TextWatcher, AdapterView.OnItemClickListener {
    private View contentView, layoutEmpty;
    private ListView lv_search;
    private EditText edit_search;
    private TextView emptycontent;

    public static Fragment_Ciku newInstance() {
        Fragment_Ciku fragment = new Fragment_Ciku();
        return fragment;
    }

    public Fragment_Ciku() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateViewProxy(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_ciku, container, false);
            initViews(contentView);
        }
        return contentView;
    }

    private void initViews(View view) {
        setToolbarVisible(View.GONE);
        layoutEmpty = view.findViewById(R.id.layoutEmpty);
        emptycontent = (TextView) view.findViewById(R.id.emptycontent);
        emptycontent.setText("想查啥单词，试试");
        lv_search = (ListView) view.findViewById(R.id.lv_search);
        edit_search = (EditText) view.findViewById(R.id.edit_search);
        view.findViewById(R.id.iv_delete).setOnClickListener(this);
        lv_search.setOnItemClickListener(this);
        edit_search.addTextChangedListener(this);
        layoutEmpty.setVisibility(View.VISIBLE);
        lv_search.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.iv_delete) {
            hideInput();
            edit_search.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String zimu = charSequence.toString().trim();
        if (zimu.length() > 0) {
            query(zimu);
        } else {
            emptycontent.setText("想查啥单词，试试");
            layoutEmpty.setVisibility(View.VISIBLE);
            lv_search.setVisibility(View.GONE);
        }
    }

    private void query(String zimu) {
        List<Word> wordlist = DBHelper.getDaoSession().getWordDao().queryBuilder().where(WordDao.Properties.Word.like("%" + zimu + "%")).list();
        if (wordlist.size() == 0) {
            emptycontent.setText("词库太渣了，试试在线翻译");
            layoutEmpty.setVisibility(View.VISIBLE);
            lv_search.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            lv_search.setVisibility(View.VISIBLE);
            lv_search.setAdapter(new CiKuListAdapter(wordlist));
        }

    }

    @Override
    public void afterTextChanged(Editable editable) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Word word = (Word) adapterView.getAdapter().getItem(position);
        Intent intent = new Intent(getActivity(), WordDetailActivity.class);
        intent.putExtra("isearch", true);
        intent.putExtra("word", word);
        startActivity(intent);
    }
}

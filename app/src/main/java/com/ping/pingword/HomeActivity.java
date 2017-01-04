package com.ping.pingword;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.ping.pingword.base.BaseActivity;
import com.ping.pingword.module.challenge.fragment.Fragment_challenge;
import com.ping.pingword.module.mine.fragment.Fragment_Mine;
import com.ping.pingword.module.translate.fragment.Fragment_Translate;
import com.ping.pingword.module.word.Fragment_Word;

import ping.Lib.Utils.SnackbarUtil;


public class HomeActivity extends BaseActivity {
    private Class mFragmentList[] = {Fragment_Word.class, Fragment_Translate.class, Fragment_challenge.class, Fragment_Mine.class};
    private int mImageList[] = {R.drawable.tab_btn_word, R.drawable.tab_btn_translate, R.drawable.tab_btn_challenge, R.drawable.tab_btn_mine};
    private String mTaglist[] = {"词库", "翻译", "闯关", "我的"};
    private FragmentTabHost fTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
    }

    private void initView() {
        setToolbarVisible(View.GONE);
        fTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        fTabHost.setup(this, getSupportFragmentManager(), R.id.rl_content);
        fTabHost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < mFragmentList.length; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = fTabHost.newTabSpec(mTaglist[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            fTabHost.addTab(tabSpec, mFragmentList[i], null);
            //设置Tab按钮的背景
            fTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
        }
    }

    private View getTabItemView(int index) {
        View view = getLayoutInflater().inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_tab);
        imageView.setImageResource(mImageList[index]);
        TextView textView = (TextView) view.findViewById(R.id.text_tab);
        textView.setText(mTaglist[index]);
        return view;
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                SnackbarUtil.shortShow(fTabHost, "再按一次退出单词呗", R.drawable.ic_app);
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

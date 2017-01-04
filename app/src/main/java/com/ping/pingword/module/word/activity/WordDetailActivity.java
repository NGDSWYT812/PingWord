package com.ping.pingword.module.word.activity;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ping.pingword.R;
import com.ping.pingword.Retrofit.RequestWord;
import com.ping.pingword.Retrofit.ResultListener;
import com.ping.pingword.Retrofit.RetrofitService;
import com.ping.pingword.base.BaseActivity;
import com.ping.pingword.bean.Word;
import com.ping.pingword.dao.WordDao;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.utils.ToolBarUtils;

import java.util.List;
import java.util.Locale;

import ping.Lib.Utils.NetUtil;
import ping.Lib.Utils.SnackbarUtil;


public class WordDetailActivity extends BaseActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    private List<Word> wordlist, NewWordList;
    private TextToSpeech speech;
    private Word word;
    private int position;
    private boolean isearch;
    private String title, type;
    private ImageView iv_add;
    private TextView tv_sound, tv_word, tv_base_translate, tv_net_translate, tv_net, tv_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worddetail);
        initData();
        initView();
    }

    private void initData() {
        speech = new TextToSpeech(this, this);
        speech.setSpeechRate(1);
        isearch = getIntent().getBooleanExtra("isearch", false);
        word = (Word) getIntent().getSerializableExtra("word");
        if (!isearch) {
            title = getIntent().getStringExtra("title");
            type = getIntent().getStringExtra("type");
            position = getIntent().getIntExtra("position", 0) + 1;
            wordlist = DBHelper.getDaoSession().getWordDao().queryBuilder().where(WordDao.Properties.Type.eq(type)).list();
        }
    }

    private void initView() {
        if (isearch) {
            ToolBarUtils.getSettor(this).setBackToolBar("单词呗");
        } else {
            ToolBarUtils.getSettor(this).setBackToolBar(title + "(" + position + "/" + wordlist.size() + ")");
        }
        tv_word = (TextView) findViewById(R.id.tv_word);
        tv_sound = (TextView) findViewById(R.id.tv_sound);
        tv_net = (TextView) findViewById(R.id.tv_net);
        tv_next = (TextView) findViewById(R.id.tv_next);
        tv_net_translate = (TextView) findViewById(R.id.tv_net_translate);
        tv_base_translate = (TextView) findViewById(R.id.tv_base_translate);
        iv_add = (ImageView) findViewById(R.id.iv_add);
        tv_next.setVisibility(isearch ? View.GONE : View.VISIBLE);
        tv_next.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        findViewById(R.id.tv_nettranslate).setOnClickListener(this);
        findViewById(R.id.iv_sound).setOnClickListener(this);
        showView();
    }

    private void showView() {
        NewWordList = DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getWordDao().queryBuilder().where(WordDao.Properties.Id.eq(word.getId())).list();
        tv_word.setText(word.getWord());
        tv_sound.setText(word.getSound().replace("#", "\n"));
        tv_base_translate.setText(word.getExplain().replace("#", "\n"));
        if (NewWordList != null && NewWordList.size() > 0) {
            iv_add.setImageResource(R.drawable.btn_selector_delete);
        } else {
            iv_add.setImageResource(R.drawable.btn_selector_add);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_nettranslate:
                translate();
                break;
            case R.id.iv_sound:
                speech.speak(word.getWord(), TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.iv_add:
                if (NewWordList != null && NewWordList.size() > 0) {
                    DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getWordDao().deleteInTx(word);
                    SnackbarUtil.shortShow(tv_sound, "已从生词本移除");
                } else {
                    DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getWordDao().insertOrReplace(word);
                    SnackbarUtil.shortShow(tv_sound, "已加入生词本");
                }
                showView();
                break;
            case R.id.tv_next:
                if (position < wordlist.size()) {
                    position = position + 1;
                    word = wordlist.get(position);
                    NewWordList.clear();
                    ToolBarUtils.getSettor(this).setBackToolBar(title + "(" + position + "/" + wordlist.size() + ")");
                    showView();
                } else {
                    SnackbarUtil.shortShow(tv_word, "亲，已经是最后一个了");
                }
                tv_net_translate.setText("");
                tv_net_translate.setVisibility(View.INVISIBLE);
                tv_net.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void translate() {
        if (NetUtil.networkEnable()) {
            RetrofitService.getInstance().getYoDaoWord(word.getWord(), resultListener);
        } else {
            SnackbarUtil.shortShow(tv_word, "网络连接异常");
        }
    }

    //网络请求结果
    private ResultListener resultListener = new ResultListener() {
        @Override
        public void onSucess(Object... result) {
            StringBuffer sb = new StringBuffer();
            RequestWord word = (RequestWord) result[0];
            int count = 0;
            for (int i = 0; i < word.getTranslation().size(); i++) {
                count++;
                sb.append("(" + count + ")、" + word.getTranslation().get(i) + "\n");
            }
            if (word.getBasic() != null && word.getBasic().getExplains() != null) {
                for (int i = 0; i < word.getBasic().getExplains().size(); i++) {
                    count++;
                    sb.append("(" + count + ")、" + word.getBasic().getExplains().get(i) + "\n");
                }
            }
            tv_net_translate.setText(sb.toString());
            tv_net_translate.setVisibility(View.VISIBLE);
            tv_net.setVisibility(View.VISIBLE);
        }

        @Override
        public void onFailre(Object... error) {
            int code = (int) error[0];
            tv_net_translate.setText("");
            tv_net_translate.setVisibility(View.INVISIBLE);
            tv_net.setVisibility(View.INVISIBLE);
            if (code == 20) {
                SnackbarUtil.shortShow(tv_word, "要翻译的文本过长");
            } else if (code == 30) {
                SnackbarUtil.shortShow(tv_word, "无法进行有效的翻译");
            } else if (code == 40) {
                SnackbarUtil.shortShow(tv_word, "不支持的该语言");
            } else if (code == 60) {
                SnackbarUtil.shortShow(tv_word, "无词典结果");
            } else {
                SnackbarUtil.shortShow(tv_word, "翻译有误");
            }
        }

        @Override
        public void onServerError(String message) {
            tv_net_translate.setText("");
            tv_net_translate.setVisibility(View.INVISIBLE);
            tv_net.setVisibility(View.INVISIBLE);
            SnackbarUtil.shortShow(tv_word, message);
        }
    };

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = speech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, "不支持指定语", Toast.LENGTH_LONG).show();
            }
        }
    }
}
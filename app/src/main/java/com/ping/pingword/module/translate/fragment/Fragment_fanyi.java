package com.ping.pingword.module.translate.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.Retrofit.RequestWord;
import com.ping.pingword.Retrofit.ResultListener;
import com.ping.pingword.Retrofit.RetrofitService;
import com.ping.pingword.base.BaseFragment;

import ping.Lib.Utils.NetUtil;
import ping.Lib.Utils.SnackbarUtil;


public class Fragment_fanyi extends BaseFragment {
    private View contentView;
    private EditText edit_write;
    private TextView tv_read;

    public static Fragment_fanyi newInstance() {
        Fragment_fanyi fragment = new Fragment_fanyi();
        return fragment;
    }

    public Fragment_fanyi() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateViewProxy(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_fanyi, container, false);
            initViews(contentView);
        }
        return contentView;
    }

    private void initViews(View view) {
        setToolbarVisible(View.GONE);
        tv_read = (TextView) view.findViewById(R.id.tv_read);
        edit_write = (EditText) view.findViewById(R.id.edit_write);
        view.findViewById(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideInput();
                edit_write.setText("");
                tv_read.setText("");
            }
        });
        view.findViewById(R.id.tv_translate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translate();
            }
        });

    }

    private void translate() {
        hideInput();
        String word = edit_write.getText().toString().trim();
        if (NetUtil.networkEnable()) {
            if (word.length() == 0) {
                SnackbarUtil.shortShow(edit_write, "请输入您要的翻译");
            } else {
                RetrofitService.getInstance().getYoDaoWord(word, resultListener);
            }
        } else {
            SnackbarUtil.shortShow(edit_write, "网络连接异常");
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
                sb.append("("+count+")、"+word.getTranslation().get(i) + "\n");
            }
            if (word.getBasic() != null && word.getBasic().getExplains() != null) {
                for (int i = 0; i < word.getBasic().getExplains().size(); i++) {
                    count++;
                    sb.append("("+count+")、"+word.getBasic().getExplains().get(i) + "\n");
                }
            }
            tv_read.setText(sb.toString());
        }

        @Override
        public void onFailre(Object... error) {
            int code = (int) error[0];
            tv_read.setText("");
            if (code == 20) {
                SnackbarUtil.shortShow(edit_write, "要翻译的文本过长");
            } else if (code == 30) {
                SnackbarUtil.shortShow(edit_write, "无法进行有效的翻译");
            } else if (code == 40) {
                SnackbarUtil.shortShow(edit_write, "不支持的该语言");
            } else if (code == 60) {
                SnackbarUtil.shortShow(edit_write, "无词典结果");
            } else {
                SnackbarUtil.shortShow(edit_write, "翻译有误");
            }
        }

        @Override
        public void onServerError(String message) {
            tv_read.setText("");
            SnackbarUtil.shortShow(edit_write, message);
        }
    };
}

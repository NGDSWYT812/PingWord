package com.ping.pingword.widgets;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ping.pingword.R;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import ping.Lib.Utils.NetUtil;
import ping.Lib.layout.RoundTextView;


public class CommonDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private RoundTextView cancel;
    private RelativeLayout pic1Layout;
    private ImageView iv_rank,iv_kong;
    private TextView tv_msg;
    private BannerView banner;

    public CommonDialog(Activity activity, BannerView banner) {
        super(activity);
        this.activity = activity;
        this.banner = banner;
        init();
    }

    public void setMsg(String msg) {
        tv_msg.setText(msg);
    }

    public void setImage(int rank) {
        iv_rank.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.GONE);
        if (rank == 0) {
            iv_rank.setImageResource(R.drawable.bad);
        } else if (rank == 1) {
            iv_rank.setImageResource(R.drawable.normal);
        } else if (rank == 2) {
            iv_rank.setImageResource(R.drawable.good);
        } else if (rank == 3) {
            iv_rank.setImageResource(R.drawable.best);
        }
    }

    public void show() {
        super.show();
    }

    public void hide() {
        super.dismiss();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        setContentView(R.layout.dialog_common);
        pic1Layout = (RelativeLayout) findViewById(R.id.pic1Layout);
        iv_rank = (ImageView) findViewById(R.id.iv_rank);
        iv_kong = (ImageView) findViewById(R.id.iv_kong);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        initAd();
        setCanceledOnTouchOutside(false);
        cancel = (RoundTextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        findViewById(R.id.ok).setOnClickListener(this);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = 0;
        lp.height = LayoutParams.WRAP_CONTENT;
        lp.width = LayoutParams.MATCH_PARENT;
    }

    private void initAd() {
        if (NetUtil.networkEnable() && banner != null) {
            pic1Layout.setVisibility(View.VISIBLE);
            banner.loadAD();
            banner.setRefresh(30);
            banner.setADListener(new AbstractBannerADListener() {
                @Override
                public void onNoAD(int i) {
                    iv_kong.setVisibility(View.VISIBLE);
                }

                @Override
                public void onADReceiv() {
                    iv_kong.setVisibility(View.GONE);
                    RelativeLayout.LayoutParams layout = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
                    banner.setLayoutParams(layout);
                    pic1Layout.addView(banner);
                }
            });
        } else {
            pic1Layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                hide();
                break;
            case R.id.ok:
                if (listener != null)
                    listener.OnOkListener();
                hide();
                break;
        }
    }

    private DialogListener listener;

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    public interface DialogListener {
        public void OnOkListener();
    }
}
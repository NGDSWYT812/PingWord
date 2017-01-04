package com.ping.pingword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.ping.pingword.bean.Word;
import com.ping.pingword.dbHelper.DBHelper;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import java.util.List;

import ping.Lib.Utils.NetUtil;

public class SplashActivity extends AppCompatActivity {
    private AlphaAnimation alphaAnimation;
    private ImageView splash;
    private FrameLayout splash_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash = (ImageView) findViewById(R.id.app);
        splash_ad = (FrameLayout) findViewById(R.id.splash_ad);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Word> words = DBHelper.getDaoSession().getWordDao().loadAll();
                if (words.size() == 0) {
                    DBHelper.copyDbFile();
                }
            }
        }).start();
        if (NetUtil.networkEnable()) {
            initAD();
        } else {
            showAlpha();
        }
    }

    private void initAD() {
        new SplashAD(this, splash_ad, "1105438309", "4050317582190001", new SplashADListener() {
            @Override
            public void onADDismissed() {
                next();
            }

            @Override
            public void onNoAD(int i) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                next();
            }

            @Override
            public void onADPresent() {
            }

            @Override
            public void onADClicked() {
            }
        });
    }


    // 默认开机动画
    private void showAlpha() {
        // 启动动画
        alphaAnimation = new AlphaAnimation(1f, 1);
        splash.setAnimation(alphaAnimation);
        alphaAnimation.setDuration(2000);
        /**
         * 处理alphaAnimation动画完成时的跳转
         */
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                next();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }
        });
    }

    private void next() {
        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        finish();
    }

    //防止用户返回键退出APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

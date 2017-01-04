package com.ping.pingword.module.challenge.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.base.BaseActivity;
import com.ping.pingword.bean.Challenge;
import com.ping.pingword.bean.Word;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.challenge.adapter.ChallengePagerAdapter;
import com.ping.pingword.utils.MyEventBus;
import com.ping.pingword.utils.ToolBarUtils;
import com.ping.pingword.widgets.CommonDialog;
import com.ping.pingword.widgets.JellyViewPager;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.BannerView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.List;


public class ChallengeActivity extends BaseActivity implements CommonDialog.DialogListener {
    public BannerView banner;
    public JellyViewPager pager;
    private List<Word> wordList;
    private int[] timeArray, rightArray;
    private TextView tv_time;
    private int position;
    public int time;
    public int rightcount;
    public Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    if (time == 4) {
                        Drawable img_left = getResources().getDrawable(R.drawable.time_limt);
                        img_left.setBounds(0, 0, img_left.getMinimumWidth(), img_left.getMinimumHeight());
                        tv_time.setCompoundDrawables(img_left, null, null, null);
                        tv_time.setTextColor(getResources().getColor(R.color.text_color_marn));
                    }
                    time--;
                    tv_time.setText(time + "S");
                    if (time == 0) {
                        handler.removeMessages(0);
                        if (rightcount >= rightArray[position - 1]) {
                            Challenge challenge = new Challenge();
                            challenge.setId((long) position - 1);
                            challenge.setPosition(position - 1);
                            challenge.setRightNum(rightcount);
                            int rank = 1;
                            if (rightcount >= rightArray[position - 1] + 2 && rightcount < rightArray[position - 1] + 4) {
                                rank = 2;
                            } else if (rightcount >= rightArray[position - 1] + 4) {
                                rank = 3;
                            }
                            challenge.setRank(rank);
                            DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getChallengeDao().insertOrReplace(challenge);
                            EventBus.getDefault().post(new MyEventBus(), "tag_notify");
                            dialog(rank, "恭喜闯关成功，您答对" + rightcount);
                        } else {
                            dialog(0, "闯关失败，不要气馁");
                        }
                    } else {
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
                case 1:
                    time = timeArray[position - 1];
                    Drawable img_left = getResources().getDrawable(R.drawable.time);
                    img_left.setBounds(0, 0, img_left.getMinimumWidth(), img_left.getMinimumHeight());
                    tv_time.setCompoundDrawables(img_left, null, null, null);
                    tv_time.setTextColor(getResources().getColor(R.color.text_color_tv1));
                    tv_time.setText(time + "S");
                    handler.sendEmptyMessage(0);
                    break;
            }
            return true;
        }
    });

    private void dialog(int rank, String msg) {
        CommonDialog commonDialog = new CommonDialog(this, banner);
        commonDialog.setDialogListener(this);
        commonDialog.setImage(rank);
        commonDialog.setMsg(msg);
        commonDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);
        initData();
        initView();
    }

    @Subscriber(tag = "tag_challenge")
    public void setRightCount(MyEventBus events) {
        rightcount = rightcount + 1;
    }

    private void initData() {
        EventBus.getDefault().register(this);
        position = getIntent().getIntExtra("position", 1);
        timeArray = getResources().getIntArray(R.array.challenge_time);
        rightArray = getResources().getIntArray(R.array.challenge_right);
        wordList = DBHelper.getDaoSession().getWordDao().queryBuilder().orderRaw("random()").limit(timeArray[position - 1] - 3).list();
        banner = new BannerView(this, ADSize.BANNER, "1105438309", "6090511572694012");
    }

    private void initView() {
        ToolBarUtils.getSettor(this).setBackToolBar("第" + position + "关   目标" + rightArray[position - 1] + "个");
        pager = (JellyViewPager) findViewById(R.id.myViewPager1);
        tv_time = (TextView) findViewById(R.id.tv_time);
//        pager.showPre();
//        pager.showNext();
        pager.setAdapter(new ChallengePagerAdapter(getSupportFragmentManager(), wordList, position - 1));
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case 1: //正在滑动
                        break;
                    case 2: //滑动结束
                        break;
                }
            }
        });
        handler.sendEmptyMessage(1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }

    @Override
    public void OnOkListener() {
        finish();
    }
}
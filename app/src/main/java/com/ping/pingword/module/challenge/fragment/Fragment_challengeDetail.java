package com.ping.pingword.module.challenge.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.base.BaseFragment;
import com.ping.pingword.bean.Challenge;
import com.ping.pingword.bean.Word;
import com.ping.pingword.dao.WordDao;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.challenge.activity.ChallengeActivity;
import com.ping.pingword.task.BackgroundWork;
import com.ping.pingword.task.Completion;
import com.ping.pingword.task.TinyTasks;
import com.ping.pingword.utils.ChangeBeanUtils;
import com.ping.pingword.utils.MyEventBus;
import com.ping.pingword.widgets.CommonDialog;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ping.Lib.Utils.SnackbarUtil;
import ping.Lib.layout.RoundTextView;

public class Fragment_challengeDetail extends BaseFragment implements View.OnClickListener, CommonDialog.DialogListener {
    private ChallengeActivity activity;
    private View contentView;
    private int[] rightArray;
    private int position, count, guanka;
    private RoundTextView rtv_word, rtv_select_a, rtv_select_b, rtv_select_c, rtv_select_d;
    private Map<Integer, RoundTextView> viewMap = new HashMap<Integer, RoundTextView>();
    private List<Word> list = new ArrayList<Word>();
    private Word word;

    public static Fragment_challengeDetail newInstance(int position, int count, Word word, int guanka) {
        Bundle bundle = new Bundle();
        Fragment_challengeDetail fragment = new Fragment_challengeDetail();
        bundle.putSerializable("word", word);
        bundle.putInt("position", position);
        bundle.putInt("guanka", guanka);
        bundle.putInt("count", count);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments() == null ? savedInstanceState : getArguments();
        if (bundle != null) {
            position = bundle.getInt("position");
            word = (Word) bundle.getSerializable("word");
            count = bundle.getInt("count");
            guanka = bundle.getInt("guanka");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("word", word);
        outState.putInt("position", position);
        outState.putInt("count", count);
        outState.putInt("guanka", guanka);
    }

    @Override
    public View onCreateViewProxy(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (contentView == null) {
            contentView = inflater.inflate(R.layout.fragment_challengedetail, container, false);
            initViews(contentView);
        }
        return contentView;
    }

    private void initViews(View contentView) {
        activity = (ChallengeActivity) getActivity();
        setToolbarVisible(View.GONE);
        TextView tv_page = (TextView) contentView.findViewById(R.id.tv_page);
        rtv_word = (RoundTextView) contentView.findViewById(R.id.rtv_word);
        rtv_select_a = (RoundTextView) contentView.findViewById(R.id.rtv_select_a);
        rtv_select_b = (RoundTextView) contentView.findViewById(R.id.rtv_select_b);
        rtv_select_c = (RoundTextView) contentView.findViewById(R.id.rtv_select_c);
        rtv_select_d = (RoundTextView) contentView.findViewById(R.id.rtv_select_d);
        viewMap.put(0, rtv_select_a);
        viewMap.put(1, rtv_select_b);
        viewMap.put(2, rtv_select_c);
        viewMap.put(3, rtv_select_d);
        findOtherWord();
        initBackgroundColor();
        tv_page.setText((position + 1) + "/" + count);
        rtv_word.setText(word.getWord());
        rtv_select_a.setOnClickListener(this);
        rtv_select_b.setOnClickListener(this);
        rtv_select_c.setOnClickListener(this);
        rtv_select_d.setOnClickListener(this);
    }

    public List<Word> getRunWords() {
        return DBHelper.getDaoSession().getWordDao().queryBuilder().where(WordDao.Properties.Id.notEq(word.getId())).orderRaw("random()").limit(3).list();
    }

    private void findOtherWord() {
        rightArray = activity.getResources().getIntArray(R.array.challenge_right);
        TinyTasks.perform(new BackgroundWork<Object>() {
            @Override
            public Object doInBackground() throws Exception {
                list.clear();
                list.add(word);
                list.addAll(getRunWords());
                return null;
            }
        }, new Completion<Object>() {
            @Override
            public void onSuccess(Object result) {
                if (word == null) {
                    return;
                }
                Collections.shuffle(list); //混乱排序
                for (int i = 0; i < list.size(); i++) {
                    switch (i) {
                        case 0:
                            rtv_select_a.setText(list.get(i).getExplain());
                            break;
                        case 1:
                            rtv_select_b.setText(list.get(i).getExplain());
                            break;
                        case 2:
                            rtv_select_c.setText(list.get(i).getExplain());
                            break;
                        case 3:
                            rtv_select_d.setText(list.get(i).getExplain());
                            break;
                    }
                }
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        setEnable(false);
        switch (view.getId()) {
            case R.id.rtv_select_a:
                CheckIsRight(0);
                break;
            case R.id.rtv_select_b:
                CheckIsRight(1);
                break;
            case R.id.rtv_select_c:
                CheckIsRight(2);
                break;
            case R.id.rtv_select_d:
                CheckIsRight(3);
                break;
        }
    }

    private void setEnable(boolean isenable) {
        rtv_select_a.setEnabled(isenable);
        rtv_select_b.setEnabled(isenable);
        rtv_select_c.setEnabled(isenable);
        rtv_select_d.setEnabled(isenable);
    }

    public void CheckIsRight(int onClickIndex) {
        if (activity.time == 0) {
            setEnable(false);
            SnackbarUtil.shortShow(rtv_select_a,"闯关已经结束");
        } else {
            Long nowId = list.get(onClickIndex).getId();
            RoundTextView view = viewMap.get(onClickIndex);
            Long rightId = word.getId();
            if (nowId == rightId) {
                EventBus.getDefault().post(new MyEventBus(), "tag_challenge");
                view.getDelegate().setBackgroundColor(Color.parseColor("#3FBD6B"));
            } else {
                view.getDelegate().setBackgroundColor(Color.parseColor("#E9634C"));
                DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getNewWordDao().insertOrReplace(ChangeBeanUtils.WordToNewWord(word, 1));
            }
            if (!activity.isFinishing() && activity.pager.hasNext()) {
                rtv_select_d.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.pager.showNext();
                    }
                }, 500);
            }
            if (position + 1 == count) {
                if (activity.rightcount >= rightArray[guanka]) {
                    Challenge challenge = new Challenge();
                    challenge.setId((long) guanka);
                    challenge.setPosition(guanka);
                    challenge.setRightNum(activity.rightcount);
                    int rank = 1;
                    if (activity.rightcount >= rightArray[guanka] + 2 && activity.rightcount < rightArray[guanka] + 4) {
                        rank = 2;
                    } else if (activity.rightcount >= rightArray[guanka] + 4) {
                        rank = 3;
                    }
                    challenge.setRank(rank);
                    DBHelper.getDaoSession(DBHelper.DATABASE_NAME1).getChallengeDao().insertOrReplace(challenge);
                    EventBus.getDefault().post(new MyEventBus(), "tag_notify");
                    dialog(rank, "恭喜闯关成功，您答对" + activity.rightcount);
                } else {
                    dialog(0, "闯关失败，不要气馁");
                }
            }
        }
    }

    private void dialog(int rank, String msg) {
        CommonDialog commonDialog = new CommonDialog(activity, activity.banner);
        commonDialog.setDialogListener(this);
        commonDialog.setImage(rank);
        commonDialog.setMsg(msg);
        commonDialog.show();
    }

    public void initBackgroundColor() {
        Iterator<Map.Entry<Integer, RoundTextView>> it = viewMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, RoundTextView> entry = it.next();
            RoundTextView value = entry.getValue();
            value.getDelegate().setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void OnOkListener() {
        if (!activity.isFinishing())
            activity.finish();
    }
}

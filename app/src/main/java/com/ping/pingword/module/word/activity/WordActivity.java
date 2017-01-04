package com.ping.pingword.module.word.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.ping.pingword.R;
import com.ping.pingword.base.BaseActivity;
import com.ping.pingword.bean.Word;
import com.ping.pingword.dao.WordDao;
import com.ping.pingword.dbHelper.DBHelper;
import com.ping.pingword.module.word.adapter.WordListAdapter;
import com.ping.pingword.utils.ToolBarUtils;

import java.util.List;
import java.util.WeakHashMap;

import se.emilsjolander.stickylistheaders.ExpandableStickyListHeadersListView;


public class WordActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private List<Word> wordlist;
    private String title, type;
    private ExpandableStickyListHeadersListView lv_search;
    WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        initData();
        initView();
    }

    private void initData() {
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        wordlist = DBHelper.getDaoSession().getWordDao().queryBuilder().where(WordDao.Properties.Type.eq(type)).list();
    }

    private void initView() {
        ToolBarUtils.getSettor(this).setBackToolBar(title);
        lv_search = (ExpandableStickyListHeadersListView) findViewById(R.id.lv_search);
        lv_search.setOnItemClickListener(this);
        lv_search.setAnimExecutor(new AnimationExecutor());
        lv_search.setAdapter(new WordListAdapter(this, wordlist));
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Word word = (Word) adapterView.getAdapter().getItem(position);
        Intent intent = new Intent(this, WordDetailActivity.class);
        intent.putExtra("isearch", false);
        intent.putExtra("title", title);
        intent.putExtra("type", type);
        intent.putExtra("word", word);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if (ExpandableStickyListHeadersListView.ANIMATION_EXPAND == animType && target.getVisibility() == View.VISIBLE) {
                return;
            }
            if (ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE == animType && target.getVisibility() != View.VISIBLE) {
                return;
            }
            if (mOriginalViewHeightPool.get(target) == null) {
                mOriginalViewHeightPool.put(target, target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();
        }
    }
}
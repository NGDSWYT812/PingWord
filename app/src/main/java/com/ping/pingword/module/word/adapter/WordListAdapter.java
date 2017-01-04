package com.ping.pingword.module.word.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ping.pingword.R;
import com.ping.pingword.bean.Word;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

public class WordListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {
    private final Context mContext;
    private List<Word> wordlist = new ArrayList<Word>();
    private int[] mSectionIndices;
    private Character[] mSectionLetters;
    private LayoutInflater mInflater;

    public WordListAdapter(Context context, List<Word> wordlist) {
        mContext = context;
        this.wordlist.clear();
        this.wordlist = wordlist;
        mInflater = LayoutInflater.from(context);
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String lastFirstChar = String.valueOf(wordlist.get(0).getWord().charAt(0));
        sectionIndices.add(0);
        for (int i = 0; i < wordlist.size(); i++) {
            if (!String.valueOf(wordlist.get(i).getWord().charAt(0)).equalsIgnoreCase(lastFirstChar)) {
                lastFirstChar = String.valueOf(wordlist.get(i).getWord().charAt(0));
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private Character[] getSectionLetters() {
        Character[] letters = new Character[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = Character.toUpperCase(wordlist.get(mSectionIndices[i]).getWord().charAt(0));
        }
        return letters;
    }

    @Override
    public int getCount() {
        return wordlist.size();
    }

    @Override
    public Object getItem(int position) {
        return wordlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_ciku, parent, false);
            holder.tv_word = (TextView) convertView.findViewById(R.id.tv_word);
            holder.tv_duying = (TextView) convertView.findViewById(R.id.tv_duying);
            holder.tv_translate = (TextView) convertView.findViewById(R.id.tv_translate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_word.setText(wordlist.get(position).getWord());
        holder.tv_duying.setText(wordlist.get(position).getSound());
        holder.tv_translate.setText(wordlist.get(position).getExplain());
        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.listview_item_header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.tv_head);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        CharSequence headerChar = wordlist.get(position).getWord().subSequence(0, 1);
        holder.text.setText(headerChar);

        return convertView;
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        return Character.toUpperCase(wordlist.get(position).getWord().charAt(0));
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView tv_word, tv_duying, tv_translate;
    }

}

package com.ping.pingword.utils;

import com.ping.pingword.bean.NewWord;
import com.ping.pingword.bean.Word;

/**
 * @author 作者: lancd
 * @version 创建时间：2015-12-24 下午4:29:22 类说明
 */
public class ChangeBeanUtils {
    public static Word WordToNewWord(NewWord newWord) {
        Word word = new Word();
        word.setId(newWord.getId());
        word.setExplain(newWord.getExplain());
        word.setSound(newWord.getSound());
        word.setType(newWord.getType());
        word.setWord(newWord.getWord());
        return word;
    }
    public static NewWord WordToNewWord(Word Word,int newType) {
        NewWord newWord = new NewWord();
        newWord.setId(Word.getId());
        newWord.setExplain(Word.getExplain());
        newWord.setSound(Word.getSound());
        newWord.setType(Word.getType());
        newWord.setWord(Word.getWord());
        newWord.setNewtype(newType);
        return newWord;
    }
}

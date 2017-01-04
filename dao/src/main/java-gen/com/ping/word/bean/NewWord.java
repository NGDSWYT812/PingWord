package com.ping.word.bean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "NEW_WORD".
 */
public class NewWord implements java.io.Serializable {

    private Long id;
    private Integer newtype;
    private String word;
    private String sound;
    private String explain;
    private String type;

    public NewWord() {
    }

    public NewWord(Long id) {
        this.id = id;
    }

    public NewWord(Long id, Integer newtype, String word, String sound, String explain, String type) {
        this.id = id;
        this.newtype = newtype;
        this.word = word;
        this.sound = sound;
        this.explain = explain;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNewtype() {
        return newtype;
    }

    public void setNewtype(Integer newtype) {
        this.newtype = newtype;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
package com.ping.dao;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class PDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.ping.word.bean");
        schema.setDefaultJavaPackageDao("com.ping.word.dao");
        addWord(schema);
        addNewWord(schema);
        addChallenge(schema);
        new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, "dao/src/main/java-gen");
    }

    public static void addWord(Schema schema){
        Entity word = schema.addEntity("Word");
        word.addIdProperty().primaryKey().autoincrement();
        word.implementsSerializable();
        word.addStringProperty("word");
        word.addStringProperty("sound");
        word.addStringProperty("explain");
        word.addStringProperty("type");
    }
    public static void addNewWord(Schema schema){
        Entity newWord = schema.addEntity("NewWord");
        newWord.addIdProperty().primaryKey();
        newWord.implementsSerializable();
        newWord.addIntProperty("newtype");
        newWord.addStringProperty("word");
        newWord.addStringProperty("sound");
        newWord.addStringProperty("explain");
        newWord.addStringProperty("type");
    }
    public static void addChallenge(Schema schema){
        Entity challenge = schema.addEntity("Challenge");
        challenge.addIdProperty().primaryKey();
        challenge.implementsSerializable();
        challenge.addIntProperty("rightNum");
        challenge.addIntProperty("rank");
        challenge.addIntProperty("position");
    }
}

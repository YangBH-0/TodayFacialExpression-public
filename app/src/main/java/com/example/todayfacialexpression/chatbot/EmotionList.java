package com.example.todayfacialexpression.chatbot;

import java.util.Arrays;
import java.util.List;

public class EmotionList {
    private List<String> angryList = Arrays.asList(new String[]{"1", "화남", "angry", "Angry", "ANGRY"});
    private List<String> disgustingList = Arrays.asList(new String[]{"2", "역겨움", "disgusting", "Disgusting", "DISGUSTING"});
    private List<String> fearfulList = Arrays.asList(new String[]{"3", "두려움", "fearful", "Fearful", "FEARFUL"});
    private List<String> happyList = Arrays.asList(new String[]{"4", "행복", "happy", "Happy", "HAPPY"});
    private List<String> naturalList = Arrays.asList(new String[]{"5", "무표정", "natural", "Natural", "NATURAL"});
    private List<String> sadList = Arrays.asList(new String[]{"6", "슬픔", "sad", "Sad", "SAD"});
    private List<String> surprisingList = Arrays.asList(new String[]{"7", "놀람", "surprising", "Surprising", "SURPRISING"});

    public List<String> getAngryList() {
        return angryList;
    }

    public List<String> getDisgustingList() {
        return disgustingList;
    }

    public List<String> getFearfulList() {
        return fearfulList;
    }

    public List<String> getHappyList() {
        return happyList;
    }

    public List<String> getNaturalList() {
        return naturalList;
    }

    public List<String> getSadList() {
        return sadList;
    }

    public List<String> getSurprisingList() {
        return surprisingList;
    }


}

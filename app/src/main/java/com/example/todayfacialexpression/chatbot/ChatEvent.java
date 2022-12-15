package com.example.todayfacialexpression.chatbot;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.util.Log;

import com.example.todayfacialexpression.DTO.chatData;
import com.example.todayfacialexpression.chatroom.chatAdapter;
import com.example.todayfacialexpression.recogemo.DetectService;
import com.example.todayfacialexpression.util.ReturnComment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ChatEvent extends Service {
    private ArrayList<chatData> dataList;
    private int mode; // 0:netural(모드선택 상태), 1: 표정 연습 상태, 2: 음악 추천 상태
    private int faceEmotion; // 101: 원하는 감정 선택 단계, 102: 표정 인식 및 감정이 맞는지 검사 단계
    private chatAdapter adapter = new chatAdapter(dataList);
    private List<String> emotionRc = Arrays.asList(new String[]{"표정 연습", "표정 인식", "표정", "표정연습", "표정인식"});
    private List<String> musicRc = Arrays.asList(new String[]{"음악", "음악 추천", "음악추천", "음악 재생", "음악재생"});
    private List<String> personalRc = Arrays.asList(new String[] {"퍼스널컬러, ㅍㅅㄴㅋㄹ, 퍼스널, 컬러"});
    private List<String> exitList = Arrays.asList(new String[]{"종료", "그만", "그만할래", "exit", "Exit", "EXIT", "그만 할래", "안해", "종료 할래", "종료할래", "다른거 할래", "다른거"});
    private List<String> stopMusic = Arrays.asList(new String[]{"정지", "멈춰", "멈춰줘", "그만 들을래", "음악 정지",
            "음악 그만", "그만 음악", "정지 음악", "음악 멈춰", "음악 멈춰줘", "멈춰 음악", "음악 꺼줘", "꺼줘", "꺼", "음악 그만들을래", "음악꺼줘", "닥쳐", "음악 그만 들을래"});
    private final IBinder binder = new ChatEvent.LocalBinder();

    private final ChatbotConfig chatbotConfig=new ChatbotConfig();


    DetectService detectService; // detectservice 객체
    boolean detectiveBound;
    ReturnComment returnComment; // 멘트 불러오는 객체
    EmotionList emotionList = new EmotionList();
    boolean bound = false; // 바인드 여부

    public chatData MessageCheck(String message) throws IOException {
        String character=chatbotConfig.getCharacter();
        if(stopMusic.contains(message)){
            returnComment.controlMusic(0);
            mode=0;
            return chatData.create(character, "표정 연습을 하고싶으시면 \"표정 연습\"을 입력해주세요.\n음악 추천을 원하시면 \"음악 추천\"을 입력해주세요.\n퍼스널컬러 추천을 원하시면 \"퍼스널컬러\"을 입력해주세요.");
        } else if(mode ==0){
            if(personalRc.contains(message)){
                mode = 3;
                return chatData.create(character, "퍼스널 컬러을 추천 해 해드리겠습니다.\n당신의 얼굴 사진을 보여주세요.");
            }
            /*
            if (emotionRc.contains(message)) {
                mode = 1;
                faceEmotion = 101;
                return chatData.create(character, "표정 연습을 시작합니다.\n원하는 표정의 번호나 단어를 입력하세요." +
                        "\n1.화남(angry)  2.역겨움(disgusting)\n3.두려움(fearful)  4.행복(happy)\n5.무표정(natural)  6.슬픔(sad)\n7.놀람(surprising)");
            } else if (musicRc.contains(message)) {
                mode = 2;
                return chatData.create(character, "음악 추천을 해드리겠습니다.\n당신의 표정을 보여주세요.");
            }
            */
        } else if(mode == 1) { // 표정 연습 중일 때
            if(exitList.contains(message)){  // 종료하고 싶을 때
                mode = 0;
                return chatData.create(character, "표정 연습을 하고싶으시면 \"표정 연습\"을 입력해주세요.\n음악 추천을 원하시면 \"음악 추천\"을 입력해주세요.");
            } else if(faceEmotion == 101){
                if(emotionList.getAngryList().contains(message)){
                    setPracticeEmotion("anger");
                    faceEmotion = 102;
                    return chatData.create(character, "1. 화남(angry)을 선택하셨어요.\n연습한 표정을 보여 주세요.");
                } else if(emotionList.getDisgustingList().contains(message)){
                    setPracticeEmotion("disgust");
                    faceEmotion = 102;
                    return chatData.create(character, "2. 역겨움(disgusting)을 선택하셨어요.\n연습한 표정을 보여 주세요.");
                } else if(emotionList.getFearfulList().contains(message)){
                    setPracticeEmotion("fearful");
                    faceEmotion = 102;
                    return chatData.create(character, "3. 두려움(fearful)을 선택하셨어요.\n연습한 표정을 보여 주세요.");
                } else if(emotionList.getHappyList().contains(message)){
                    setPracticeEmotion("happiness");
                    faceEmotion = 102;
                    return chatData.create(character, "4. 행복(happy)을 선택하셨어요.\n연습한 표정을 보여 주세요.");
                } else if(emotionList.getNaturalList().contains(message)){
                    setPracticeEmotion("natural");
                    faceEmotion = 102;
                    return chatData.create(character, "5. 무표정(natural)을 선택하셨어요.\n연습한 표정을 보여 주세요.");
                } else if(emotionList.getSadList().contains(message)){
                    setPracticeEmotion("sadness");
                    faceEmotion = 102;
                    return chatData.create(character, "6. 슬픔(sad)을 선택하셨어요.\n연습한 표정을 보여 주세요.");
                } else if(emotionList.getSurprisingList().contains(message)){
                    setPracticeEmotion("surprise");
                    faceEmotion = 102;
                    return chatData.create(character, "7. 놀람(surprising)을 선택하셨어요.\n연습한 표정을 보여 주세요.");
                } else {
                    return chatData.create(character, "잘못된 선택입니다.\n원하는 표정의 번호나 단어를 입력하세요." +
                            "\n1.화남(angry)  2.역겨움(disgusting)\n3.두려움(fearful)  4.행복(happy)\n5.무표정(natural)  6.슬픔(sad)\n7.놀람(surprising)");
                }
            } else if(faceEmotion == 102){  // 사진 받을때의 메시지 처리
                return chatData.create(character, "연습한 표정을 사진을 찍어 보여 주세요.");
            } else {
                return chatData.create(character, "잘못된 선택입니다.\n원하는 표정의 번호나 단어를 입력하세요." +
                        "\n1.화남(angry)  2.역겨움(disgusting)\n3.두려움(fearful)  4.행복(happy)\n5.무표정(natural)  6.슬픔(sad)\n7.놀람(surprising)");
            }

        } else if(mode == 2) {
            if(exitList.contains(message)){ // 종료하고 싶을 때
                mode = 0;
                return chatData.create(character, "표정 연습을 하고싶으시면 \"표정 연습\"을 입력해주세요.\n음악 추천을 원하시면 \"음악 추천\"을 입력해주세요.");
            }
            return chatData.create(character, "당신의 표정을 보고 음악을 재생해드릴게요.");
        }else if(mode ==3 ){
            return chatData.create(character, "당신의 얼굴을 보고 퍼스널 컬러 추천 해드릴게요.");
        }
        return chatData.create(character, "표정 연습을 하고싶으시면 \"표정 연습\"을 입력해주세요.\n음악 추천을 원하시면 \"음악 추천\"을 입력해주세요.");
    }

    public chatData testProcess(chatData chatData){
        if (mode==0) {
            return chatData.create(chatbotConfig.getCharacter(), "표정 연습을 하고싶으시면 \"표정 연습\"을 입력해주세요.\n음악 추천을 원하시면 \"음악 추천\"을 입력해주세요.");
        } else if(mode==1) { // 감정인식하여 TTS 제공
            if(faceEmotion == 101) { // 무슨 감정으로 할지 입력 받기
                return chatData.create(chatbotConfig.getCharacter(), "원하는 표정의 번호나 단어를 입력하세요." +
                        "\n1.화남(angry)  2.역겨움(disgusting)\n3.두려움(fearful)  4.행복(happy)\n5.무표정(natural)  6.슬픔(sad)\n7.놀람(surprising)");
            } else if(faceEmotion == 102) {     // 얼굴 인식하면서 현재 원하는 감정과 매치될 경우 감정 그대로 path 출력
                // 원한는 표정이 아니면 path에 un 붙여서 출력
                detectService.process(chatData.getBitmap());

                return null;
            }
            return chatData.create(chatbotConfig.getCharacter(), "표정을 입력하세요~");
        }else if(mode==2){ // music

            detectService.process(chatData.getBitmap());

            return null;
//            return chatData.create(chatbotConfig.getCharacter(), "음악 재생 부분");
        }
        return chatData.create(chatbotConfig.getCharacter(), "뭔가 잘못된 입력을 한거 같아요.");
    }

    public void printResult() throws IOException {
//        String emotion = detectService.toString();
        if(mode==1){
            HashMap<String, Double> temp = detectService.getEmotion();

            String idx=""; // 감정
            double max=0;

            for(String key: temp.keySet()){
                if(max<temp.get(key)){
                    idx = key;
                    max = temp.get(key);
                }
                Log.d("testhashmap",key+" ,"+ temp.get(key));
            }

            // 번들에 넣어서 챗룸 액티비티의 리시버가 받아서 chatroomactivity에서 처리하는 형식
            Bundle bundle = new Bundle();

            if(idx == "contempt"){
                idx = "disgust";
            }
            if(chatbotConfig.getPracticeEmotion() != idx){
                idx = "un" + chatbotConfig.getPracticeEmotion();
            }
            String comment = returnComment.getTTS(chatbotConfig.getCharacter(), idx);
            bundle.putSerializable("chat",chatData.create(chatbotConfig.getCharacter(), comment));
            receiver.send(1,bundle);
            Log.d("testreceive","chatbot chat send");
            System.out.println(detectService.toString());
        }
        if(mode==2){
            HashMap<String, Double> temp = detectService.getEmotion();

            String idx=""; // 감정
            double max=0;

            for(String key: temp.keySet()){
                if(max<temp.get(key)){
                    idx = key;
                    max = temp.get(key);
                }
                Log.d("testhashmap",key+" ,"+ temp.get(key));
            }

            // 번들에 넣어서 챗룸 액티비티의 리시버가 받아서 chatroomactivity에서 처리하는 형식
            Bundle bundle = new Bundle();

            if(idx == "contempt"){
                idx = "disgust";
            }

            String comment = returnComment.processMusic(idx);
            bundle.putSerializable("chat",chatData.create(chatbotConfig.getCharacter(), "당신의 감정으로 추천해드릴 곡은\n" + comment +
                    " 입니다.\n* 음악을 그만 들으시려면 \"음악 꺼줘\"라고 해보세요"));
            receiver.send(1,bundle);
        }

    }

    public void noPicture() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("chat",chatData.create(chatbotConfig.getCharacter(), "사진에 얼굴이 없어요. 얼굴 정면이 잘 나온 사진으로 다시 올려주세요."));
        receiver.send(1,bundle);
        Log.d("testreceive","chatbot chat send");
    }

    public void setCharacter(String character){
        chatbotConfig.setCharacter(character);
    }
    public void setPracticeEmotion(String emotion){
        chatbotConfig.setPracticeEmotion(emotion);
    }

    /*  code for  bind service */
    // Binder given to clients
    private ResultReceiver receiver;


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ChatEvent getService() {
            // Return this instance of LocalService so clients can call public methods
            return ChatEvent.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        chatbotConfig.setCharacter("채린");
    }

    @Override
    public IBinder onBind(Intent intent) {
        receiver = intent.getParcelableExtra("receiver");

        Intent intent1 = new Intent(this, ReturnComment.class);
        bindService(intent1, connection, Context.BIND_AUTO_CREATE);
        Intent intent2 = new Intent(this, DetectService.class);
        bindService(intent2, detectiveConnection, Context.BIND_AUTO_CREATE);
        return binder;
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
                ReturnComment.LocalBinder binder = (ReturnComment.LocalBinder) service;
                returnComment = binder.getService();
                bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    private ServiceConnection detectiveConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
                DetectService.LocalBinder binder = (DetectService.LocalBinder) service;
                detectService = binder.getService();
                detectiveBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            detectiveBound = false;
        }
    };

    @Override
    public void onDestroy() {
        if (bound) {
            unbindService(connection);
            bound = false;
        }
        if (detectiveBound) {
            unbindService(detectiveConnection);
            detectiveBound = false;
        }
    }
}


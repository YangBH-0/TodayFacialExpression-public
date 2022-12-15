package com.example.todayfacialexpression.util;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ReturnComment extends Service {
    HashMap<String, ArrayList<HashMap<String, String>>> hashswan = new HashMap<>();
    JsonLoader jsonLoader; // json에서 데이터 불러오는 서비스
    MusicService musicService; // music 재생하는 서비스
    VoiceService voiceService; // 목소리 재생하는 서비스
    boolean jbound = false; // 바운드 여부
    boolean mbound = false;
    boolean vbound = false;

    public String getTTS(String character, String emotion) throws IOException {
        String characterName=checkCharacter(character);
        String testJsonPath = "json/comment/" + characterName + ".json";
        hashswan = jsonLoader.getMentLoader(testJsonPath);
        ArrayList<HashMap<String, String>> list = hashswan.get(emotion);
        Random random = new Random();
        int randomCommentIndex = random.nextInt(list.size());
        String comment = String.valueOf(list.get(randomCommentIndex).keySet());
        String tts = String.valueOf(list.get(randomCommentIndex).values());
        System.out.println("전달될 코멘트: " + comment);
        System.out.println("전달될 tts: " + tts);
        tts = tts.substring(1);
        tts = tts.substring(0, tts.length()-1);
        voiceService.playVoice(tts);
        return comment;
    }
    public String processMusic(String emotion)throws IOException{
        String musicJsonPath="json/music/song.json";
        hashswan = jsonLoader.getMentLoader(musicJsonPath);
        ArrayList<HashMap<String, String>> list = hashswan.get(emotion);
        int rIndex = new Random().nextInt(list.size());
        String comment = String.valueOf(list.get(rIndex).keySet());
        String music = String.valueOf(list.get(rIndex).values());
        music = music.substring(1);
        music = music.substring(0, music.length()-1);
        System.out.println("전달될 music: " + music);
        musicService.controlMusic(music,true);
        return comment;
    }
    public String getCommentMusic(String character, String emotion)throws IOException{

        String musicJsonPath="json/music/song.json";
        HashMap<String,ArrayList<String>> tmp= jsonLoader.getMusicLoader(musicJsonPath);
        ArrayList<String> songList=tmp.get(emotion);
        int rIndex = new Random().nextInt(songList.size());
        String musicPath = songList.get(rIndex);
        musicService.controlMusic(musicPath,true);

        return getTTS(character,emotion);
    }
    public boolean controlMusic(String path, int bit){
        if(path!=null) {
            musicService.controlMusic(path,true);
        }else{
            return controlMusic(bit);
        }
        return true;
    }
    public boolean controlMusic(int bit){
        if(bit==0){ // bit = 0 음악 셧다운
            musicService.controlMusic("",false);
        }else if(bit==1){ // bit = 1 음악 일시정지
            return musicService.pauseMusic();
        }else if(bit == 2){ // bit = 2 음악 다시 재생
            return musicService.continueMusic();
        }
        return false;
    }
    private String checkCharacter(String character){
        if (character == "찬구") character = "changu";
        if (character == "채린") character = "chaerin";
        if (character == "교관") character = "gyogwan";
        if (character == "예린") character = "yerin";
        if (character == "주원") character = "juwon";
        if (character == "덕춘") character = "deokchun";
        if (character == "호빈") character = "hobin";
        if (character == "주하") character = "juha";
        return character;
    }

    public void playTTS(String path) {
        MediaPlayer mPlayer;
        try {
            AssetFileDescriptor descriptor = getAssets().openFd("data/" + path);
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            mPlayer.prepare();
            mPlayer.start();
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.stop();
                    mp.release();
                }
            });
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("TTS 재생 오류 [ReturnComment.java]");
        }
    }

    // 사용 예시
//    public void abc() throws IOException {
//        String[] arr = new String[2];
//        arr = getTTS("changu", "anger");
//
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println(arr[i]);
//        }
//
//    }


    /*  code for  bind service */
    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public ReturnComment getService() {
            // Return this instance of LocalService so clients can call public methods
            return ReturnComment.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        Intent intent1 = new Intent(this, JsonLoader.class);
        bindService(intent1, connection, Context.BIND_AUTO_CREATE);
        Intent intent2 = new Intent(this, MusicService.class);
        bindService(intent2, musicConnection, Context.BIND_AUTO_CREATE);
        Intent intent3 = new Intent(this, VoiceService.class);
        bindService(intent3, voiceConnection, Context.BIND_AUTO_CREATE);
        return binder;
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            JsonLoader.LocalBinder binder = (JsonLoader.LocalBinder) service;
            jsonLoader = binder.getService();
            jbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            jbound = false;
        }
    };

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicService.LocalBinder binder = (MusicService.LocalBinder) service;
            musicService = binder.getService();
            mbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mbound = false;
        }
    };
    private ServiceConnection voiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            VoiceService.LocalBinder binder = (VoiceService.LocalBinder) service;
            voiceService = binder.getService();
            vbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            vbound = false;
        }
    };

    @Override
    public void onDestroy() {
        unbindService(connection);
        unbindService(musicConnection);
        unbindService(voiceConnection);
        jbound = false;
    }
}




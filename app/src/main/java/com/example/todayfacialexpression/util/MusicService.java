package com.example.todayfacialexpression.util;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class MusicService extends Service {
    MediaPlayer mediaPlayer;
    private final String musicDataPath="data/music/";

    public void controlMusic(String path,boolean flag){
        if(flag){
            try {
                AssetFileDescriptor descriptor = getAssetManger().openFd(musicDataPath+path+".mp3");
                if(mediaPlayer!=null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer=null;
                }
                mediaPlayer= new MediaPlayer();
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            if(mediaPlayer!=null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer=null;
            }
        }
    }
    public boolean pauseMusic(){
        if(mediaPlayer!=null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                return true;
            }
        }
        return false;
    }

    public boolean continueMusic() {
        if(mediaPlayer!=null){
            mediaPlayer.start();
            return true;
        }
        return false;
    }

    private AssetManager getAssetManger(){
        return this.getResources().getAssets();
    }
    /*  code for  bind service */
    // Binder given to clients
    private final IBinder binder = new MusicService.LocalBinder();


    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public MusicService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
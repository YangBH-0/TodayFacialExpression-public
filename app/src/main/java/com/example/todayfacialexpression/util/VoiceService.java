package com.example.todayfacialexpression.util;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import java.io.IOException;

public class VoiceService extends Service {
    private final String voiceDataPath="data/comment/";
    public void playVoice(String path) throws IOException {
        AssetFileDescriptor descriptor = getAssetManger().openFd(voiceDataPath+path+".mp3");
        MediaPlayer mediaPlayer= new MediaPlayer();
        mediaPlayer.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    private AssetManager getAssetManger(){
        return this.getResources().getAssets();
    }
    /*  code for  bind service */
    // Binder given to clients
    private final IBinder binder = new VoiceService.LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public VoiceService getService() {
            // Return this instance of LocalService so clients can call public methods
            return VoiceService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
package com.example.todayfacialexpression.recogemo;

import static android.app.ProgressDialog.show;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

/*
import com.microsoft.projectoxford.emotion.EmotionServiceClient;
import com.microsoft.projectoxford.emotion.EmotionServiceRestClient;
import com.microsoft.projectoxford.emotion.contract.RecognizeResult;
import com.microsoft.projectoxford.emotion.contract.Scores;
import com.microsoft.projectoxford.emotion.rest.EmotionServiceException;*/

import com.example.todayfacialexpression.chatbot.ChatEvent;
import com.microsoft.projectoxford.face.*;
import com.microsoft.projectoxford.face.contract.*;
import com.microsoft.projectoxford.face.rest.ClientException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.Consumer;

public class DetectService extends Service {

    private final String apiEndpoint = "https://koreacentral.api.cognitive.microsoft.com/face/v1.0/";

    private final String subscriptionKey = "keyString";

    private final FaceServiceClient faceServiceClient =
            new FaceServiceRestClient(apiEndpoint,
                    subscriptionKey);


    Bitmap mBitmap;
    Disposable backgroundTask;
    HashMap<String, Double> emotion;

    private void processImage() {

        // ProgressBar mProgressBar = new ProgressBar(detectActivity.this);

        // task var
        //onPreExecute(task 시작 전 실행될 코드 여기에 작성)


        backgroundTask = Observable.fromCallable(() -> {
            //doInBackground(task에서 실행할 코드 여기에 작성)
            Face[] faces = processWithAutoFaceDetection();
            if(faces.length==0){
                throw new ArrayIndexOutOfBoundsException();
            }

            return faces;

        }).doOnError(throwable -> {
            if(throwable instanceof ArrayIndexOutOfBoundsException){
                chatEvent.noPicture();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Face[]>() {
                    @Override
                    public void accept(Face[] faces) throws Exception {
                        if (faces == null) {
                            Log.d("exception", "null error");
                            return;
                        }
                        emotion = getEmotion(faces);
                        Log.d("face", toString());
                        chatEvent.printResult();
                    }
                });
    }

    private Face[] processWithAutoFaceDetection() {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Face[] result = new Face[0];
        try {
            result = faceServiceClient.detect(inputStream, true, false, new FaceServiceClient.FaceAttributeType[]{FaceServiceClient.FaceAttributeType.Emotion});
        } catch (ClientException | IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private HashMap<String, Double> getEmotion(Face[] res) throws Exception {
        Emotion emo = res[0].faceAttributes.emotion;
        HashMap<String, Double> emotions = new HashMap<>();
        emotions.put("anger", emo.anger);
        emotions.put("happiness", emo.happiness);
        emotions.put("contempt", emo.contempt);
        emotions.put("fear", emo.fear);
        emotions.put("natural", emo.neutral);
        emotions.put("sadness", emo.sadness);
        emotions.put("surprise", emo.surprise);
        emotions.put("disgust", emo.disgust);
        return emotions;
    }

    public HashMap<String, Double> getEmotion() {
        return emotion;
    }

    public void process(Bitmap bitmap) {
        mBitmap = bitmap;
        processImage();
    }

    public String toString() {
        Iterator<String> keys = emotion.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (keys.hasNext()) {
            String key = keys.next();
            Log.d(key, key + ":" + emotion.get(key));
            sb.append(key + ":" + emotion.get(key) + " ");
        }
        return sb.toString();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
            }
            if ((e instanceof IOException)) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) { // that's likely a bug in the applicatio
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) { // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            if(e instanceof ArrayIndexOutOfBoundsException){
                chatEvent.noPicture();
                return;
            }
            Log.e("RxJava_HOOK", "Undeliverable exception received, not sure what to do" + e.getMessage());
        });

    }

    // bind code
    private ChatEvent chatEvent;
    private Boolean bound;
    private final IBinder binder = new DetectService.LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public DetectService getService() {
            // Return this instance of LocalService so clients can call public methods
            return DetectService.this;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            ChatEvent.LocalBinder binder = (ChatEvent.LocalBinder) service;
            chatEvent = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    @Override
    public void onDestroy() {
        unbindService(connection);
        bound = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Intent intent1 = new Intent(this, ChatEvent.class);
        bindService(intent1, connection, Context.BIND_AUTO_CREATE);
        return binder;
    }
}

package com.example.todayfacialexpression.util;


import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.todayfacialexpression.service.http.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Scheduler;

public class JsonLoader extends Service {
    public final static String musicPath = "../music/";

    public HashMap<String,ArrayList<String>>  getMusicLoader(String path)throws  IOException{
        HashMap<String,ArrayList<String>> result=new HashMap<>(); // <"comment","path">
        InputStream inputStream = getAssetManger().open(path);
        Charset charset = StandardCharsets.UTF_8;
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream,charset));
        Gson gson= new Gson();
        JsonObject jsonObject= gson.fromJson(reader,JsonObject.class);
        System.out.println(jsonObject);
        Map<String, ArrayList<String>> mapObj = new Gson().fromJson(
                jsonObject, new TypeToken<HashMap<String, ArrayList<String>>>() {}.getType()
        );
        for(String temp:mapObj.keySet()){
            result.put(temp, (ArrayList<String>) mapObj.get(temp));
        }
        return result;
    }

    public HashMap<String,ArrayList<HashMap<String,String>>>  getMentLoader(String path)throws  IOException{
        HashMap<String,ArrayList<HashMap<String,String>>> result=new HashMap<>(); // <"comment","path">
        AssetManager am = getAssetManger();
        InputStream inputStream = am.open(path);
        Charset charset = StandardCharsets.UTF_8;
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream,charset));
        Gson gson= new Gson();
        JsonObject jsonObject= gson.fromJson(reader,JsonObject.class);

        Map<String, ArrayList<HashMap<String,String>>> mapObj = new Gson().fromJson(
                jsonObject, new TypeToken<HashMap<String, ArrayList<HashMap<String,String>>>>() {}.getType()
        );
        for(String temp:mapObj.keySet()){
            result.put(temp, (ArrayList<HashMap<String, String>>) mapObj.get(temp));
        }
        //System.out.println(result);
        return result;
    }

    private AssetManager getAssetManger(){
        return this.getResources().getAssets();
    }

    /*  code for  bind service */
    // Binder given to clients
    private final IBinder binder = new JsonLoader.LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public JsonLoader getService() {
            // Return this instance of LocalService so clients can call public methods
            return JsonLoader.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}

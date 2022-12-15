package com.example.todayfacialexpression;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.example.todayfacialexpression.util.JsonLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    HashMap<String, ArrayList<HashMap<String,String>>> hashswan =new HashMap<>();

    public String[] test_json(String character, String emotion) throws IOException{
        String testJsonPath = "src/main/assets/json/comment/" + character + ".json";
        JsonLoader jsonLoader = new JsonLoader();
        hashswan = jsonLoader.getMentLoader(testJsonPath);
        ArrayList<HashMap<String,String>> list = hashswan.get(emotion);
        Random random = new Random();

        int randomComment = random.nextInt(list.size());
        String comment = String.valueOf(list.get(randomComment).keySet());
        String tts = String.valueOf(list.get(randomComment).values());
        System.out.println("전달될 코멘트: " + comment);
        System.out.println("전달될 tts: " + tts);
//        for(HashMap<String,String> temp: list ){
//                for(String key: temp.keySet()){
//                    System.out.println("key:"+key+", path"+temp.get(key));
//                }
//        }
        /*

        for(Map.Entry<String,ArrayList<HashMap<String,String>>> entry: hashswan.entrySet()){
            ArrayList<HashMap<String,String>> temp=entry.getValue();
            for(HashMap<String,String> tmp: temp){
                for(String key: tmp.keySet()){
                    System.out.println("key:"+key+", path"+tmp.get(key));
                }
            }
        }*/
        return new String[] {comment, tts};
    }
    @Test
    public void abc() throws IOException {
        String[] arr = new String[2];
        arr = test_json("changu", "anger");

        for(int i=0; i< arr.length; i++){
            System.out.println(arr[i]);
        }

    }
}
package com.example.todayfacialexpression.DTO;


import android.graphics.Bitmap;

import com.example.todayfacialexpression.chatroom.Alignment;

import java.io.Serializable;

public class chatData implements Serializable {
    String contents;
    String user;
    int type;
    long time;
    Bitmap bitmap;

    public static chatData create(String user, String content){
        return create(user, content, Alignment.left);
    }
    public static chatData create(String user, String content, int type){
        return new chatData(content, user, type, System.currentTimeMillis());
    }
    public static chatData createUser(String content){
        return new chatData(content, "사용자", Alignment.right, System.currentTimeMillis());
    }
    public chatData(String contents, String user, int type, long time) {
        this.contents = contents;
        this.user = user;
        this.type = type;
        this.time = time;
    }
    public chatData(){

    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}

package com.example.todayfacialexpression.DTO.http;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("code")
    public int code;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

package com.example.todayfacialexpression.DTO.http;

import com.google.gson.annotations.SerializedName;

public class SignupRequest {
    @SerializedName("input_id")
    public String inputId;

    @SerializedName("input_pw")
    public String inputPw;

    @SerializedName("nickname")
    public String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getInputId() {
        return inputId;
    }

    public String getInputPw() {
        return inputPw;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public void setInputPw(String inputPw) {
        this.inputPw = inputPw;
    }

    public SignupRequest(String inputId, String inputPw, String nickname) {
        this.inputId = inputId;
        this.inputPw = inputPw;
        this.nickname = nickname;
    }

    public SignupRequest() {
        this.inputId="";
        this.inputPw="";
        this.nickname="";
    }
}

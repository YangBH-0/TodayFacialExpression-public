package com.example.todayfacialexpression.service.http;

import com.example.todayfacialexpression.DTO.http.LoginRequest;
import com.example.todayfacialexpression.DTO.http.LoginResponse;
import com.example.todayfacialexpression.DTO.http.SignupRequest;
import com.example.todayfacialexpression.DTO.http.SignupResponse;

import java.io.Serializable;
import java.sql.SQLException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class RetrofitClient  {
    private static RetrofitClient instance = null;
    private static initAPI initApi;

    private static String ip="server_IP";
    private static int port =80; // server_port
    //사용하고 있는 서버 BASE 주소
    private static String baseUrl = "http://"+ip+":"+port;



    private RetrofitClient() {
        //로그를 보기 위한 Interceptor

        //retrofit 설정
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        initApi = retrofit.create(initAPI.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public static initAPI getRetrofitInterface() {
        return initApi;
    }
    interface initAPI{
        //@통신 방식("통신 API명")
        @POST("/app/login")
        Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest)throws SQLException,Exception;
        @POST("/app/signup")
        Call<SignupResponse> getSignupResponse(@Body SignupRequest signupRequest)throws SQLException,Exception;
        @POST("/app/checkID")
        Call<SignupResponse> getCheckID(@Body SignupRequest signupRequest)throws SQLException,Exception;
    }

}



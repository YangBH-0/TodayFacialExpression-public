package com.example.todayfacialexpression.service.http;


import android.content.Intent;
import android.util.Log;


import com.example.todayfacialexpression.DTO.http.LoginRequest;
import com.example.todayfacialexpression.DTO.http.LoginResponse;
import com.example.todayfacialexpression.DTO.http.SignupRequest;
import com.example.todayfacialexpression.DTO.http.SignupResponse;
import com.example.todayfacialexpression.login.LoginActivity;
import com.example.todayfacialexpression.signup.RegisterActivity;


import java.sql.SQLException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HttpService  {
    private static HttpService instance;
    private RetrofitClient retrofitClient;
    private RetrofitClient.initAPI initApi;
    private LoginResponse loginResponse;
    private SignupResponse signupResponse;

    public HttpService() {
        retrofitClient = RetrofitClient.getInstance();
        initApi = RetrofitClient.getRetrofitInterface();
        loginResponse= new LoginResponse();
        signupResponse = new SignupResponse();
    }
    public static HttpService getService() {
        if (instance == null) {
            instance = new HttpService();
        }
        return instance;
    }

    public SignupResponse checkUserID(SignupRequest signupRequest, RegisterActivity registerActivity) throws SQLException, Exception {
        setSignupResponse(new SignupResponse());
        initApi.getCheckID(signupRequest).enqueue((new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if(response.isSuccessful()){
                    SignupResponse data= response.body();
                    switch (data.getCode()){
                        case(code.OK):{
                            Log.d("signup","성공");
                            Log.d("signup",data.isResult()+"");
                            break;
                        } case(code.SQLError):{
                            Log.d("login", "SQL 관련 문제");
                            try {
                                throw new SQLException("SQL 관련 문제");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            break;
                        } case(code.UnknownError):{
                            Log.d("login", "서버에서의 문제");
                            try {
                                throw new Exception("서버에서의 문제");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    registerActivity.checkIDProcess(data);
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                setSignupResponse(new SignupResponse());
                loginResponse.setCode(code.httpError);
                Log.d("login", "통신 문제로 실패");
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }));
        return signupResponse;
    }

    public LoginResponse login(LoginRequest loginRequest, LoginActivity loginActivity) throws SQLException,Exception{
        setLoginResponse(new LoginResponse());
       initApi.getLoginResponse(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse data = response.body();
                    System.out.println(data.toString());
                    switch (data.getCode()){
                        case(code.OK):{
                            Log.d("login", "성공");
                            break;
                        } case(code.SQLError):{
                            Log.d("login", "SQL 관련 문제");
                            try {
                                throw new SQLException("SQL 관련 문제");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            break;
                        } case(code.UnknownError):{
                            Log.d("login", "서버에서의 문제");
                            try {
                                throw new Exception("서버에서의 문제");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        } default:{
                            break;
                        }
                    }
                    loginActivity.loginProcess(data);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoginResponse(new LoginResponse());
                loginResponse.setCode(code.httpError);
                Log.d("login", "통신 문제로 실패");
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return loginResponse;
    }

    public SignupResponse signUp(SignupRequest signupRequest, RegisterActivity registerActivity)  throws SQLException,Exception{
        setSignupResponse(new SignupResponse());
        initApi.getSignupResponse(signupRequest).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if(response.isSuccessful()){
                    SignupResponse data= response.body();
                    switch (data.getCode()){
                        case(code.OK):{
                            Log.d("signup","성공");
                            Log.d("signup",data.isResult()+"");
                            break;
                        } case(code.SQLError):{
                            Log.d("login", "SQL 관련 문제");
                            try {
                                throw new SQLException("SQL 관련 문제");
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            break;
                        } case(code.UnknownError):{
                            Log.d("login", "서버에서의 문제");
                            try {
                                throw new Exception("서버에서의 문제");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        }
                    }
                    registerActivity.signUpProcess(data);
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                setSignupResponse(new SignupResponse());
                loginResponse.setCode(code.httpError);
                Log.d("login", "통신 문제로 실패");
                try {
                    throw new Exception();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return signupResponse;
    }

    public void setLoginResponse(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public void setSignupResponse(SignupResponse signupResponse) {
        this.signupResponse = signupResponse;
    }
}

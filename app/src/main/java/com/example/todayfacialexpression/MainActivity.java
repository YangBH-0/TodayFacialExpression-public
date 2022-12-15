package com.example.todayfacialexpression;

import static android.os.SystemClock.sleep;

import static com.example.todayfacialexpression.Loading.LoadingGifCode.loadingGif;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.todayfacialexpression.DTO.UserInfo;
import com.example.todayfacialexpression.Loading.LoadingGifCode;

import java.util.Random;

public class MainActivity extends AppCompatActivity {


    // Used to load the 'todayfacialexpression' library on application startup.
    static {
        System.loadLibrary("todayfacialexpression");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Example of a call to a native method
//        TextView tv = binding.sampleText;
//        tv.setText(stringFromJNI());



        setContentView(R.layout.activity_loading); // 로딩화면
        int gifImg = new Random().nextInt(loadingGif.length);
        ImageView imageView = (ImageView)findViewById(R.id.loading_face_model);
        Glide.with(this)
                .load(loadingGif[gifImg])
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        return false;
                    }
                })
                .into(imageView)
        ;  // 로딩 화면 때의 gif파일


        startLoading_v2(); // 로딩화면 재생
    }

    /**
     * A native method that is implemented by the 'todayfacialexpression' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // 로딩 화면 후 로그인 화면 띄우기
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent= new Intent(getApplicationContext(), com.example.todayfacialexpression.login.LoginActivity.class);
                startActivity(intent);  //Loagin화면을 띄운다.

                finish();   //현재 액티비티 종료

            }
        }, 5000); // 화면에 Logo 5초간 보이기
    }// startLoading Method..
    private void startLoading_v2(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                UserInfo userInfo = new UserInfo();
                userInfo.setUserName("guest");
                Intent intent = new Intent(getApplicationContext(), com.example.todayfacialexpression.chatroom.chatRoomActivity.class);
                intent.putExtra("userInfo",userInfo);
                startActivity(intent);  //charoom화면을 띄운다.
                finish();   //현재 액티비티 종료

            }
        }, 5000);

    }
}
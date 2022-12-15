package com.example.todayfacialexpression.chatroom;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.todayfacialexpression.DTO.UserInfo;
import com.example.todayfacialexpression.DTO.chatData;
import com.example.todayfacialexpression.R;
import com.example.todayfacialexpression.chatbot.ChatEvent;
import com.example.todayfacialexpression.util.JsonLoader;
import com.example.todayfacialexpression.util.ReturnComment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class chatRoomActivity extends Activity implements View.OnClickListener {
    private ArrayList<chatData> dataList;
    private chatAdapter adapter;
    private ImageButton sendBtn;
    private EditText editText;
    private RecyclerView recyclerView;
    private Boolean isMenuShow = false;
    private Boolean isExitFlag = false;
    private ViewGroup mainLayout;   //사이드 나왔을때 클릭방지할 영역
    private ViewGroup viewLayout;   //전체 감싸는 영역
    private ViewGroup sideLayout;   //사이드바만 감싸는 영역
    private int TAKE_PICTURE_CODE = 100; // intent 관련 코드
    private UserInfo userInfo;
    Bitmap mBitmap;
    boolean first;


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isMenuShow) {
            closeMenu();
        } else {
            if (isExitFlag) {
                finish();
            } else {
                isExitFlag = true;
                Toast.makeText(this, "뒤로가기를 한번더 누르시면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExitFlag = false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);   //나중에 바꾸기
        init();
        addSideView();

        recyclerView = findViewById(R.id.recyvlerv);
        final ImageButton sendBtn = findViewById(R.id.chatRoomSendBtn);
        editText = findViewById(R.id.editText1);
        ImageButton addImg = (ImageButton) findViewById(R.id.addImg);

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        dataList= new ArrayList<chatData>();
        adapter = new chatAdapter(dataList);
        recyclerView.setAdapter(adapter);
        chatEvent = new ChatEvent();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_ENTER || i == EditorInfo.IME_ACTION_SEND) {
                    sendBtn.performClick();
                    return true;
                }
                return false;
            }
        });


        // 사진 업로드 버튼
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicFromGallery();
            }
        });

        // 메시지 전송
        sendBtn.setOnClickListener(new View.OnClickListener() { // 버튼 클릭 시
            @Override
            public void onClick(View view) {
                String message = editText.getText().toString().trim();
                chatByUser(message);  // 사용자가 보낸 메시지 뷰로 띄워주기

                chatData chat = null;
                try {
                    chat = chatEvent.MessageCheck(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                viewScrollPositionRefresh(chat);
                initEditText(); // 입력부분 비워주기
            }
        });
    }

    private void init() {
        findViewById(R.id.btn_menu).setOnClickListener(this);
        viewLayout = findViewById(R.id.fl_silde);
        sideLayout = findViewById(R.id.view_sildebar);
        mainLayout = findViewById(R.id.id_main);
    }

    private void addSideView() {
        SideBarView sidebar = new SideBarView(chatRoomActivity.this);
        sideLayout.addView(sidebar);

        viewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sidebar.setEventListener(new SideBarView.EventListener() {

            @Override
            public void btnCancel() {
                closeMenu();
            }

            @Override
            public void btnCheck1() {
                //ChangeCharater
                changeChar("찬구");
                closeMenu();
            }

            @Override
            public void btnCheck2() {
                changeChar("채린");
                closeMenu();
            }

            @Override
            public void btnCheck3() {
                changeChar("교관");
                closeMenu();
            }

            @Override
            public void btnCheck4() {
                changeChar("예린");
                closeMenu();
            }

            @Override
            public void btnCheck5() {
                changeChar("주원");
                closeMenu();
            }

            @Override
            public void btnCheck6() {
                changeChar("덕춘");
                closeMenu();
            }

            @Override
            public void btnCheck7() {
                changeChar("호빈");
                closeMenu();
            }

            @Override
            public void btnCheck8() {
                changeChar("주하");
                closeMenu();
            }
        });
    }

    public String changeChar(String character) {
        chatEvent.setCharacter(character);
        return character;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_menu:
                showMenu();
                break;
        }
    }

    private void closeMenu() {
        isMenuShow = false;
        Animation slide = AnimationUtils.loadAnimation(chatRoomActivity.this, R.anim.sidebar_hidden);
        sideLayout.startAnimation(slide);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                viewLayout.setVisibility(View.GONE);
                viewLayout.setEnabled(false);
                mainLayout.setEnabled(true);
            }
        }, 450);
    }


    private void showMenu() {
        isMenuShow = true;
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.sidebar_show);
        sideLayout.startAnimation(slide);
        viewLayout.setVisibility(View.VISIBLE);
        viewLayout.setEnabled(true);
        mainLayout.setEnabled(false);
    }


    private void initEditText() {
        editText.setText(null);
    }

    private void addChat(chatData data) {
        adapter.addChat(data);
    }

    private void chatByUser(String message) {
        adapter.addChat(chatData.createUser(message));
    }

    protected void viewScrollPositionRefresh(chatData chatData) {
        addChat(chatData);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }

    private void initData() {
        dataList = new ArrayList<chatData>();
        addChat(new chatData("채린님이 게임에 참가하셨습니다.", null, Alignment.center, System.currentTimeMillis()));
        addChat(new chatData(userInfo.getUserName()+"님이 게임에 참가하셨습니다.", null, Alignment.center, System.currentTimeMillis()));
        addChat(chatData.create("채린", "반갑습니다."));
        addChat(chatData.create("채린", "표정 연습을 하고싶으시면 \"표정 연습\"을 입력해주세요"));
        addChat(chatData.create("채린", "음악 추천을 원하시면 \"음악 추천\"을 입력해주세요"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TAKE_PICTURE_CODE) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    InputStream in = null;

                    try {
                        in = getContentResolver().openInputStream(selectedImageUri);
                        mBitmap = BitmapFactory.decodeStream(in);
                        chatData tmp = new chatData(null, "사용자", Alignment.right_img, System.currentTimeMillis());
                        tmp.setBitmap(mBitmap);
                        chatData cd=chatEvent.testProcess(tmp);
                        viewScrollPositionRefresh(tmp);
                        if(cd !=null) {
                            viewScrollPositionRefresh(cd);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    initEditText(); // 입력부분 비워주기
                }
            }
        }
    }

    private void takePicFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, TAKE_PICTURE_CODE);
    }

    /*code for bind service*/
    boolean bound = false; // 바운드 여부

    private ChatEvent chatEvent;

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        userInfo = (UserInfo) getIntent().getExtras().get("userInfo");
        Intent intent = new Intent(this, ChatEvent.class);
        intent.putExtra("receiver",resultReceiver);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        if(!first){
            initData();
            first=true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        bound = false;
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
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

    // handler
    // for chatEvent
    private Handler handler = new Handler();
    private ResultReceiver resultReceiver = new ResultReceiver(handler){
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            Log.d("receive","successResult");
            if(resultCode==1){
                chatData temp = (chatData) resultData.get("chat");
                viewScrollPositionRefresh(temp);
            }
        }
    };


}

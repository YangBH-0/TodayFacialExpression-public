package com.example.todayfacialexpression.chatroom;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todayfacialexpression.R;

public class SideBarView extends RelativeLayout implements View.OnClickListener {
    public EventListener listener;

    public void setEventListener(EventListener l) { listener = l; }
    public interface EventListener {
        void btnCancel();
        void btnCheck1();
        void btnCheck2();
        void btnCheck3();
        void btnCheck4();
        void btnCheck5();
        void btnCheck6();
        void btnCheck7();
        void btnCheck8();
    }
    public SideBarView(Context context){
        this(context,null);
        init();
    }
    public SideBarView(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.tts_sidebar, this, true);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
        findViewById(R.id.check1).setOnClickListener(this);
        findViewById(R.id.check2).setOnClickListener(this);
        findViewById(R.id.check3).setOnClickListener(this);
        findViewById(R.id.check4).setOnClickListener(this);
        findViewById(R.id.check5).setOnClickListener(this);
        findViewById(R.id.check6).setOnClickListener(this);
        findViewById(R.id.check7).setOnClickListener(this);
        findViewById(R.id.check8).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.btn_cancel:
                listener.btnCancel();
                break;
            case R.id.check1:
                listener.btnCheck1();
                break;
            case R.id.check2:
                listener.btnCheck2();
                break;
            case R.id.check3:
                listener.btnCheck3();
                break;
            case R.id.check4:
                listener.btnCheck4();
                break;
            case R.id.check5:
                listener.btnCheck5();
                break;
            case R.id.check6:
                listener.btnCheck6();
                break;
            case R.id.check7:
                listener.btnCheck7();
                break;
            case R.id.check8:
                listener.btnCheck8();
                break;
            default:
                break;
        }

    }
}

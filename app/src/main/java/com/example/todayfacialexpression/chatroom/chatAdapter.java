package com.example.todayfacialexpression.chatroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.todayfacialexpression.DTO.chatData;
import com.example.todayfacialexpression.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class chatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<chatData> myDataList = null;
    private final SimpleDateFormat timeFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public chatAdapter(ArrayList<chatData> dataList){
        myDataList = dataList;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Alignment.center){
            view = inflater.inflate(R.layout.chat_room_center_item,parent,false);
            return new CenterViewHolder(view);
        }else if(viewType == Alignment.left){
            view = inflater.inflate(R.layout.chat_room_left_item,parent,false);
            return new LeftViewHolder(view);
        }else if (viewType == Alignment.right){
            view = inflater.inflate(R.layout.chat_room_right_item,parent,false);
            return new RightViewHolder(view);
        }else if (viewType == Alignment.left_img){
            view = inflater.inflate(R.layout.chat_room_left_img,parent,false);
            return new LeftImgViewHolder(view);
        }else if (viewType == Alignment.right_img){
            view = inflater.inflate(R.layout.chat_room_right_img,parent,false);
            return new RightImgViewHolder(view);
        }else{
            Log.d("viewHolder","no view Type");
            return null;
        }

    }

    // 실제 각 뷰 홀더에 데이터를 연결해주는 함수
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder instanceof CenterViewHolder){
            ((CenterViewHolder)viewHolder).textv.setText(myDataList.get(position).getContents());
        }else if(viewHolder instanceof LeftViewHolder){
            LeftViewHolder leftViewHolder = (LeftViewHolder)viewHolder;
            chatData elem = myDataList.get(position);
            String userNickname = elem.getUser();
            Integer imageCode = CharacterCode.getCode(userNickname);
            leftViewHolder.imgv.setBackground(new ShapeDrawable(new OvalShape()));
            leftViewHolder.imgv.setClipToOutline(true);

            leftViewHolder.imgv.setImageResource(imageCode);
            leftViewHolder.textv_nickname.setText(userNickname);
            leftViewHolder.textv_msg.setText(elem.getContents());
            leftViewHolder.textv_time.setText(formatTime(elem.getTime()));
        }else if(viewHolder instanceof RightViewHolder){
            ((RightViewHolder)viewHolder).textv_msg.setText(myDataList.get(position).getContents());
            ((RightViewHolder)viewHolder).textv_time.setText(formatTime(myDataList.get(position).getTime()));
        }else if(viewHolder instanceof LeftImgViewHolder){
            LeftImgViewHolder temp = (LeftImgViewHolder) viewHolder;
            temp.imageView.setImageBitmap(myDataList.get(position).getBitmap());
            temp.textv_time.setText(formatTime(myDataList.get(position).getTime()));
            temp.textv_nickname.setText(myDataList.get(position).getUser());
        }else if(viewHolder instanceof RightImgViewHolder){
            RightImgViewHolder temp = (RightImgViewHolder) viewHolder;
            Bitmap tmp=myDataList.get(position).getBitmap();
            temp.imageView.setImageBitmap(tmp);
            temp.textv_time.setText(formatTime(myDataList.get(position).getTime()));
        }

    }

    // 리사이클러뷰안에서 들어갈 뷰 홀더의 개수
    @Override
    public int getItemCount() {
        return myDataList.size();
    }

    // ★★★
    // 위에 3개만 오버라이드가 기본 셋팅임,
    // 이 메소드는 ViewType때문에 오버라이딩 했음(구별할려고)
    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getType();
    }
    // 채팅 추가
    public void addChat(chatData data){
        myDataList.add(data);
    }

    public String formatTime(long time){
        return timeFormat.format(new Date(time));
    }
    // "리사이클러뷰에 들어갈 뷰 홀더", 그리고 "그 뷰 홀더에 들어갈 아이템들을 셋팅"
    public class CenterViewHolder extends RecyclerView.ViewHolder{
        TextView textv;

        public CenterViewHolder(@NonNull View itemView) {
            super(itemView);
            textv = (TextView)itemView.findViewById(R.id.textv);
        }
    }

    public class LeftViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePicture;
        ImageView imgv;
        TextView textv_nickname;
        TextView textv_msg;
        TextView textv_time;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv = (ImageView)itemView.findViewById(R.id.imgv);
            textv_nickname = (TextView)itemView.findViewById(R.id.textv_name);
            textv_msg = (TextView)itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);

        }
    }

    public class RightViewHolder extends RecyclerView.ViewHolder{
        TextView textv_msg;
        TextView textv_time;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            textv_msg = (TextView)itemView.findViewById(R.id.textv_msg);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);
        }
    }
    public class LeftImgViewHolder extends  RecyclerView.ViewHolder{
        TextView textv_time;
        TextView textv_nickname;
        ImageView imageView;
        public LeftImgViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.chat_img);
            textv_nickname = (TextView)itemView.findViewById(R.id.textv_name);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);
        }
    }
    public class RightImgViewHolder extends  RecyclerView.ViewHolder{
        TextView textv_time;
        ImageView imageView;
        public RightImgViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.chat_img);
            textv_time = (TextView)itemView.findViewById(R.id.textv_time);
        }
    }
}

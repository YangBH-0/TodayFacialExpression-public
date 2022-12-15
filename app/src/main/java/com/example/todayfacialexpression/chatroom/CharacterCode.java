package com.example.todayfacialexpression.chatroom;

import com.example.todayfacialexpression.R;
import java.util.HashMap;

public class CharacterCode {
    private static HashMap<String, Integer> charMap = new HashMap<String, Integer>();
    static {
        charMap.put("찬구",R.drawable.changu);
        charMap.put("채린",R.drawable.chaerin);
        charMap.put("교관",R.drawable.gyogwan);
        charMap.put("예린",R.drawable.yerin);
        charMap.put("주원",R.drawable.juwon);
        charMap.put("덕춘",R.drawable.deokchun);
        charMap.put("호빈",R.drawable.hobin);
        charMap.put("주하",R.drawable.juha);    
    }
    /** 
    * get drawable code from nickname
    * @param nickname nickname must be korean.
    * @return drawable code */
    static Integer getCode(String nickname){
        return charMap.get(nickname);
    }
}

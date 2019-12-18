package com.example.wristwashsmartphone;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static List<String> extractMessageByRegular(String msg){
        List<String> list = new ArrayList<>();
        //Pattern p = Pattern.compile("(\\[[^\\]]*\\])");
        Pattern p = Pattern.compile("(\\([^\\)]*\\))");
        Matcher m = p.matcher(msg);
        while(m.find()){
            list.add(m.group().substring(1, m.group().length() - 1));
        }
        return list;
    }
}

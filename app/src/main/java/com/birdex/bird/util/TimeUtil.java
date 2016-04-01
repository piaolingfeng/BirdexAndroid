package com.birdex.bird.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huwei on 16/3/28.
 * 对时间格式和转换的操作
 */
public class TimeUtil {
    private static SimpleDateFormat format1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format2=new SimpleDateFormat("yyyy-MM-dd");
    public static  String calendar2String(Calendar calendar){
        if(calendar!=null){
            return format1.format(calendar.getTime());
        }else{
            return "";
        }
    }
    public static  String date2String(Date date){
        if(date!=null){
            return format1.format(date);
        }else{
            return "";
        }
    }
    public static  String calendar2SimpleString(Calendar calendar){
        if(calendar!=null){
            return format2.format(calendar.getTime());
        }else{
            return "";
        }
    }
}

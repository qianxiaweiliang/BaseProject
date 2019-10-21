package com.base.project.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) {
        return null;
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date(Long.parseLong(String.valueOf(timeStamp))));
    }
}

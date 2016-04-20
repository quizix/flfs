/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.utils;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author pronics3
 */
public class TimeUtil {

    public static String getCurrentTime() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        String s = f.format(Calendar.getInstance().getTime());
        return s;
    }
    
    public static String getCurrentDate() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String s = f.format(Calendar.getInstance().getTime());
        return s;
    }
    
    public static String getCurrentDateTime() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = f.format(Calendar.getInstance().getTime());
        return s;
    }


    /**
     * 判断是上午还是下午
     * @return
     */
    public static boolean isAmOrPm(){
        LocalTime time = LocalTime.now();
        if( time.getHour() <=12)
            return true;
        else
            return false;
    }

    public static LocalDate fromDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date toDate(LocalDate localDate){
        return  Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}

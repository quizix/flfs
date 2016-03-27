/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dxw.common.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
}

package com.awu.powerlottery.util;

import android.util.Base64;

/**
 * Created by awu on 2015-12-09.
 */
public class DesUtil {
    public static String EnBase64(String source){
        return Base64.encodeToString(source.getBytes(),Base64.DEFAULT);
    }

    public static String DeBase64(String base64){
        return new String(Base64.decode(base64,Base64.DEFAULT));
    }
}

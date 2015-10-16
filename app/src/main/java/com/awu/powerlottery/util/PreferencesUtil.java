package com.awu.powerlottery.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.awu.powerlottery.app.PowerLotteryApplication;


/**
 * SharedPreferences helper class.
 * Created by awu on 2015-10-13.
 */
public class PreferencesUtil {
    /**
     * app private sharedpreferences name.
     */
    private static final String SP_NAME = "com.awu.ordermenu.sp";

    /**
     * store value in sharedpreferences.
     * @param context current context.
     * @param key the name of preference to retrieve.
     * @param value the value to store in preference.
     */
    public static void setData(Context context,String key,Object value){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        String type = value.getClass().getSimpleName();
        if("Integer".equals(type)){
            editor.putInt(key,(Integer)value);
        }else if("String".equals(type)){
            editor.putString(key,(String)value);
        }else if("Boolean".equals(type)){
            editor.putBoolean(key,(Boolean)value);
        }else if("Long".equals(type)){
            editor.putLong(key,(Long)value);
        }else if("Float".equals(type)){
            editor.putFloat(key,(Float)value);
        }

        editor.commit();
    }

    /**
     * read value from sharedpreferences.
     * @param context current context.
     * @param key the name of the preference to retrieve.
     * @param defaultValue value to return if key does not exist.
     * @return returns the value if it exist or defaultValue.
     */
    public static Object getData(Context context,String key,Object defaultValue){
        SharedPreferences sp = context.getSharedPreferences(SP_NAME,Context.MODE_PRIVATE);

        String type = defaultValue.getClass().getSimpleName();
        if("Integer".equals(type)){
            return sp.getInt(key,(Integer)defaultValue);
        }else if("String".equals(type)){
            return sp.getString(key, (String) defaultValue);
        }else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean) defaultValue);
        }else if("Long".equals(type)){
            return sp.getLong(key, (Long) defaultValue);
        }else if("Float".equals(type)){
            return sp.getFloat(key, (Float) defaultValue);
        }

        return  defaultValue;
    }

    /**
     * store value in sharedpreferences by application context.
     * @param key the name of preference to retrieve.
     * @param value the value to store in preference.
     */
    public static void setData(String key,Object value){
        setData(PowerLotteryApplication.appContext(), key, value);
    }

    /**
     * read value from sharedpreferences by application context.
     * @param key the name of the preference to retrieve.
     * @param defaultValue value to return if key does not exist.
     * @return returns the value if it exist or defaultValue.
     */
    public static Object getData(String key,Object defaultValue){
        return  getData(PowerLotteryApplication.appContext(),key,defaultValue);
    }
}

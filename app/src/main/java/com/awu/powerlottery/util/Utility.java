package com.awu.powerlottery.util;

import com.awu.powerlottery.R;
import com.awu.powerlottery.app.PowerLotteryApplication;
import com.awu.powerlottery.entity.LotteryResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * Created by awu on 2015-10-15.
 */
public class Utility {
    private static final String TAG = "Utility";

    /**
     * Compare datetime with now
     * @param date
     * @return if date is after now,return true.
     */
    public static boolean compareCurrentDateTime(String date){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");

        try {
            java.util.Date dt1 = df.parse(date);
            java.util.Date dt2 = new Date();
            Log.i(TAG, "compareDate date1:" + dt1.getTime());
            Log.i(TAG, "compareDate date2:" + dt2.getTime());
            if (dt1.getTime() > dt2.getTime()) {
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            Log.i(TAG, "compareDate ex:"+e.getMessage());
            return false;
        }
    }

    public static boolean afterCurrentDate(String date){
        SimpleDateFormat  df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            java.util.Date dt1 = df.parse(date);

            Calendar calendar = Calendar.getInstance();
            String dt2Str = String.format("%d-%d-%d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
            java.util.Date dt2 = df.parse(dt2Str);
            Log.i(TAG, "compareDate date1:" + dt1.toString());
            Log.i(TAG, "compareDate date2:" + dt2.toString());
            if (dt1.getTime() > dt2.getTime()) {
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            Log.i(TAG, "compareDate ex:"+e.getMessage());
            return false;
        }
    }

    public static int todayWeekDay(){
        Calendar calendar = Calendar.getInstance();
        int value = calendar.get(Calendar.DAY_OF_WEEK);
        if(value == 1)
            return 7;
        else
            return value - 1;
    }

    public static String aboutMessage(){
        return "当前版本：" + getVersion() + "\n祝君中奖";
    }

    /**
     * get application version.
     * @return
     */
    public static String getVersion() {
        Context context = PowerLotteryApplication.appContext();
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


}

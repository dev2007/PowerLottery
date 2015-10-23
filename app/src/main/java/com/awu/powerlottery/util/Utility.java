package com.awu.powerlottery.util;

import com.awu.powerlottery.entity.LotteryResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import android.util.Log;

/**
 * Created by awu on 2015-10-15.
 */
public class Utility {
    private static final String TAG = "Utility";
    /**
     * the newest phase key name.
     */
    public static final String KEY_NEW_PHASE = "next_phase";
    /**
     * the newest date key name.
     */
    public static final String KEY_NEW_DATE = "date";
    /**
     * the current phase key name.
     */
    public  static final String KEY_PHASE = "phase";

    /**
     * the date of latest phase key name.
     */
    public static final String KEY_ENDDATE = "enddate";

    /**
     * the prize result key name.
     */
    public static final String KEY_RESULT = "result";

    /**
     * the prize list key name.
     */
    public static final String KEY_PRIZE = "prize";

    /**
     * the list LotteryType with phase date key name.
     */
    private static final Map<LotteryType,String> LotteryPhaseDateList = new HashMap<LotteryType,String>();

    /**
     * the list LotteryType with phase key name.
     */
    private static final Map<LotteryType,String> LotteryPhaseList = new HashMap<LotteryType,String>();


    static {
        LotteryPhaseDateList.put(LotteryType.SHUANGSEQIU,"SSQ_DATE");
        LotteryPhaseDateList.put(LotteryType.FUCAI3D,"FC_DATE");

        LotteryPhaseList.put(LotteryType.SHUANGSEQIU,"SSQ_PHASE");
        LotteryPhaseList.put(LotteryType.FUCAI3D,"FC_PHASE");
    }


    /**
     * Store the newest phase and date.
     * @param lotteryType
     * @param newData
     */
    public static void storeNewPhase(LotteryType lotteryType,Map<String,Object> newData){
        int phase = Integer.parseInt((String)newData.get(KEY_NEW_PHASE));
        String date = (String)newData.get(KEY_NEW_DATE);

        PreferencesUtil.setData(KEY_NEW_PHASE,phase);
        PreferencesUtil.setData(KEY_NEW_DATE,date);
        PreferencesUtil.setData(KEY_PHASE,phase - 1);

        setPhase(lotteryType,phase - 1);
        setPhaseDate(lotteryType,date);
    }

    private static Map<String, Object> SSQ_PrizeData = new HashMap<String, Object>();

    public static void storePhaseData(LotteryType lotteryType,Map<String, Object> data){
        SSQ_PrizeData = data;
    }

    /**
     * get SSQ latest phase date.
     * @return
     */
    public static String getSSQPrizeDate(){
        if(SSQ_PrizeData.containsKey(KEY_ENDDATE)) {
            String endDate = (String) SSQ_PrizeData.get(KEY_ENDDATE);
            return endDate;
        }else{
            return "";
        }
    }

    /***
     * get SSQ latest result.
     * @return
     */
    public static String[] getSSQResult(){
        if(SSQ_PrizeData.containsKey(KEY_RESULT)){
            String result = (String)SSQ_PrizeData.get(KEY_RESULT);
            return result.split(",");
        }else{
            return new String[0];
        }
    }

    /**
     * get ssq prize list.
     * @return
     */
    public static String[] getSSQPrize(){
        String[] prize = new String[28];
        ArrayList<Map<String,String>> prizeList = (ArrayList<Map<String,String>>)SSQ_PrizeData.get(KEY_PRIZE);
        int index = 4;
        prize[0] = "奖项";
        prize[1] = "中奖注数";
        prize[2] = "单注奖金";
        prize[3] = "中奖条件";
        String[] prizeCondition = {"6+1","6+0","5+1","5+0/4+1","4+0/3+1","2+1/1+1/0+1"};
        for (Map<String,String> map : prizeList){
            prize[index] = map.get("name");
            prize[index+1] = map.get("bet");
            prize[index+2] = map.get("prize");
            prize[index+3] = prizeCondition[index/4-1];
            index += 4;
        }
        return  prize;
    }

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

    /**
     * set phase.
     * @param lotteryType
     * @param phase
     */
    public static void setPhase(LotteryType lotteryType,int phase){
        PreferencesUtil.setData(LotteryPhaseList.get(lotteryType),phase);
    }

    /**
     * get phase.
     * @param lotteryType
     * @return
     */
    public static int getPhase(LotteryType lotteryType){
        return (int)PreferencesUtil.getData(LotteryPhaseList.get(lotteryType),0);
    }

    /**
     * set phase date.
     * @param lotteryType
     * @param date
     */
    public static void setPhaseDate(LotteryType lotteryType,String date){
        PreferencesUtil.setData(LotteryPhaseDateList.get(lotteryType),date);
    }

    /**
     * get phase date.
     * @param lotteryType
     * @return
     */
    public static String getPhaseDate(LotteryType lotteryType){
        return (String) PreferencesUtil.getData(LotteryPhaseDateList.get(lotteryType), "2014-07-07 00:01");
    }
}

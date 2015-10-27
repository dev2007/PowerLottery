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
        LotteryPhaseDateList.put(LotteryType.FUCAI3D,"FC3D_DATE");

        LotteryPhaseList.put(LotteryType.SHUANGSEQIU,"SSQ_PHASE");
        LotteryPhaseList.put(LotteryType.FUCAI3D,"FC3D_PHASE");
    }


    /**
     * Store the newest phase and date.
     * @param lotteryType
     * @param newData
     */
    public static void storeNewPhase(LotteryType lotteryType,Map<String,Object> newData){
        int phase = Integer.parseInt((String)newData.get(KEY_NEW_PHASE));
        String date = (String)newData.get(KEY_NEW_DATE);

        setPhase(lotteryType,phase - 1);
        setPhaseDate(lotteryType,date);
    }

    private static Map<String, Object> SSQ_PrizeData = new HashMap<String, Object>();
    private static Map<String, Object> FC3D_PrizeData = new HashMap<String, Object>();

    /***
     * store prize data.
     * @param lotteryType
     * @param data
     */
    public static void pushPhaseData(LotteryType lotteryType,Map<String, Object> data){
        switch (lotteryType){
            case SHUANGSEQIU:
                SSQ_PrizeData = data;
                break;
            case FUCAI3D:
                FC3D_PrizeData = data;
                break;
            default:
                break;
        }
    }

    private static Map<String, Object> switchPrizeData(LotteryType lotteryType){
        Map<String, Object> data = new HashMap<String,Object>();
        switch (lotteryType){
            case SHUANGSEQIU:
                data = SSQ_PrizeData;
                break;
            case FUCAI3D:
                data = FC3D_PrizeData;
                break;
            default:
                break;
        }
        return data;
    }

    /***
     * get phase date.
     * @param lotteryType
     * @return
     */
    public static String pullPhaseDate(LotteryType lotteryType){
        Map<String,Object> data = switchPrizeData(lotteryType);

        if(data.containsKey(KEY_ENDDATE)) {
            return (String) data.get(KEY_ENDDATE);
        }else{
            return "";
        }
    }

    /**
     * get prize result.
     * @param lotteryType
     * @return
     */
    public static String[] pullPrizeResult(LotteryType lotteryType){
        Map<String,Object> data = switchPrizeData(lotteryType);

        if(data.containsKey(KEY_RESULT)){
            String result = (String)data.get(KEY_RESULT);
            return result.split(",");
        }else{
            return new String[0];
        }
    }


    /**
     * get prize detail.
     * @param lotteryType
     * @return
     */
    public static String[] pullPrize(LotteryType lotteryType){
        switch (lotteryType){
            case SHUANGSEQIU:
                return getSSQPrize();
            case FUCAI3D:
                return get3DPrize();
            default:
                break;
        }
        return new String[0];
    }

    /**
     * get ssq prize list.
     * @return
     */
    private static String[] getSSQPrize(){
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

    private static String[] get3DPrize(){
        String[] prize = new String[12];
        ArrayList<Map<String,String>> prizeList = (ArrayList<Map<String,String>>)FC3D_PrizeData.get(KEY_PRIZE);
        int index = 3;
        prize[0] = "奖项";
//        prize[1] = "中奖条件";
        prize[1] = "中奖注数";
        prize[2] = "单注奖金";
        String[] prizeDesc = {"选号与奖号按顺序全部相同","选号与奖号一致(顺序不限)，且任意两位相同","选号与奖号一致(顺序不限)，且3位数各不相同"};
        for (Map<String,String> map : prizeList){
            prize[index] = map.get("name");
//            prize[index+1] = prizeDesc[index/4-1];
            prize[index+1] = map.get("bet");
            prize[index+2] = map.get("prize");
            index += 3;
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

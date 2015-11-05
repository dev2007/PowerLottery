package com.awu.powerlottery.bl;

import android.os.Message;
import android.util.Log;

import com.awu.powerlottery.activity.BaseFragment;
import com.awu.powerlottery.app.PowerLotteryApplication;
import com.awu.powerlottery.db.DBUtil;
import com.awu.powerlottery.entity.PrizeResult;
import com.awu.powerlottery.handler.QueryDataHandler;
import com.awu.powerlottery.handler.QueryLatestHandler;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.util.PreferencesUtil;
import com.awu.powerlottery.util.Utility;
import com.awu.powerlottery.util.WebUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by awu on 2015-11-03.
 */
public class DataLayer {
    private static String TAG = "DATALAYER";

    /**
     * the newest phase key name.
     */
    public static final String KEY_NEW_PHASE = "next_phase";
    /**
     * the newest date key name.
     */
    public static final String KEY_NEW_DATE = "date";

//    public static Map<Integer,Map<String,List<PrizeResult>>> PrizeData = new HashMap<>();

    public static Map<String, List<PrizeResult>> SSQ_PrizeData = new HashMap<>();

    public static Map<String, List<PrizeResult>> FC3D_PrizeData = new HashMap<>();

    private static String selectedPhase = "";

    /**
     * the list LotteryType with phase date key name.
     */
    private static final Map<LotteryType, String> LotteryPhaseDateList = new HashMap<LotteryType, String>();

    /**
     * the list LotteryType with phase key name.
     */
    private static final Map<LotteryType, String> LotteryPhaseList = new HashMap<LotteryType, String>();

    static {
        LotteryPhaseDateList.put(LotteryType.SHUANGSEQIU, "SSQ_DATE");
        LotteryPhaseDateList.put(LotteryType.FUCAI3D, "FC3D_DATE");

        LotteryPhaseList.put(LotteryType.SHUANGSEQIU, "SSQ_PHASE");
        LotteryPhaseList.put(LotteryType.FUCAI3D, "FC3D_PHASE");
    }

    /**
     * Store the newest phase and date.
     *
     * @param lotteryType
     * @param newData
     */
    public static void storeNewPhase(LotteryType lotteryType, Map<String, Object> newData) {
        int phase = Integer.parseInt((String) newData.get(KEY_NEW_PHASE));
        String date = (String) newData.get(KEY_NEW_DATE);

        setPhase(lotteryType, phase - 1);
        setPhaseDate(lotteryType, date);
    }

    public static void getNewLottery(LotteryType lotteryType, QueryLatestHandler queryLatestHandler, BaseFragment fragment) {
        String date = getPhaseDate(lotteryType);
        //newest has not result
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if (Utility.compareCurrentDateTime(date)) {
            Log.i(TAG, "P had phase");
            queryLatestHandler.sendEmptyMessage(QueryLatestHandler.MSG_OK);
        } else {
            fragment.showProgressDialog(true);
            Log.i(TAG, "Request phase");
            WebUtility.queryNewLottery(lotteryType, queryLatestHandler);
        }
    }

    /**
     * set phase.
     *
     * @param lotteryType
     * @param phase
     */
    public static void setPhase(LotteryType lotteryType, int phase) {
        PreferencesUtil.setData(LotteryPhaseList.get(lotteryType), phase);
    }

    /**
     * set phase date.
     *
     * @param lotteryType
     * @param date
     */
    public static void setPhaseDate(LotteryType lotteryType, String date) {
        PreferencesUtil.setData(LotteryPhaseDateList.get(lotteryType), date);
    }

    /**
     * get phase date.
     *
     * @param lotteryType
     * @return
     */
    public static String getPhaseDate(LotteryType lotteryType) {
        return (String) PreferencesUtil.getData(LotteryPhaseDateList.get(lotteryType), "2014-07-07 00:01");
    }

    /**
     * get phase.
     *
     * @param lotteryType
     * @return
     */
    public static int getPhase(LotteryType lotteryType) {
        return (int) PreferencesUtil.getData(LotteryPhaseList.get(lotteryType), 0);
    }

    public static void getLotteryResult(LotteryType lotteryType, String phase, QueryDataHandler queryDataHandler, BaseFragment fragment) {
        Log.i(TAG,"Get Lottery Result:"+phase);
        selectedPhase = phase;
        List<PrizeResult> list = DBUtil.instance(PowerLotteryApplication.appContext()).getPrize(lotteryType, phase);
        if (list.size() == 0) {
            Log.i(TAG, "Request prize");
            WebUtility.queryLottery(lotteryType, phase, queryDataHandler);
        } else {
            Log.i(TAG, "DB has prize");
            appendLotteryResult(lotteryType, phase, list);
            if (queryDataHandler != null) {
                Message msg = new Message();
                msg.what = 1;
                queryDataHandler.handleMessage(msg);
            }
        }
    }

    public static void appendLotteryResult(LotteryType lotteryType, String phase, List<PrizeResult> list) {
        Map<String, List<PrizeResult>> typeResult = switchTypeStore(lotteryType);

        Log.i(TAG, "append phase:" + phase);
        if (typeResult == null) {
            Log.i(TAG, "append null");
        } else {
            //had not phase key.
            if (!typeResult.containsKey(phase)) {
                Log.d(TAG, "append ok");
                typeResult.put(phase, list);
            }
        }
    }

    private static Map<String, List<PrizeResult>> switchTypeStore(LotteryType lotteryType) {
        switch (lotteryType) {
            case SHUANGSEQIU:
                return SSQ_PrizeData;
            case FUCAI3D:
                return FC3D_PrizeData;
            default:
                return null;
        }
    }

    /**
     * get prize detail.
     *
     * @param lotteryType
     * @return
     */
    public static String[] pullPrize(LotteryType lotteryType) {
        switch (lotteryType) {
            case SHUANGSEQIU:
                return getSSQPrize(selectedPhase);
            case FUCAI3D:
                return get3DPrize(selectedPhase);
            default:
                break;
        }
        return new String[0];
    }

    /**
     * get ssq prize list.
     *
     * @return
     */
    private static String[] getSSQPrize(String phase) {
        Log.i(TAG, "ssq prize phase:" + phase);
        String[] prize = new String[28];
//        ArrayList<Map<String,String>> prizeList = (ArrayList<Map<String,String>>)SSQ_PrizeData.get(KEY_PRIZE);

        //had ssq data.
        Map<String, List<PrizeResult>> prizeList = switchTypeStore(LotteryType.SHUANGSEQIU);

        if (!prizeList.containsKey(phase))
            return new String[0];

        List<PrizeResult> list = prizeList.get(phase);

        int index = 4;
        prize[0] = "奖项";
        prize[1] = "中奖注数";
        prize[2] = "单注奖金";
        prize[3] = "中奖条件";
        String[] prizeName = {"一等奖", "二等奖", "三等奖", "四等奖", "五等奖", "六等奖"};
        String[] prizeCondition = {"6+1", "6+0", "5+1", "5+0/4+1", "4+0/3+1", "2+1/1+1/0+1"};
        for (PrizeResult map : list) {
            prize[index] = prizeName[index / 4 - 1];
            prize[index + 1] = "" + map.getTotal();
            prize[index + 2] = "" + map.getPrizemoney();
            prize[index + 3] = prizeCondition[index / 4 - 1];
            index += 4;
        }
        return prize;
    }

    private static String[] get3DPrize(String phase) {
        String[] prize = new String[12];
//        ArrayList<Map<String,String>> prizeList = (ArrayList<Map<String,String>>)FC3D_PrizeData.get(KEY_PRIZE);

        //had ssq data.
        Map<String, List<PrizeResult>> prizeList = switchTypeStore(LotteryType.FUCAI3D);

        if (!prizeList.containsKey(phase))
            return new String[0];

        List<PrizeResult> list = prizeList.get(phase);

        int index = 3;
        prize[0] = "奖项";
//        prize[1] = "中奖条件";
        prize[1] = "中奖注数";
        prize[2] = "单注奖金";
        String[] prizeName = {"直选", "组三", "组六"};
        String[] prizeDesc = {"选号与奖号按顺序全部相同", "选号与奖号一致(顺序不限)，且任意两位相同", "选号与奖号一致(顺序不限)，且3位数各不相同"};
        for (PrizeResult map : list) {
            prize[index] = prizeName[index / 3 - 1];
//            prize[index+1] = prizeDesc[index/4-1];
            prize[index + 1] = "" + map.getTotal();
            prize[index + 2] = "" + map.getPrizemoney();
            index += 3;
        }
        return prize;
    }

    /**
     * get prize result.
     *
     * @param lotteryType
     * @return
     */
    public static String[] pullPrizeResult(LotteryType lotteryType) {

        //had ssq data.
        Map<String, List<PrizeResult>> prizeList = switchTypeStore(lotteryType);

        if (!prizeList.containsKey(selectedPhase))
            return new String[0];

        List<PrizeResult> list = prizeList.get(selectedPhase);
        String result = list.get(0).getResult();
        Log.i(TAG, "prize result:" + result);
        return result.split(",");
    }

    /***
     * get phase date.
     *
     * @param lotteryType
     * @return
     */
    public static String pullPhaseDate(LotteryType lotteryType) {

        //had ssq data.
        Map<String, List<PrizeResult>> prizeList = switchTypeStore(lotteryType);

        if (!prizeList.containsKey(selectedPhase))
            return "";

        List<PrizeResult> list = prizeList.get(selectedPhase);
        Log.i(TAG, "phase date:" + list.get(0).getDate());
        return list.get(0).getDate();
    }
}

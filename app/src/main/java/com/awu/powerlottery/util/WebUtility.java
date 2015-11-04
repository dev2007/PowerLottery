package com.awu.powerlottery.util;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.entity.LotteryResult;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by awu on 2015-10-15.
 */
public class WebUtility {
    /**
     * Web process result ok.
     */
    public static final int MSG_OK = 1;
    /**
     * Web process result fail.
     */
    public static final int MSG_FAIL = -1;

    private static final String TAG = "WebUtility";
    private static final String LOTTERY_HOST = "http://baidu.lecai.com";
    private static final String LOTTERY_QUERY = "/lottery/draw/ajax_get_detail.php?";
    private static final String LOTTERY_PARAM_TYPE = "lottery_type";
    private static final String LOTTERY_PARAM_DATE = "phase";

    private static final String LOTTERY_NEW = "/lottery/ajax_current.php?";

    private static String queryLotteryResultAddress(LotteryType lotteryType,String phase){
        return String.format("%s%s%s=%d&%s=%s",LOTTERY_HOST,LOTTERY_QUERY,
                LOTTERY_PARAM_TYPE,lotteryType.getValue(),LOTTERY_PARAM_DATE,phase);
    }

    private static String queryNewLotteryAddress(LotteryType lotteryType){
        return String.format("%s%s%s=%s",LOTTERY_HOST,LOTTERY_NEW,LOTTERY_PARAM_TYPE,lotteryType.getValue());
    }

    public static void queryLottery(final LotteryType lotteryType,final String phase, final Handler msgHandler){
        String url = queryLotteryResultAddress(lotteryType, phase);
        Log.i(TAG, "queryLottery url:"+url);
        HttpUtil.sendHttpRequest(url, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i(TAG, "onFinish query:" + response);
                LotteryResult.parseDetail(phase,response,lotteryType);
                if(msgHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_OK;
                    msgHandler.handleMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError query:" + e.getMessage());
                if(msgHandler != null) {
                    Message msg = new Message();
                    msg.what = MSG_FAIL;
                    msgHandler.handleMessage(msg);
                }
            }
        });
    }

    /***
     * query the newest prize result.
     * @param lotteryType
     * @param msgHandler
     */
    public static void queryNewLottery(final LotteryType lotteryType, final Handler msgHandler){
        String url = queryNewLotteryAddress(lotteryType);
        Log.i(TAG, "queryNewLottery url:" + url);
        HttpUtil.sendHttpRequest(url, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i(TAG, "onFinish newlottery.");
                Map<String,Object> result = LotteryResult.parseNewPhase(response);
                DataLayer.storeNewPhase(lotteryType, result);
                if(msgHandler != null){
                    Message msg = new Message();
                    msg.what = MSG_OK;
                    msgHandler.sendMessage(msg);
                }
            }

            @Override
            public void onError(Exception e) {
                Log.i(TAG, "onError newlottery.");
                if(msgHandler != null){
                    Message msg = new Message();
                    msg.what = MSG_FAIL;
                    msgHandler.sendMessage(msg);
                }
            }
        });
    }
}

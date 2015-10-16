package com.awu.powerlottery.util;

import android.util.Log;
/**
 * Created by awu on 2015-10-15.
 */
public class WebUtility {
    private static final String TAG = "WebUtility";
    private static final String LOTTERY_HOST = "http://baidu.lecai.com";
    private static final String LOTTERY_QUERY = "/lottery/draw/ajax_get_detail.php?";
    private static final String LOTTERY_PARAM_TYPE = "lottery_type";
    private static final String LOTTERY_PARAM_DATE = "phase";

    private static String queryAddress(LotteryType lotteryType,String phase){
        return String.format("%s%s%s=%d&%s=%s",LOTTERY_HOST,LOTTERY_QUERY,
                LOTTERY_PARAM_TYPE,lotteryType.getValue(),LOTTERY_PARAM_DATE,phase);
    }

    public static void queryLottery(LotteryType lotteryType,String phase){
        String url = queryAddress(lotteryType, phase);
        Log.i(TAG, "queryLottery url:"+url);
        HttpUtil.sendHttpRequest(url, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Log.i(TAG, "onFinish result:"+response);
            }

            @Override
            public void onError(Exception e) {
                Log.e(TAG, "onError request fail:" + e.getMessage());
            }
        });
    }
}

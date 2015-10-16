package com.awu.powerlottery.entity;


import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

/**
 * Created by awu on 2015-10-15.
 */
public class LotteryResult {
    private static final String TAG = "LotteryResult";

    /**
     * Parse shuangseqiu result.
     * @param response The server response result json string.
     * @return Result string,such like "16,21,24,26,27,29,16"
     */
    public static String parseShuangseqiu(String response) {
        String resultStr = "";
        try {
            JSONObject jsonobject = new JSONObject(response);
            JSONObject data = jsonobject.getJSONObject("data");
            JSONObject result = data.getJSONObject("result");
            JSONArray result2 = result.getJSONArray("result");
            JSONObject obj = (JSONObject) result2.get(0);
            String key = obj.getString("key");
            JSONArray redData = null;
            JSONArray blueData = null;
            if (key.equals("red"))
                redData = obj.getJSONArray("data");
            else
                blueData = obj.getJSONArray("data");

            obj = (JSONObject) result2.get(1);
            key = obj.getString("key");
            if (key.equals("red"))
                redData = obj.getJSONArray("data");
            else
                blueData = obj.getJSONArray("data");
            if (redData != null) {
                for (int i = 0; i < redData.length(); i++) {
                    String num = (String) redData.get(i);
                    resultStr += num + ",";
                }
            }
            if (blueData != null) {
                resultStr += (String) blueData.get(0);
            }
            Log.i(TAG, "parseShuangseqiu result:" + resultStr);
            return resultStr;
        } catch (Exception e) {
            Log.i(TAG, "parseShuangseqiu exception:" + e.getMessage());
            return "";
        }
    }
}

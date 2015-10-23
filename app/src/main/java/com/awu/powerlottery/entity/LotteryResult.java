package com.awu.powerlottery.entity;


import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.awu.powerlottery.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by awu on 2015-10-15.
 */
public class LotteryResult {
    private static final String TAG = "LotteryResult";

    /**
     * Parse shuangseqiu result.
     *
     * @param response The server response result json string.
     * @return Result string,such like "16,21,24,26,27,29,16"
     */
    public static Map<String, Object> parseShuangseqiu(String response) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject jsonobject = new JSONObject(response);
            JSONObject data = jsonobject.getJSONObject("data");
            map.put("result",prizeResult(data));
            map.put("enddate",prizeEndDate(data));
            map.put("prize",prizeList(data));

        } catch (Exception e) {
            map = new HashMap<String, Object>();
        }
        return map;
    }

    private static String prizeResult(JSONObject data) {
        String resultStr = "";
        try {
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

    private static String prizeEndDate(JSONObject data) {
        try{
            String endDate = data.getString("time_draw");
            Log.i(TAG, "prizeEndDate enddate:" +endDate);
            return endDate;
        }catch (Exception e){
            return "";
        }
    }

    private static ArrayList<Map<String,String>> prizeList(JSONObject data) {
        //result_detail
        ArrayList<Map<String,String>> list = new ArrayList<>();
        try{
            JSONObject result = data.getJSONObject("result_detail");
            JSONArray result2 = result.getJSONArray("resultDetail");
            for (int i = 0;i < result2.length();i++){
                JSONObject obj = (JSONObject)result2.get(i);
                Map<String,String> row = new HashMap<>();
                row.put("key",obj.getString("key"));
                row.put("bet",obj.getString("bet"));
                row.put("prize",obj.getString("prize"));
                row.put("name",obj.getString("name"));
                Log.i(TAG, "prizeList prize:"+ row.get("key") + row.get("bet") + row.get("prize")+row.get("name"));
                list.add(row);
            }
        }catch (Exception e){
            return new ArrayList<Map<String,String>>();
        }
        return list;
    }

    /**
     * get the newest phase and date.
     * @param response
     * @return
     */
    public static Map<String,Object> parseNewPhase(String response){
        Map<String,Object> map = new HashMap<>();
        try{
            JSONObject object = new JSONObject(response);
            JSONObject data = object.getJSONObject("data");
            String phase = data.getString("phase");
            String date = data.getString("time_draw");
            Log.i(TAG, "parseNewPhase phase:" + phase);
            Log.i(TAG, "parseNewPhase data:" + date);
            map.put(Utility.KEY_NEW_PHASE,phase);
            map.put(Utility.KEY_NEW_DATE,date);
        }catch (Exception e){
            return new HashMap<String, Object>();
        }
        return map;
    }
}

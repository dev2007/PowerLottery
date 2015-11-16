package com.awu.powerlottery.entity;


import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.awu.powerlottery.app.PowerLotteryApplication;
import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.db.DBUtil;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.util.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by awu on 2015-10-15.
 */
public class LotteryResult {
    private static final String TAG = "LotteryResult";


    public static void parseDetail(String phase,String response,LotteryType lotteryType){
        List<PrizeResult> list = new ArrayList<>();
        try {
            JSONObject jsonobject = new JSONObject(response);
            JSONObject data = jsonobject.getJSONObject("data");

            String result = "";
            switch (lotteryType){
                case SHUANGSEQIU:
                    result = prizeResult(data);
                    break;
                case FUCAI3D:
                    result = prize3DResult(data);
                    break;
                case QILECAI:
                    result = prizeResult(data);
                    break;
                case DALETOU:
                    result = prizeDLTResult(data);
                    break;
                default:
                    break;
            }

            String date = prizeEndDate(data);

            Log.i(TAG,"request phase:"+phase);
            Log.i(TAG,"request phase date:"+date);
            Log.i(TAG,"request phase result:"+result);

            int index = 1;
            for (Map<String,String> row : prizeList(data)){
                Log.i(TAG,"LR 55:"+ index++);
                PrizeResult prizeResult = new PrizeResult();
                prizeResult.setPhase(Integer.parseInt(phase));
                prizeResult.setResult(result);
                prizeResult.setType(lotteryType.getName());
                String bet = row.get("bet");
                prizeResult.setTotal(Integer.parseInt(bet.equals("") ? "0" : bet));
                String prize = row.get("prize");
                prizeResult.setPrizemoney(Integer.parseInt(prize.equals("") ? "0" : prize));
                prizeResult.setDate(date);
                list.add(prizeResult);
            }
            Log.i(TAG,"LR 68");
            DataLayer.appendLotteryResult(lotteryType,phase,list);
            DBUtil.instance(PowerLotteryApplication.appContext()).savePrize(lotteryType,list);

        } catch (Exception e) {
            Log.e(TAG,"LR:"+e.getMessage());
        }
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

    private static String prize3DResult(JSONObject data) {
        String resultStr = "";
        try {
            JSONObject result = data.getJSONObject("result");
            JSONArray result2 = result.getJSONArray("result");
            JSONObject obj = (JSONObject) result2.get(0);
            String key = obj.getString("key");
            JSONArray ballData = null;
            if (key.equals("ball"))
                ballData = obj.getJSONArray("data");

            if(ballData != null)
            for (int i = 0; i < ballData.length(); i++) {
                String num = (String) ballData.get(i);
                resultStr += num + ",";
            }

            resultStr = resultStr.substring(0,resultStr.length() - 1);

            Log.i(TAG, "parse3d result:" + resultStr);
            return resultStr;
        } catch (Exception e) {
            Log.i(TAG, "parse3d exception:" + e.getMessage());
            return "";
        }
    }

    private static String prizeDLTResult(JSONObject data) {
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
                for (int i = 0; i < blueData.length(); i++) {
                    String num = (String) blueData.get(i);
                    resultStr += num + ",";
                }
            }

            resultStr = resultStr.substring(0,resultStr.length() - 1);
            Log.i(TAG, "parsedlt result:" + resultStr);
            return resultStr;
        } catch (Exception e) {
            Log.i(TAG, "parsedlt exception:" + e.getMessage());
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
            Log.e(TAG,"LR 163:"+e.getMessage());
            return new ArrayList<Map<String,String>>();
        }
        Log.i(TAG,"prize list ok");
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
            map.put(DataLayer.KEY_NEW_PHASE,phase);
            map.put(DataLayer.KEY_NEW_DATE,date);
        }catch (Exception e){
            return new HashMap<String, Object>();
        }
        return map;
    }
}

package com.awu.powerlottery.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.awu.powerlottery.entity.PrizeResult;
import com.awu.powerlottery.util.LotteryType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awu on 2015-11-03.
 */
public class DBUtil {
    private static final String DB_NAME = "power_lottery";

    public static final int VERSION = 1;

    private static DBUtil dbUtil;

    private SQLiteDatabase db;

    private DBUtil(Context context){
        DbHelper dbHelper = new DbHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static DBUtil instance(Context context){
        if(dbUtil == null){
            dbUtil = new DBUtil(context);
        }

        return dbUtil;
    }

    /**
     * save prize result into database.

     */
    public void savePrize(LotteryType lotteryType,List<PrizeResult> detail){
        for (PrizeResult result : detail){
            ContentValues values = new ContentValues();
            values.put("type",lotteryType.getName());
            values.put("phase",result.getPhase());
            values.put("result",result.getResult());
            values.put("total",result.getTotal());
            values.put("prizemoney",result.getPrizemoney());
            db.insert("Detail",null,values);
        }
    }

    public List<PrizeResult> getPrize(LotteryType lotteryType,String phase){
        List<PrizeResult> list = new ArrayList<PrizeResult>();
        Cursor cursor = db.query("Detail",null,"type = ? and phase = ?",new String[]{lotteryType.getName(),phase},null,null,"id asc");
        if(cursor.moveToFirst()){
            do{
                PrizeResult result = new PrizeResult();
                result.setPhase(Integer.parseInt(phase));
                result.setPrizemoney(cursor.getInt(cursor.getColumnIndex("prizemoney")));
                result.setResult(cursor.getString(cursor.getColumnIndex("result")));
                result.setTotal(cursor.getInt(cursor.getColumnIndex("total")));
                result.setType(cursor.getString(cursor.getColumnIndex("type")));
                list.add(result);
            }while (cursor.moveToNext());

            if(cursor != null){
                cursor.close();
            }
        }
        return list;
    }

}

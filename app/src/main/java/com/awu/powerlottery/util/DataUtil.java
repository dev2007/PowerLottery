package com.awu.powerlottery.util;

import com.awu.powerlottery.R;
import com.awu.powerlottery.app.PowerLotteryApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by awu on 2015-10-19.
 */
public class DataUtil {

    /**
     * Create data for listView menu.
     * @return
     */
    public static List<Map<String,Object>> getMenuData(){
        ArrayList<Map<String, Object>> menuData = new ArrayList<Map<String, Object>>();
        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("img", R.mipmap.ssq);
        map.put("title", getResourceStr(R.string.lottery_name_ssq));
        map.put("id",R.string.lottery_name_ssq);
        menuData.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.mipmap.fc3d);
        map.put("title", getResourceStr(R.string.lottery_name_3d));
        map.put("id",R.string.lottery_name_3d);
        menuData.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.mipmap.qlc);
        map.put("title", getResourceStr(R.string.lottery_name_qlc));
        map.put("id",R.string.lottery_name_qlc);
        menuData.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.mipmap.dlt);
        map.put("title", getResourceStr(R.string.lottery_name_dlt));
        map.put("id",R.string.lottery_name_dlt);
        menuData.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.mipmap.pl);
        map.put("title", getResourceStr(R.string.lottery_name_pl));
        map.put("id",R.string.lottery_name_pl);
        menuData.add(map);

        map = new HashMap<String,Object>();
        map.put("img",R.mipmap.qxc);
        map.put("title", getResourceStr(R.string.lottery_name_qxc));
        map.put("id",R.string.lottery_name_qxc);
        menuData.add(map);

        return  menuData;
    }

    private static String getResourceStr(int id){
        return PowerLotteryApplication.appContext().getResources().getString(id);
    }
}

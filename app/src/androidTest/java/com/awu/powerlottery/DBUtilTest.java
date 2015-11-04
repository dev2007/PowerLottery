package com.awu.powerlottery;

import android.test.AndroidTestCase;

import com.awu.powerlottery.app.PowerLotteryApplication;
import com.awu.powerlottery.db.DBUtil;
import com.awu.powerlottery.entity.PrizeResult;
import com.awu.powerlottery.util.LotteryType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by awu on 2015-11-03.
 */
public class DBUtilTest extends AndroidTestCase {

    public void testDBInsert(){
        List<PrizeResult> list = new ArrayList<>();
        PrizeResult p1 = new PrizeResult();
        p1.setType("ssq");
        p1.setTotal(100);
        p1.setResult("1,2,3,4,5,6,7");
        p1.setPrizemoney(1000);
        p1.setPhase(123);
        list.add(p1);
        DBUtil util = DBUtil.instance(PowerLotteryApplication.appContext());
        util.savePrize(LotteryType.SHUANGSEQIU,list);

        List<PrizeResult> list2 =  util.getPrize(LotteryType.SHUANGSEQIU, "123");
        assertEquals(1,list.size());
        assertEquals(123,list2.get(0).getPhase());
    }
}

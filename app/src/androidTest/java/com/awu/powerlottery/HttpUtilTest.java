package com.awu.powerlottery;

import android.test.AndroidTestCase;

import com.awu.powerlottery.entity.LotteryResult;
import com.awu.powerlottery.util.HttpUtil;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by awu on 2015-10-15.
 */
public class HttpUtilTest extends AndroidTestCase {
    private CountDownLatch countDownLatch = new CountDownLatch(1);
    private CountDownLatch newDownLatch = new CountDownLatch(1);

    private String url = "http://baidu.lecai.com/lottery/draw/ajax_get_detail.php?lottery_type=50&phase=2015122";
    private String newUrl = "http://baidu.lecai.com/lottery/ajax_current.php?lottery_type=50";
    public void testRequest(){
        HttpUtil.sendHttpRequest(url, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Map<String,Object> result = LotteryResult.parseShuangseqiu(response);
                assertEquals("05,07,11,16,22,25,07",result.get("result"));
                assertEquals("2015-10-18 21:15:00", result.get("enddate"));
                ArrayList<Map<String,Object>> prizeList = (ArrayList<Map<String,Object>>)result.get("prize");
                Map<String,Object> row = prizeList.get(0);
                assertEquals("prize1145486082一等奖",(String)row.get("key")+row.get("bet")+row.get("prize")+row.get("name"));
                countDownLatch.countDown();
            }

            @Override
            public void onError(Exception e) {
                fail();
                countDownLatch.countDown();
            }
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testNewLotteryRequest(){
        HttpUtil.sendHttpRequest(newUrl, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                assertNotNull(response);
                newDownLatch.countDown();
            }

            @Override
            public void onError(Exception e) {
                fail();
                newDownLatch.countDown();
            }
        });

        try {
            newDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}

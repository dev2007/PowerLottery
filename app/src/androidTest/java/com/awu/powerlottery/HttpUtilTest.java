package com.awu.powerlottery;

import android.test.AndroidTestCase;

import com.awu.powerlottery.entity.LotteryResult;
import com.awu.powerlottery.util.HttpUtil;

import java.util.concurrent.CountDownLatch;

/**
 * Created by awu on 2015-10-15.
 */
public class HttpUtilTest extends AndroidTestCase {
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private String url = "http://baidu.lecai.com/lottery/draw/ajax_get_detail.php?lottery_type=50&phase=2015120";
    public void testRequest(){
        HttpUtil.sendHttpRequest(url, new HttpUtil.HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                assertEquals("16,21,24,26,27,29,16",LotteryResult.parseShuangseqiu(response));
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
}

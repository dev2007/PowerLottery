package com.awu.powerlottery.bl;

import com.awu.powerlottery.util.LotteryType;

/**
 * Created by awu on 2015-12-10.
 */
public interface IAutoNotifyListener {
    void sendNotification(LotteryType lotteryType);
}

package com.awu.powerlottery.handler;

import android.os.Handler;
import android.os.Message;

import com.awu.powerlottery.bl.IAutoNotifyListener;
import com.awu.powerlottery.util.LotteryType;

/**
 * Created by awu on 2015-12-10.
 */
public class NotifyHandler extends Handler {
    public static final int MSG_OK = 1;
    public static final int MSG_FAIL = -1;
    private IAutoNotifyListener mListener;
    private LotteryType mLotteryType;

    public NotifyHandler(IAutoNotifyListener listener,LotteryType lotteryType) {
        this.mListener = listener;
        this.mLotteryType = lotteryType;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_OK:
                if (mListener != null) {
                    mListener.sendNotification(mLotteryType);
                }
                break;
            case MSG_FAIL:
                break;
            default:
                break;
        }
    }
}

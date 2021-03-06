package com.awu.powerlottery.handler;

import android.os.Handler;
import android.os.Message;

import com.awu.powerlottery.bl.IQueryDataListener;

/**
 * Created by awu on 2015-10-22.
 */
public class QueryDataHandler extends Handler {
    public static final int MSG_OK = 1;
    public static final int MSG_FAIL = -1;
    private IQueryDataListener listener;

    public QueryDataHandler(IQueryDataListener listener){
        super();
        this.listener = listener;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_OK:
                if(listener != null)
                    listener.getDataOK();
                break;
            case MSG_FAIL:
                if(listener != null)
                    listener.getDataFail();
                break;
            default:
                break;
        }
    }
}

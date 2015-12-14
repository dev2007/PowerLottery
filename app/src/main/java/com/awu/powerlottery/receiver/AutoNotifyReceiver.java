package com.awu.powerlottery.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.awu.powerlottery.app.Definition;
import com.awu.powerlottery.service.AutoNotifyService;

/**
 * Created by awu on 2015-12-10.
 */
public class AutoNotifyReceiver extends BroadcastReceiver {
    private final String TAG = "AutoNotifyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG,"receiver get");
        Intent i = new Intent(context, AutoNotifyService.class);
        i.putExtra(Definition.LotteryType,intent.getIntExtra(Definition.LotteryType,0));
        context.startService(i);
    }
}

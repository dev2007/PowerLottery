package com.awu.powerlottery.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.awu.powerlottery.R;
import com.awu.powerlottery.activity.MainActivity;
import com.awu.powerlottery.app.Definition;
import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.bl.IAutoNotifyListener;
import com.awu.powerlottery.handler.NotifyHandler;
import com.awu.powerlottery.receiver.AutoNotifyReceiver;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.util.Utility;
import com.awu.powerlottery.util.WebUtility;

/**
 * Created by awu on 2015-12-10.
 */
public class AutoNotifyService extends Service implements IAutoNotifyListener {
    /**
     * TAG
     */
    private final String TAG = "AutoNotifyService";

    /**
     * Lottery type for query.
     */
    public static LotteryType lotteryType = LotteryType.DEFAULT;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        Log.e(TAG,"service start");
        int value = 0;
        if(intent != null) {
            value = intent.getIntExtra("LotteryType", 0);
        }else{
            Log.e(TAG,"service intent is null");
        }
        if(value != 0){
            Log.i(TAG,"start service:"+value);
            lotteryType = LotteryType.valueOf(value);
            WebUtility.queryNewLottery(lotteryType,new NotifyHandler(this,lotteryType));

            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            int anHour = 1 * 60 * 1 * 1000;
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
            Intent i = new Intent(this, AutoNotifyReceiver.class);
            i.putExtra(Definition.LotteryType,value);
            PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }

        return super.onStartCommand(intent,flags,startId);
    }

    /**
     * send notification.
     */
    @Override
    public void sendNotification(LotteryType lotteryType) {
        //if it's old phase,not notify user.
        if(Utility.compareCurrentDate(DataLayer.getPhaseDate(lotteryType))){
            Log.e(TAG,"old phase,not notify");
            return;
        }
        Log.e(TAG,"notify:"+lotteryType.getName());
        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra(Definition.LotteryType,lotteryType.getValue());
        PendingIntent pi = PendingIntent.getActivity(this,0,viewIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(getString(R.string.tip_notify_title))
                .setContentTitle(getString(R.string.tip_notify_title))
                .setContentText(getString(R.string.tip_notify_text))
                .setContentIntent(pi)
                .setNumber(1).getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, notification);
    }
}

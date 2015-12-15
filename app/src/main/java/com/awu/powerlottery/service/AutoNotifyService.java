package com.awu.powerlottery.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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

import java.util.Calendar;

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
            WebUtility.queryNewLottery(lotteryType, new NotifyHandler(this, lotteryType));

            AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
            int anHour = calcNextQuery(lotteryType);
            long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
            Intent i = new Intent(this, AutoNotifyReceiver.class);
            i.putExtra(Definition.LotteryType,value);
            PendingIntent pi = PendingIntent.getBroadcast(this,0,i,0);
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }

        return super.onStartCommand(intent,flags,startId);
    }

    private  int calcNextQuery(LotteryType lotteryType){
        int phase = 0;
        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        switch (lotteryType){
            case SHUANGSEQIU:
                if(weekDay == 3 || weekDay == 5 || weekDay == 7){
                    if(hour < 21 || hour == 21 && minute < 45){
                        Log.i(TAG,"SSQ weekday"+weekDay + " if 1");
                        phase = ((21 - hour) * 60 + 45 - minute) * 60 * 1000;
                    }else{
                        if(weekDay == 7){
                            Log.i(TAG,"SSQ weekday"+weekDay + " if 2");
                            phase = ((23 - hour) * 60 + 60 - minute + 24 * 60 * 2 + 21 * 60 + 45) * 60 * 1000;
                        }else{
                            Log.i(TAG,"SSQ weekday"+weekDay + " if 3");
                            phase = ((23 - hour) * 60 + 60 - minute + 24 * 60 + 21 * 60 + 45) * 60 * 1000;
                        }
                    }
                }
                break;
            case DALETOU:
                if(weekDay == 2 || weekDay == 4 || weekDay == 7){
                    if(hour < 21 || hour == 21 && minute < 0){
                        Log.i(TAG,"dlt weekday"+weekDay + " if 1");
                        phase = ((20 - hour) * 60 + 60 - minute) * 60 * 1000;
                    }else{
                        if(weekDay == 4){
                            Log.i(TAG,"dlt weekday"+weekDay + " if 2");
                            phase = ((23 - hour) * 60 + 60 - minute + 24 * 60 * 2 + 21 * 60 + 0) * 60 * 1000;
                        }else{
                            Log.i(TAG,"dlt weekday"+weekDay + " if 3");
                            phase = ((23 - hour) * 60 + 60 - minute + 24 * 60 + 21 * 60 + 0) * 60 * 1000;
                        }
                    }
                }
        }

        return phase;
    }

    /**
     * send notification.
     */
    @Override
    public void sendNotification(LotteryType lotteryType) {
        //if it's old phase,not notify user.
        if(!checkCanNotify(lotteryType)){
            Log.e(TAG,"old phase,not notify");
            return;
        }
        Log.e(TAG, "notify:" + lotteryType.getName());
        Intent viewIntent = new Intent(this, MainActivity.class);
        viewIntent.putExtra(Definition.LotteryType,lotteryType.getValue());
        PendingIntent pi = PendingIntent.getActivity(this,0,viewIntent,PendingIntent.FLAG_CANCEL_CURRENT);

        String lottery = lotteryType == LotteryType.SHUANGSEQIU ? getString(R.string.lottery_name_ssq) : getString(R.string.lottery_name_dlt);
        String content = lottery + getString(R.string.tip_notify_text);
        String title = lottery + getString(R.string.tip_notify_title);

        NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker(title)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pi)
                .setNumber(1).getNotification();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        nm.notify(1, notification);
    }

    private boolean checkCanNotify(LotteryType lotteryType){
        Calendar calendar = Calendar.getInstance();
        int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
        int minuteNow = calendar.get(Calendar.MINUTE);
        boolean flag = false;
        switch (lotteryType){
            case SHUANGSEQIU:
                if(Utility.afterCurrentDate(DataLayer.getPhaseDate(lotteryType))
                        && (Utility.todayWeekDay() == 2 || Utility.todayWeekDay() == 4 || Utility.todayWeekDay() == 7)
                        && (hourNow <= 22 && hourNow > 21 || hourNow == 21 && minuteNow >= 45) )
                    flag = true;
                else
                    flag =  false;
                default:
                    break;
        }
        return true;
    }

}

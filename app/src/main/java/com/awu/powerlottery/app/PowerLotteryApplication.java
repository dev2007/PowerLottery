package com.awu.powerlottery.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by awu on 2015-10-15.
 */
public class PowerLotteryApplication extends Application {
    private static Context mContext;
    @Override
    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
    }

    /**
     * Get application context.
     * @return
     */
    public static Context appContext(){
        return mContext;
    }
}

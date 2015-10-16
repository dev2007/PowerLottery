package com.awu.powerlottery.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity collector.
 * Created by awu on 2015-10-16.
 */
public class ActivityCollector {
    private static List<Activity> activities = new ArrayList<Activity>();

    /**
     * add activity into collector.
     * @param activity
     */
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    /**
     * remove activity from collector.
     * @param activity
     */
    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    /**
     * finish all activities in collector.
     */
    public static void finishAll(){
        for (Activity activity : activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }
}

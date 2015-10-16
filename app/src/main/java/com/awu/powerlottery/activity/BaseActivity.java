package com.awu.powerlottery.activity;

import android.app.Activity;
import android.os.Bundle;

import com.awu.powerlottery.app.ActivityCollector;

/**
 * Created by awu on 2015-10-16.
 */
public abstract class BaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

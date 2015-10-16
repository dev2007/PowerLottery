package com.awu.powerlottery.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awu.powerlottery.R;

/**
 * Created by awu on 2015-10-15.
 */
public class DisplayFragment extends Fragment {

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.lottery_layout,container,false);
    }
}

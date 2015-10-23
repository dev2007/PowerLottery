package com.awu.powerlottery.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.awu.powerlottery.R;
import com.awu.powerlottery.util.LotteryType;

/**
 * Created by awu on 2015-10-20.
 */
public class Display3DFragment extends BaseFragment {

    public Display3DFragment(){
        super();
        setLayout(R.layout.layout_lottery_3d, LotteryType.FUCAI3D);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {

    }


    @Override
    protected void organizePhaseData() {

    }


    @Override
    protected void requestOthersOK() {
        super.requestOthersOK();
    }

    @Override
    protected void organizeOthersData() {

    }

}

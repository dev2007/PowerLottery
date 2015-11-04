package com.awu.powerlottery.activity;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.awu.powerlottery.R;
import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.util.Utility;
import com.awu.powerlottery.view.ImageText;

/**
 * Created by awu on 2015-10-20.
 */
public class Display3DFragment extends BaseFragment {

    private ImageText[] ballButtonArray = new ImageText[3];

    public Display3DFragment(){
        super();
        setLayout(R.layout.layout_lottery_3d, LotteryType.FUCAI3D);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        ballButtonArray[0] = (ImageText)contentView.findViewById(R.id.iv_b1);
        ballButtonArray[1] = (ImageText)contentView.findViewById(R.id.iv_b2);
        ballButtonArray[2] = (ImageText)contentView.findViewById(R.id.iv_b3);
    }


    @Override
    protected void requestOthersOK() {
        super.requestOthersOK();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewLotterydate.setText(DataLayer.pullPhaseDate(lotteryType));
                String[] prizeResult = DataLayer.pullPrizeResult(lotteryType);
                if(prizeResult.length == ballButtonArray.length){
                    for (int i = 0;i < prizeResult.length;i++){
                        ballButtonArray[i].setText(prizeResult[i]);
                    }
                }
            }
        });
    }

}

package com.awu.powerlottery.activity;

import android.os.Bundle;
import android.util.Log;

import com.awu.powerlottery.R;
import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.view.ImageText;

/**
 * Created by awu on 2015-11-16.
 */
public class DisplayPL3Fragment extends BaseFragment {
    private static String TAG = "DisplayPL3Fragment";

    private ImageText[] ballButtonArray = new ImageText[3];

    public DisplayPL3Fragment() {
        super();
        setLayout(R.layout.layout_lottery_pl3, LotteryType.PAILEI3);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        ballButtonArray[0] = (ImageText) contentView.findViewById(R.id.iv_b1);
        ballButtonArray[1] = (ImageText) contentView.findViewById(R.id.iv_b2);
        ballButtonArray[2] = (ImageText) contentView.findViewById(R.id.iv_b3);
    }

    @Override
    protected void requestOthersOK() {
        super.requestOthersOK();
        Log.i(TAG, "pl3 requestothers ok");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewLotterydate.setText(DataLayer.pullPhaseDate(lotteryType));
                String[] prizeResult = DataLayer.pullPrizeResult(lotteryType);
                for (int i = 0; i < prizeResult.length; i++) {
                    ballButtonArray[i].setText("" + prizeResult[i]);
                }
            }
        });
    }
}

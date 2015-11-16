package com.awu.powerlottery.activity;

import android.os.Bundle;
import android.util.Log;

import com.awu.powerlottery.R;
import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.view.ImageText;

/**
 * Created by awu on 2015-11-10.
 */
public class DisplayDLTFragment extends BaseFragment {
    private static String TAG = "DisplayDLTFragment";

    private ImageText[] ballButtonArray = new ImageText[7];

    public DisplayDLTFragment() {
        super();
        setLayout(R.layout.layout_lottery_dlt, LotteryType.DALETOU);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        ballButtonArray[0] = (ImageText) contentView.findViewById(R.id.iv_b1);
        ballButtonArray[1] = (ImageText) contentView.findViewById(R.id.iv_b2);
        ballButtonArray[2] = (ImageText) contentView.findViewById(R.id.iv_b3);
        ballButtonArray[3] = (ImageText) contentView.findViewById(R.id.iv_b4);
        ballButtonArray[4] = (ImageText) contentView.findViewById(R.id.iv_b5);
        ballButtonArray[5] = (ImageText) contentView.findViewById(R.id.iv_b6);
        ballButtonArray[6] = (ImageText) contentView.findViewById(R.id.iv_b7);
    }

    @Override
    protected void requestOthersOK() {
        super.requestOthersOK();
        Log.i(TAG, "dlt requestothers ok");
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

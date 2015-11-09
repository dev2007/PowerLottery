package com.awu.powerlottery.activity;

import android.os.Bundle;
import android.util.Log;

import com.awu.powerlottery.R;
import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.view.ImageText;

/**
 * Created by awu on 2015-11-09.
 */
public class DipslayQLCFragment extends BaseFragment {
    private  static String TAG = "DisplayQLCFragment";

    private ImageText[] ballButtonArray = new ImageText[8];

    public DipslayQLCFragment(){
        super();
        setLayout(R.layout.layout_lottery_qlc, LotteryType.QILECAI);
    }

    @Override
    protected void onInit(Bundle savedInstanceState) {
        ballButtonArray[0] = (ImageText)contentView.findViewById(R.id.iv_b1);
        ballButtonArray[1] = (ImageText)contentView.findViewById(R.id.iv_b2);
        ballButtonArray[2] = (ImageText)contentView.findViewById(R.id.iv_b3);
        ballButtonArray[3] = (ImageText)contentView.findViewById(R.id.iv_b4);
        ballButtonArray[4] = (ImageText)contentView.findViewById(R.id.iv_b5);
        ballButtonArray[5] = (ImageText)contentView.findViewById(R.id.iv_b6);
        ballButtonArray[6] = (ImageText)contentView.findViewById(R.id.iv_b7);
        ballButtonArray[7] = (ImageText)contentView.findViewById(R.id.iv_b8);
    }

    @Override
    protected void requestOthersOK(){
        super.requestOthersOK();
        Log.i(TAG, "qlc requestothers ok");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewLotterydate.setText(DataLayer.pullPhaseDate(lotteryType));
                String[] prizeResult = DataLayer.pullPrizeResult(lotteryType);
                if (prizeResult.length == 8)
                    for (int i = 0; i < prizeResult.length; i++) {
                        ballButtonArray[i].setText("" + prizeResult[i]);
                    }
            }
        });
    }
}

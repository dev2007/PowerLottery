package com.awu.powerlottery.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.awu.powerlottery.R;
import com.awu.powerlottery.adapter.PrizeGridArrayAdapter;
import com.awu.powerlottery.bl.IQueryDataListener;
import com.awu.powerlottery.bl.IQueryLatestListener;
import com.awu.powerlottery.handler.QueryDataHandler;
import com.awu.powerlottery.handler.QueryLatestHandler;
import com.awu.powerlottery.util.LotteryType;
import com.awu.powerlottery.util.PreferencesUtil;
import com.awu.powerlottery.util.Utility;
import com.awu.powerlottery.util.WebUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by awu on 2015-10-15.
 */
public class DisplaySSQFragment extends BaseFragment {
    private static final String TAG = "DisplaySSQFragment";




    private Button[] ballList = new Button[7];

    private  int currentPosition;

    public DisplaySSQFragment(){
        super();
        setLayout(R.layout.layout_lottery_ssq,LotteryType.SHUANGSEQIU);
    }

    @Override
    protected void onInit(Bundle savedInstanceState){
        ballList[0] = (Button)contentView.findViewById(R.id.iv_b1);
        ballList[1] = (Button)contentView.findViewById(R.id.iv_b2);
        ballList[2] = (Button)contentView.findViewById(R.id.iv_b3);
        ballList[3] = (Button)contentView.findViewById(R.id.iv_b4);
        ballList[4] = (Button)contentView.findViewById(R.id.iv_b5);
        ballList[5] = (Button)contentView.findViewById(R.id.iv_b6);
        ballList[6] = (Button)contentView.findViewById(R.id.iv_b7);
    }



    @Override
    protected void organizePhaseData() {
        int phase = Utility.getPhase(lotteryType);
        if (phase != 0) {
            for (int i = 0; i < m_arr.length; i++) {
                m_arr[i] = "" + phase;
                phase--;
            }
        }
    }

    @Override
    protected void requestOthersOK(){
        super.requestOthersOK();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewLotterydate.setText(Utility.getSSQPrizeDate());
                String[] prizeResult = Utility.getSSQResult();
                if (prizeResult.length == 7)
                    for (int i = 0; i < prizeResult.length; i++) {
                        ballList[i].setText("" + prizeResult[i]);
                    }
            }
        });
    }

    @Override
    protected void organizeOthersData(){
        String[] prize = Utility.getSSQPrize();
        gridArr = new String[prize.length];
        for (int i = 0;i < gridArr.length;i++){
            gridArr[i] = prize[i];
        }
    }



}

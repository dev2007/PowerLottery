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

    private Spinner periodSpinner;
    private ArrayAdapter spinnerAdapter;
    private TextView textViewLotterydate;
    private GridView gridViewPrize;

    private static final String[] m_arr = new String[30];
    private static String[] gridArr;
    private PrizeGridArrayAdapter gridArrayAdapter;
    private Button[] ballList = new Button[7];

    private  int currentPosition;

    public DisplaySSQFragment(){
        super();
        setLayout(R.layout.layout_lottery_ssq);
    }

    @Override
    protected void onInit(Bundle savedInstanceState){
        periodSpinner = (Spinner) contentView.findViewById(R.id.spinner_period);
        textViewLotterydate = (TextView)contentView.findViewById(R.id.tv_lotterydate);
        gridViewPrize = (GridView)contentView.findViewById(R.id.gv_prize);
        ballList[0] = (Button)contentView.findViewById(R.id.iv_b1);
        ballList[1] = (Button)contentView.findViewById(R.id.iv_b2);
        ballList[2] = (Button)contentView.findViewById(R.id.iv_b3);
        ballList[3] = (Button)contentView.findViewById(R.id.iv_b4);
        ballList[4] = (Button)contentView.findViewById(R.id.iv_b5);
        ballList[5] = (Button)contentView.findViewById(R.id.iv_b6);
        ballList[6] = (Button)contentView.findViewById(R.id.iv_b7);
    }

    @Override
    protected void requestPhase() {
        String date = (String) PreferencesUtil.getData(Utility.KEY_NEW_DATE, "2014-07-07 00:01");
        Log.i(TAG, "loadDefaultData date:" + date);
        //newest has not result
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if (Utility.compareCurrentDateTime(date)) {
            Log.i(TAG,"load default data.");
            queryLatestHandler.sendEmptyMessage(QueryLatestHandler.MSG_OK);
        } else {
            showProgressDialog(true);
            WebUtility.queryNewLottery(LotteryType.SHUANGSEQIU, queryLatestHandler);
        }
    }

    @Override
    protected void setSpinner() {
        processListItem();
        spinnerAdapter = new ArrayAdapter(contentView.getContext(), android.R.layout.simple_list_item_1, m_arr);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(spinnerAdapter);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                queryLottery(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void processListItem() {
        int phase = (int) PreferencesUtil.getData(Utility.KEY_PHASE, 0);
        if (phase != 0) {
            for (int i = 0; i < m_arr.length; i++) {
                m_arr[i] = "" + phase;
                phase--;
            }
        }
    }

    @Override
    protected void setOthers(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textViewLotterydate.setText(Utility.getSSQPrizeDate());
                initGridArray();
                gridArrayAdapter = new PrizeGridArrayAdapter(contentView.getContext(),R.layout.layout_prize_cell,gridArr);
                gridViewPrize.setAdapter(gridArrayAdapter);
                String[] prizeResult = Utility.getSSQResult();
                if(prizeResult.length == 7)
                for (int i = 0;i < prizeResult.length;i++){
                    ballList[i].setText(""+prizeResult[i]);
                }
            }
        });
    }

    private void initGridArray(){
        String[] prize = Utility.getSSQPrize();
        gridArr = new String[prize.length];
        for (int i = 0;i < gridArr.length;i++){
            gridArr[i] = prize[i];
        }
    }

    private void queryLottery(int position){
        currentPosition = position;
        requestOthers();
    }

    @Override
    protected   void requestOthers(){
        Toast.makeText(contentView.getContext(), m_arr[currentPosition], Toast.LENGTH_SHORT).show();
        showProgressDialog(true);
        WebUtility.queryLottery(LotteryType.SHUANGSEQIU, m_arr[currentPosition], queryDataHandler);
    }
}

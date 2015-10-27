package com.awu.powerlottery.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
 * Created by awu on 2015-10-23.
 */
public abstract class BaseFragment extends Fragment implements IQueryLatestListener, IQueryDataListener {
    private static final String TAG = "BaseFragment";
    private ProgressDialog progressDialog = null;
    protected View contentView;
    private int mLayoutId;
    protected LotteryType lotteryType;
    protected QueryDataHandler queryDataHandler = new QueryDataHandler(this);
    protected QueryLatestHandler queryLatestHandler = new QueryLatestHandler(this);


    protected Spinner periodSpinner;
    protected ArrayAdapter spinnerAdapter;
    protected static String[] m_arr = new String[30];

    protected TextView textViewLotterydate;

    protected LinearLayout linearLayoutPhase;
    protected LinearLayout linearLayoutPhaseDate;
    protected LinearLayout linearLayoutBall;

    protected GridView gridViewPrize;
    protected static String[] gridArr;
    private PrizeGridArrayAdapter gridArrayAdapter;

    protected Button buttonRandom;

    public BaseFragment() {
        super();
    }

    protected void setLayout(int resouceId,LotteryType lotteryType) {
        this.mLayoutId = resouceId;
        this.lotteryType = lotteryType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(mLayoutId, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInitView();
        onInit(savedInstanceState);
        requestPhase();
    }

    /**
     * initialize common views.
     */
    private void onInitView() {
        textViewLotterydate = (TextView)contentView.findViewById(R.id.tv_lotterydate);
        periodSpinner = (Spinner) contentView.findViewById(R.id.spinner_period);
        linearLayoutPhase = (LinearLayout) contentView.findViewById(R.id.line_phase);
        linearLayoutPhaseDate = (LinearLayout) contentView.findViewById(R.id.line_phasedate);
        linearLayoutBall = (LinearLayout) contentView.findViewById(R.id.line_ball);
        gridViewPrize = (GridView) contentView.findViewById(R.id.gv_prize);
        buttonRandom = (Button) contentView.findViewById(R.id.btn_random);
    }

    /**
     * subclass for initialize views.
     *
     * @param savedInstanceState
     */
    protected abstract void onInit(Bundle savedInstanceState);


    /**
     * subclass request phase data.
     */
    protected void requestPhase(){
        setPhaseVisible(false);
        String date = Utility.getPhaseDate(lotteryType);
        //newest has not result
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        if (Utility.compareCurrentDateTime(date)) {
            queryLatestHandler.sendEmptyMessage(QueryLatestHandler.MSG_OK);
        } else {
            showProgressDialog(true);
            WebUtility.queryNewLottery(lotteryType, queryLatestHandler);
        }
    }

    protected void showProgressDialog(boolean show) {
        if (show) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            } else {
                progressDialog = new ProgressDialog(contentView.getContext());
                progressDialog.setMessage(getResources().getString(R.string.msg_query));
                progressDialog.show();
            }
        } else {
            if (progressDialog != null) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }
    }

    /**
     * subclass set spinner after get phase data.
     */
    protected void requestPhaseOK(){
        setPhaseVisible(true);
        m_arr = new String[30];
        organizePhaseData();
        spinnerAdapter = new ArrayAdapter(contentView.getContext(), android.R.layout.simple_list_item_1, m_arr);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(spinnerAdapter);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                requestOthers(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * organize phase data.
     */
    private void organizePhaseData() {
        int phase = Utility.getPhase(lotteryType);
        if (phase != 0) {
            for (int i = 0; i < m_arr.length; i++) {
                m_arr[i] = "" + phase;
                phase--;
            }
        }
    }



    /**
     * request prize data.
     */
    private void requestOthers(int position){
        setOthersVisible(false);
        showProgressDialog(true);
        WebUtility.queryLottery(lotteryType, m_arr[position], queryDataHandler);
    }


    /**
     * subclass set other views after get prize data.
     */
    protected   void requestOthersOK(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setOthersVisible(true);
                organizeOthersData();
                gridArrayAdapter = new PrizeGridArrayAdapter(contentView.getContext(), R.layout.layout_prize_cell, gridArr);
                gridViewPrize.setAdapter(gridArrayAdapter);
            }
        });
    }

    /***
     * organize others data.
     */
    private void organizeOthersData(){
        String[] prize = Utility.pullPrize(lotteryType);
        gridArr = new String[prize.length];
        for (int i = 0;i < gridArr.length;i++){
            gridArr[i] = prize[i];
        }
    }

    @Override
    public void getDataOK() {
        showProgressDialog(false);
        requestOthersOK();
    }

    @Override
    public void getDataFail() {
        showProgressDialog(false);
    }

    @Override
    public void getLatestOK() {
        requestPhaseOK();
    }

    @Override
    public void getLatestFail() {
        showProgressDialog(false);
        Toast.makeText(contentView.getContext(), "加载数据失败", Toast.LENGTH_SHORT).show();
    }

    /**
     * set phase view visible.
     *
     * @param visible
     */
    protected void setPhaseVisible(boolean visible) {
        if (linearLayoutPhase != null) {
            linearLayoutPhase.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        if (linearLayoutPhaseDate != null) {
            linearLayoutPhaseDate.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }

    /**
     * set others view visible.
     *
     * @param visible
     */
    protected void setOthersVisible(boolean visible) {
        if (linearLayoutBall != null) {
            linearLayoutBall.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        if (gridViewPrize != null) {
            gridViewPrize.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        if (buttonRandom != null) {
            buttonRandom.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
    }
}

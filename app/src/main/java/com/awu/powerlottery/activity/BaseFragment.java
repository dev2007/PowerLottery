package com.awu.powerlottery.activity;

import android.app.Fragment;
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
import com.awu.powerlottery.app.CommonDialog;
import com.awu.powerlottery.bl.DataLayer;
import com.awu.powerlottery.bl.IQueryDataListener;
import com.awu.powerlottery.bl.IQueryLatestListener;
import com.awu.powerlottery.handler.QueryDataHandler;
import com.awu.powerlottery.handler.QueryLatestHandler;
import com.awu.powerlottery.util.LotteryType;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by awu on 2015-10-23.
 */
public abstract class BaseFragment extends Fragment implements IQueryLatestListener, IQueryDataListener {
    /**
     * Base Fragment TAG.
     */
    private static final String TAG = "BaseFragment";
    /**
     * CommonDialog for ProgressDialog
     */
    protected CommonDialog dialogHelper;
    /**
     * Fragment view.
     */
    protected View contentView;
    /**
     * Fragment layout resource id.
     */
    private int mLayoutId;
    /**
     * Subclass fragment's lottery type.
     */
    protected LotteryType lotteryType;
    /**
     * Query lottery data's handler.
     */
    protected QueryDataHandler queryDataHandler = new QueryDataHandler(this);
    /**
     * Query lottery phase's handler.
     */
    protected QueryLatestHandler queryLatestHandler = new QueryLatestHandler(this);
    /**
     * General Spinner for display lottery latest 30 phases.
     */
    protected Spinner periodSpinner;
    /**
     * General Spinner adapter.
     */
    protected ArrayAdapter spinnerAdapter;
    /**
     * General array for store latest 30 phases.
     */
    protected static String[] m_arr = new String[30];
    /**
     * General TextView for display lottery date.
     */
    protected TextView textViewLotterydate;
    /**
     * General LinearLayout for wrap current phase view.
     */
    protected LinearLayout linearLayoutPhase;
    /**
     * General LinearLayout for wrap current phase's date view.
     */
    protected LinearLayout linearLayoutPhaseDate;
    /**
     * General LinearLayout for wrap prize result ball view.
     */
    protected LinearLayout linearLayoutBall;
    /**
     * General GridView for display prize list.
     */
    protected GridView gridViewPrize;
    /**
     * General array for store prize list.
     */
    protected static String[] gridArr;
    /**
     * Adapter for GridView.
     */
    private PrizeGridArrayAdapter gridArrayAdapter;
    /**
     * General Button for function-random choice.
     */
    protected Button buttonRandom;

    /**
     * Constructor.
     */
    public BaseFragment() {
        super();
    }


    /**
     * Bind fragment view with layout resource id & set the sub fragment's lottery type.
     * @param resourceId Layout resource id.
     * @param lotteryType Fragment's lottery type.
     */
    protected void setLayout(int resourceId,LotteryType lotteryType) {
        this.mLayoutId = resourceId;
        this.lotteryType = lotteryType;
    }


    /**
     * Create view.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(mLayoutId, container, false);
        return contentView;
    }


    /**
     * Activity created.
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialogHelper = new CommonDialog(contentView.getContext());
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
     * 虚方法，子类需实现的 其他初始化
     *
     * @param savedInstanceState
     */
    protected abstract void onInit(Bundle savedInstanceState);


    /**
     * request phase data.
     * 请求期数数据。
     */
    protected void requestPhase(){
        setPhaseVisible(false);
        DataLayer.getNewLottery(lotteryType,queryLatestHandler,this);
    }


    /**
     * set spinner after get phase data.
     * 期数请求成功后的处理。主要是绑定期数选择器。
     * 子类如果需要其他设置，可重写。
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
     * 组织期数数据用于期数选择器。
     */
    private void organizePhaseData() {
        int phase = DataLayer.getPhase(lotteryType);
        if (phase != 0) {
            for (int i = 0; i < m_arr.length; i++) {
                m_arr[i] = "" + phase;
                phase--;
            }
        }
    }


    /**
     * request prize data.
     * 请求某期的获奖信息。
     */
    private void requestOthers(int position){
        setOthersVisible(false);
        showProgressDialog(true);
        Log.i(TAG, "Request data of phase:" + m_arr[position]);
        DataLayer.getLotteryResult(lotteryType, m_arr[position], queryDataHandler, this);
    }


    /**
     * set other views after get prize data.
     * 获取获取信息后的控件处理。
     */
    protected   void requestOthersOK(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setOthersVisible(true);
                organizeOthersData();
                gridArrayAdapter = new PrizeGridArrayAdapter(contentView.getContext(), R.layout.layout_prize_cell, gridArr, redLocation());
                gridViewPrize.setAdapter(gridArrayAdapter);
            }
        });
    }


    /**
     * set prize list somewhere text is red.
     * 设置获取列表中某些位置的文字是红色。
     * @return
     */
    private int[] redLocation(){
        switch (lotteryType){
            case SHUANGSEQIU:
                return new int[]{6,10};
            default:
                return  new int[]{};
        }
    }


    /***
     * organize others data.
     * 组织其他数据。主要为获取列表数据
     */
    private void organizeOthersData(){
        String[] prize = DataLayer.pullPrize(lotteryType);
        gridArr = new String[prize.length];
        for (int i = 0;i < gridArr.length;i++){
            gridArr[i] = prize[i];
        }
    }


    /**
     * get prize data ok callback.
     * 获取获奖数据成功后回调函数。
     */
    @Override
    public void getDataOK() {
        showProgressDialog(false);
        requestOthersOK();
    }


    /**
     * get prize data fail callback.
     * 获取获奖数据失败后回调函数。
     */
    @Override
    public void getDataFail() {
        showProgressDialog(false);
    }


    /**
     * get phase data ok callback.
     * 获取期数成功回调函数。
     */
    @Override
    public void getLatestOK() {
        requestPhaseOK();
    }


    /**
     * get phase fail callback.
     * 获取期数失败回调函数。
     */
    @Override
    public void getLatestFail() {
        showProgressDialog(false);
        Toast.makeText(contentView.getContext(), "加载数据失败", Toast.LENGTH_SHORT).show();
    }


    /**
     * set phase view visible.
     * 设置期数控件可见性。
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
     * 设置其他控件可见性。
     * @param visible
     */
    protected void setOthersVisible(boolean visible) {
        if (linearLayoutBall != null) {
            linearLayoutBall.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        if (gridViewPrize != null) {
            gridViewPrize.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        }
        //TODO:下版增加功能后再放开。20151130 awu
//        if (buttonRandom != null) {
//            buttonRandom.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
//        }
    }


    /**
     * set progress dialog visible.
     * 设置进度框可见性。
     * @param show
     */
    public void showProgressDialog(boolean show){
        dialogHelper.show(show);
    }


    @Override
    public void onResume(){
        super.onResume();
        //Umeng statistics functioin.
        MobclickAgent.onPageStart(lotteryType.getName());
    }


    @Override
    public void onPause(){
        super.onPause();
        //Umeng statistics functioin.
        MobclickAgent.onPageEnd(lotteryType.getName());
    }
}

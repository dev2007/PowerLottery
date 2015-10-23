package com.awu.powerlottery.activity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.awu.powerlottery.R;
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
public class BaseFragment extends Fragment implements IQueryLatestListener,IQueryDataListener {
    private static final String TAG = "BaseFragment";
    private ProgressDialog progressDialog = null;
    protected View contentView;
    private int mLayoutId;
    protected QueryDataHandler queryDataHandler = new QueryDataHandler(this);
    protected QueryLatestHandler queryLatestHandler = new QueryLatestHandler(this);

    public BaseFragment(){
        super();
    }

    protected void setLayout(int resouceId){
        this.mLayoutId = resouceId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(mLayoutId, container, false);
        return contentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInit(savedInstanceState);
        requestPhase();
    }

    /**
     * subclass for initialize views.
     * @param savedInstanceState
     */
    protected void onInit(Bundle savedInstanceState){

    }

    /**
     * subclass request phase data.
     */
    protected void requestPhase(){

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
    protected  void setSpinner(){

    }


    /**
     * subclass request prize data.
     */
    protected  void requestOthers(){

    }

    /**
     * subclass set other views after get prize data.
     */
    protected  void setOthers(){

    }

    @Override
    public void getDataOK() {
        showProgressDialog(false);
        setOthers();
    }

    @Override
    public void getDataFail() {
        showProgressDialog(false);
    }

    @Override
    public void getLatestOK() {
        setSpinner();
    }

    @Override
    public void getLatestFail() {
        showProgressDialog(false);
        Toast.makeText(contentView.getContext(), "加载数据失败", Toast.LENGTH_SHORT).show();
    }
}

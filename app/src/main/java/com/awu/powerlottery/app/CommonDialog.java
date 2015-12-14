package com.awu.powerlottery.app;

import android.app.ProgressDialog;
import android.content.Context;

import com.awu.powerlottery.R;

/**
 * General ProgressDialog class for Lottery Fragment.
 *
 * Created by awu on 2015-11-03.
 */
public class CommonDialog {
    private ProgressDialog progressDialog = null;
    private Context mContext;
    /**
     * Constructor.
     */
    public CommonDialog(Context context){
        this.mContext = context;
    }

    /**
     * Show ProgressDialog or dismiss it.
     * @param isShow True:display dialog. False: dismiss dialog if it's display.
     */
    public void show(boolean isShow) {
        if (isShow) {
            if (progressDialog != null) {
                if (!progressDialog.isShowing())
                    progressDialog.show();
            } else {
                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage(mContext.getResources().getString(R.string.msg_query));
                progressDialog.show();
            }
        } else {
            if (progressDialog != null) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }
    }
}

package com.awu.powerlottery.app;

import android.app.ProgressDialog;
import android.content.Context;

import com.awu.powerlottery.R;

/**
 * Created by awu on 2015-11-03.
 */
public class CommonDialog {
    private ProgressDialog progressDialog = null;
    private Context mContext;
    public CommonDialog(Context context){
        this.mContext = context;
    }

    public void showProgressDialog(boolean show) {
        if (show) {
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

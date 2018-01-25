package com.android.update;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by  Marlon on 2018/1/22.
 * Describe 默认进度条下载(可自定义)
 */

public class ProgressDialogDownloadListener implements OnDownloadListener {

    private Context mContext;
    private ProgressDialog mDialog;

    public ProgressDialogDownloadListener(Context context) {
        mContext = context;
    }

    @Override
    public void onStart() {
        if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
            ProgressDialog dialog = new ProgressDialog(mContext);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("下载中...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
            mDialog = dialog;
        }
    }

    @Override
    public void onProgress(int i) {
        if (mDialog != null) {
            mDialog.setProgress(i);
        }
    }

    @Override
    public void onFinish() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}

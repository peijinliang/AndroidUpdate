package com.android.update;

import android.content.Context;

/**
 * Created by  Marlon on 2018/1/22.
 * Describe
 */

public class UpdateManager {

    private Context mContext;
    private String apkUrl;
    private boolean isSlient = false;
    private UpdateInfo updateInfo;
    private String apkName;
    private ProgressDialogDownloadListener progressDialogDownloadListener;
    private NotificationDownloadListener notificationDownloadListener;
    private DefaultUpdatePrompter defaultUpdatePrompter;
    private DownLoadCallBack downLoadCallBack;
    private IUpdateAgent iUpdateAgent;
    private ApkLoadUtils apkLoadUtils;

    /**
     * @param mContext
     * @param apkUrl
     * @param isSlient
     * @param updateInfo
     */
    public UpdateManager(Context mContext, String apkUrl, String apkName, boolean isSlient, UpdateInfo updateInfo) {
        this.mContext = mContext;
        this.apkUrl = apkUrl;
        this.isSlient = isSlient;
        this.apkName = apkName;
        this.updateInfo = updateInfo;
    }

    public void init() {
        if (isSlient) {
            notificationDownloadListener = new NotificationDownloadListener(mContext, 484);
        } else {
            progressDialogDownloadListener = new ProgressDialogDownloadListener(mContext);
        }
        iUpdateAgent = new IUpdateAgent() {
            @Override
            public void update() {
                apkLoadUtils = new ApkLoadUtils(mContext, apkUrl, apkName, downLoadCallBack);
                apkLoadUtils.download();
            }
            @Override
            public void ignore() {
                //暂时取消
                //记录下来，防止下一次依旧开启
            }
        };
        downLoadCallBack = new DownLoadCallBack() {
            @Override
            public void onStart() {
                if (isSlient) {
                    notificationDownloadListener.onStart();
                } else {
                    progressDialogDownloadListener.onStart();
                }
            }

            @Override
            public void onComplete(String filePath) {
                apkLoadUtils.installAPK(filePath, updateInfo.isForce);
                if (isSlient) {
                    notificationDownloadListener.onFinish();
                } else {
                    progressDialogDownloadListener.onFinish();
                }
            }

            @Override
            public void onLoading(long total, long current) {
                if (isSlient) {
                    notificationDownloadListener.onProgress((int) (current * 100 / total));
                } else {
                    progressDialogDownloadListener.onProgress((int) (current * 100 / total));
                }
            }

            @Override
            public void onFail(Exception e) {
                e.printStackTrace();
            }
        };
        defaultUpdatePrompter = new DefaultUpdatePrompter(mContext, iUpdateAgent);
        defaultUpdatePrompter.prompt(updateInfo);
    }

}

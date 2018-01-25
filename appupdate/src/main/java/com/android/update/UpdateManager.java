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
    private boolean isWifiOnly = true; //默认仅仅在wifi环境下下载
    private UpdateInfo updateInfo;
    private String apkName;
    private ProgressDialogDownloadListener progressDialogDownloadListener;
    private NotificationDownloadListener notificationDownloadListener;
    private DefaultUpdatePrompter defaultUpdatePrompter;
    private DownLoadCallBack downLoadCallBack;
    private IUpdateAgent iUpdateAgent;
    private ApkLoadUtils apkLoadUtils;
    private NotificationInfo notificationInfo;

    /**
     * @param mContext
     * @param apkUrl
     * @param isSlient
     * @param updateInfo
     */
    public UpdateManager(Context mContext, String apkUrl, String apkName, boolean isSlient, UpdateInfo updateInfo, NotificationInfo notificationInfo) {
        this.mContext = mContext;
        this.apkUrl = apkUrl;
        this.isSlient = isSlient;
        this.apkName = apkName;
        this.updateInfo = updateInfo;
        this.notificationInfo = notificationInfo;
    }

    public void init() {
        if (updateInfo == null) {
            return;
        }
        //是否已经忽略该版本
        if (UpdateUtils.isIgnore(mContext, updateInfo.versionName)) {
            return;
        }
        isWifiOnly = UpdateUtils.checkWifi(mContext);

        if (isSlient) {
            notificationDownloadListener = new NotificationDownloadListener(mContext, 484, notificationInfo);
        } else {
            progressDialogDownloadListener = new ProgressDialogDownloadListener(mContext);
        }
        iUpdateAgent = new IUpdateAgent() {
            @Override
            public void update() {
                apkLoadUtils = new ApkLoadUtils(mContext, apkUrl, apkName, downLoadCallBack);
                if (isWifiOnly) {
                    apkLoadUtils.download();
                } else {
                    //理论上应该给用户提示
                    apkLoadUtils.download();
                }
            }

            @Override
            public void ignore() {
                UpdateUtils.setIgnore(mContext, updateInfo.versionName);
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

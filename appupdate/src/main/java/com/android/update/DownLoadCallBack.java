package com.android.update;

/**
 * Created by  Marlon on 2018/1/22.
 * Describe
 */
public interface DownLoadCallBack {

    void onStart();

    void onComplete(String filePath);

    void onLoading(long total, long current);

    void onFail(Exception e);

}

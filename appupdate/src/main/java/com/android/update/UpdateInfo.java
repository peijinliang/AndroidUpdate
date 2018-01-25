package com.android.update;


/**
 * Created by  Marlon on 2018/1/23.
 * Describe 更新信息
 */
public class UpdateInfo {

    // 是否静默下载：有新版本时不提示直接下载
    public boolean isSilent = false;
    // 是否强制安装：不安装无法使用app
    public boolean isForce = false;
    // 是否可忽略该版本
    public boolean isIgnorable = true;

    public int versionCode;
    public String versionName;
    public String updateContent;


    public String url;
    public String md5;
    public long size;

}

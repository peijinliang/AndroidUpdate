package com.test.update;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.update.UpdateInfo;
import com.android.update.UpdateManager;

/**
 * APk 更新
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDownload;
    private Context mContext;
    private String apkURL = "http://zebexpress.com/downloads/zebdriver.1.2.0.apk";
    private String apkName = "testdemo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDownload:
                updateApk(mContext, "兄弟们这是咱们需要更新的内容", "15.5", false, true, 1000000, apkURL, apkName);
                break;
        }
    }

    /**
     * @param hitContent  提示更新内容
     * @param versionName 更新版本名
     * @param isForce     是否强制升级
     * @param isSlient    是否静默安装
     * @param fileSize    Apk文件大小
     * @param apkURL      Apk下载链接
     * @param apkName     Apk名称
     */
    public void updateApk(Context mContext, String hitContent, String versionName, boolean isForce, boolean isSlient, long fileSize, String apkURL, String apkName) {
        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.versionName = versionName;
        updateInfo.versionCode = 10;
        updateInfo.isForce = isForce;
        updateInfo.size = fileSize;
        updateInfo.updateContent = hitContent;
        if (isForce) {
            updateInfo.isIgnorable = false;
        }
        new UpdateManager(mContext, apkURL, apkName, isSlient, updateInfo).init();
    }


}

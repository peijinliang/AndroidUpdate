package com.android.update;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by  Marlon on 2018/1/22.
 * Describe apk 下载
 * 主要去处理更新操作
 */

public class ApkLoadUtils {

    private DownLoadCallBack loadCallBack;
    private String LOAD_PATH = null;  //下载路径(URL)
    private String FILE_NAME = null;  //文件名称
    private String SAVE_PATH = null;  //保存文件
    private File SAVE_FILE = null;

    private int FILE_LONGTH = 0;   //文件长度
    private int CURRENT_LONGTH = 0; //当前下载长度
    private Context mContext;       // activity
    //任务定时器
    private Timer mTimer;
    //定时任务
    private TimerTask mTask;

    /**
     * @param loadPath
     * @param fileName
     * @param loadCallBack
     */
    public ApkLoadUtils(Context mContext, String loadPath, String fileName, DownLoadCallBack loadCallBack) {
        this.mContext = mContext;
        this.LOAD_PATH = loadPath;
        this.FILE_NAME = fileName;
        this.SAVE_PATH = getCachePath(mContext);
        this.loadCallBack = loadCallBack;
    }

    /**
     * HttpURLConnection 下载的主方法
     */
    public void download() {
        if (TextUtils.isEmpty(LOAD_PATH)) {
            return;
        }
        if (TextUtils.isEmpty(FILE_NAME)) {
            return;
        }
        if (TextUtils.isEmpty(SAVE_PATH)) {
            return;
        }
        File pathFile = new File(SAVE_PATH);
        if (!pathFile.exists()) {
            boolean isHave = pathFile.mkdirs();
            if (!isHave) {
                return;
            }
        }
        SAVE_FILE = new File(SAVE_PATH + File.separator + FILE_NAME + ".apk");
        if (loadCallBack != null) {
            loadCallBack.onStart();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                FileOutputStream outputStream = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(LOAD_PATH);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(20000);
                    connection.setRequestMethod("GET");
                    FILE_LONGTH = connection.getContentLength();
                    if (connection.getResponseCode() == 404) {
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loadCallBack != null) {
                                    loadCallBack.onFail(new Exception("URL not invalid"));
                                    loadCallBack = null;
                                }
                            }
                        });
                        return;
                    }
                    //计时器
                    initTimer();
                    inputStream = connection.getInputStream();
                    outputStream = new FileOutputStream(SAVE_FILE, false);
                    byte buffer[] = new byte[1024];
                    int readsize = 0;
                    while ((readsize = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, readsize);
                        CURRENT_LONGTH += readsize;
                    }
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeApkFileMode(SAVE_FILE);
                            if (loadCallBack != null) {
                                loadCallBack.onComplete(SAVE_FILE.getPath());
                                loadCallBack = null;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    if (loadCallBack != null) {
                        loadCallBack.onFail(e);
                        loadCallBack = null;
                    }
                } finally {
                    try {
                        destroyTimer();
                        if (connection != null) {
                            connection.disconnect();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }

                    } catch (IOException e) {
                        if (loadCallBack != null) {
                            loadCallBack.onFail(e);
                            loadCallBack = null;
                        }
                    }
                }
            }
        }).start();
    }

    public void installAPK(String apkPath, boolean isForce) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            File apkFile = new File(apkPath);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                String authority = mContext.getPackageName() + ".updateFileProvider";
                Uri contentUri = FileProvider.getUriForFile(mContext, authority, apkFile);
                intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            }
            mContext.startActivity(intent);
            //关闭当前
            if (isForce) {
//              android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        } catch (Exception e) {
            if (loadCallBack != null) {
                loadCallBack.onFail(e);
                loadCallBack = null;
            }
        }
    }

    //在run方法中执行定时的任务
    private void initTimer() {
        mTimer = new Timer();
        mTask = new TimerTask() {
            @Override
            public void run() {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (loadCallBack != null) {
                            loadCallBack.onLoading(FILE_LONGTH, CURRENT_LONGTH);
                        }
                    }
                });
            }
        };
        //任务定时器一定要启动
        mTimer.schedule(mTask, 0, 200);
    }

    private void destroyTimer() {
        if (mTimer != null && mTask != null) {
            mTask.cancel();
            mTimer.cancel();
            mTask = null;
            mTimer = null;
        }
    }

    /**
     * 获取app缓存路径    SDCard/Android/data/你的应用的包名/cache
     *
     * @param context
     * @return
     */
    public String getCachePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    //参照：APK放到data/data/下面提示解析失败 (http://blog.csdn.net/lonely_fireworks/article/details/27693073)
    private void changeApkFileMode(File file) {
        try {
            //apk放在缓存目录时，低版本安装提示权限错误，需要对父级目录和apk文件添加权限
            String cmd1 = "chmod 777 " + file.getParent();
            Runtime.getRuntime().exec(cmd1);

            String cmd = "chmod 777 " + file.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

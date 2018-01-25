package com.android.update;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * Created by  Marlon on 2018/1/25.
 * Describe
 */

public class UpdateUtils {

    private static final String PREFS = "com.android.update";
    private static final String KEY_IGNORE = "com.android.update.ignore";

    /**
     * 设置是否这是忽略该版本
     *
     * @param context
     * @param versionName
     */
    public static void setIgnore(Context context, String versionName) {
        context.getSharedPreferences(PREFS, 0).edit().putString(versionName, KEY_IGNORE).apply();
    }

    /**
     * 检查是否是一个已经忽略的版本
     *
     * @param context
     * @param versionName
     * @return
     */
    public static boolean isIgnore(Context context, String versionName) {
        return context.getSharedPreferences(PREFS, 0).contains(versionName);
    }

    public static boolean checkWifi(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        return info != null && info.isConnected();
    }


}

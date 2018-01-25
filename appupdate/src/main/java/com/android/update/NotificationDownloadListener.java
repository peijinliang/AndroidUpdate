package com.android.update;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

/**
 * Created by  Marlon on 2018/1/22.
 * Describe  Notification 通知
 */
public class NotificationDownloadListener implements OnDownloadListener {

    private Context mContext;
    private String NOTIFICATION_CHANNEL_ID = "484";
    private int NOTIFICATION_ID = 484;
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private NotificationCompat.Builder mBuilder;
    private Notification.Builder mBuilderO;
    private NotificationInfo notificationInfo;

    public NotificationDownloadListener(Context context, int NOTIFICATION_ID, NotificationInfo notificationInfo) {
        this.mContext = context;
        this.NOTIFICATION_ID = NOTIFICATION_ID;
        this.NOTIFICATION_CHANNEL_ID = NOTIFICATION_ID + "";
        this.notificationInfo = notificationInfo;
        notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationChannel == null) {
                notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, notificationInfo.getUpdateInfo(), NotificationManager.IMPORTANCE_LOW);
//              Configure the notification channel.
//              notificationChannel.setDescription("渠道的描述");
                notificationChannel.enableLights(false);
//              notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), Notification.AUDIO_ATTRIBUTES_DEFAULT);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(false);
//              notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationManager.createNotificationChannel(notificationChannel);
            }
            mBuilderO = getChannelNotification(notificationInfo);
        } else {
            if (mBuilder == null) {
//              String title = mContext.getString(mContext.getApplicationInfo().labelRes) + "下载中 - ";
                mBuilder = getCreateNotification(notificationInfo);
            }
        }
        onProgress(0);
    }

    @Override
    public void onProgress(int progress) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilderO.setProgress(100, progress, false);
            notificationManager.notify(NOTIFICATION_ID, mBuilderO.build());
        } else {
            if (mBuilder != null) {
                mBuilder.setProgress(100, progress, false);
                notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
            }
        }
    }

    @Override
    public void onFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
            mBuilderO.setAutoCancel(true);
        } else {
            mBuilder.setAutoCancel(true);
            notificationManager.cancel(NOTIFICATION_ID);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getChannelNotification(NotificationInfo notificationInfo) {
        return new Notification.Builder(mContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(notificationInfo.getContentTitle())
                .setContentText(notificationInfo.getContextText())
                .setOngoing(true)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), notificationInfo.getLargeIcon()))
                .setSmallIcon(notificationInfo.getSmallIcon())
                .setAutoCancel(true);
    }

    public android.support.v4.app.NotificationCompat.Builder getCreateNotification(NotificationInfo notificationInfo) {
        return new NotificationCompat.Builder(mContext)
                .setOngoing(true)
                .setAutoCancel(false)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), notificationInfo.getLargeIcon()))
                .setSmallIcon(notificationInfo.getSmallIcon())
                .setContentTitle(notificationInfo.getContentTitle())
                .setContentText(notificationInfo.getContextText());
    }


}

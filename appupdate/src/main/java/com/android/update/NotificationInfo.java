package com.android.update;

/**
 * Created by  Marlon on 2018/1/25.
 * Describe  Notification Info Configuration
 */
public class NotificationInfo {

    //共有的
    private int largeIcon; //大图标
    private int smallIcon; //小图标
    private String contentTitle; //下载内容标题
    private String contextText;  //下载内容

    //8.0即以上
    private String updateInfo;  //在8.0通知的详细信息里显示（用户点看看详情才会看到）

    public NotificationInfo(int largeIcon, int smallIcon, String contentTitle, String contextText, String updateInfo) {
        this.largeIcon = largeIcon;
        this.smallIcon = smallIcon;
        this.updateInfo = updateInfo;
        this.contentTitle = contentTitle;
        this.contextText = contextText;
    }

    public String getContextText() {
        return contextText;
    }

    public void setContextText(String contextText) {
        this.contextText = contextText;
    }

    public int getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(int largeIcon) {
        this.largeIcon = largeIcon;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }
}

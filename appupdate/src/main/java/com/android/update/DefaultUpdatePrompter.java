package com.android.update;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;


/**
 * Created by  Marlon on 2018/1/22.
 * Describe
 */

public class DefaultUpdatePrompter {

    private Context mContext;
    private IUpdateAgent iUpdateAgent;

    public DefaultUpdatePrompter(Context context, IUpdateAgent iUpdateAgent) {
        this.mContext = context;
        this.iUpdateAgent = iUpdateAgent;
    }

    public void prompt(final UpdateInfo info) {
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
//      String size = Formatter.formatShortFileSize(mContext, info.size); 格式化Apk文件大小
        String content = info.updateContent;
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.setTitle(mContext.getResources().getString(info.title));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        float density = mContext.getResources().getDisplayMetrics().density;
        TextView tv = new TextView(mContext);
        tv.setMovementMethod(new ScrollingMovementMethod());
        tv.setVerticalScrollBarEnabled(true);
        tv.setTextSize(14);
        tv.setMaxHeight((int) (250 * density));
        dialog.setView(tv, (int) (25 * density), (int) (15 * density), (int) (25 * density), 0);
        DialogInterface.OnClickListener listener = new DefaultPromptClickListener(iUpdateAgent);
        if (info.isForce) {
            tv.setText(content);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getResources().getString(info.confirm), listener);
        } else {
            tv.setText(content);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getResources().getString(info.updateNow), listener);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getResources().getString(info.laterSay), listener);
            if (info.isIgnorable) {
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, mContext.getResources().getString(info.ignorable), listener);
            }
        }
        dialog.show();
    }

}

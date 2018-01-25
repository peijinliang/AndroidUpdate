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
//      String size = Formatter.formatShortFileSize(mContext, info.size);
        String content = info.updateContent;
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.setTitle("应用更新");
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
            tv.setText("您需要更新应用才能继续使用\n\n" + content);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", listener);
        } else {
            tv.setText(content);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "立即更新", listener);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "以后再说", listener);
            if (info.isIgnorable) {
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "忽略该版", listener);
            }
        }
        dialog.show();
    }

}

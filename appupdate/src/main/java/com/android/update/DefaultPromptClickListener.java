package com.android.update;

import android.content.DialogInterface;

/**
 * Created by  Marlon on 2018/1/24.
 * Describe
 */
public class DefaultPromptClickListener implements DialogInterface.OnClickListener {

    private IUpdateAgent iUpdateAgent;

    public DefaultPromptClickListener(IUpdateAgent iUpdateAgent) {
        this.iUpdateAgent = iUpdateAgent;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                iUpdateAgent.update();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                iUpdateAgent.ignore();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                // not now  暂时先不更新
                break;
        }
        dialog.dismiss();
    }


}

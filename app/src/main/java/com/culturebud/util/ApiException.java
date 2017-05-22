package com.culturebud.util;

import android.text.TextUtils;

import com.culturebud.BaseApp;

/**
 * Created by XieWei on 2016/11/7.
 */

public class ApiException extends Exception {
    private int code;

    public ApiException(int errCode, String msg) {
        super(msg);
        this.code = errCode;
    }

    @Override
    public String getMessage() {
        if (!TextUtils.isEmpty(getMessage())) {
            return super.getMessage();
        }

        return BaseApp.getInstance().getResources().getString(com.classic.common.R.string.error_view_hint);
    }

    public int getCode() {
        return code;
    }
}

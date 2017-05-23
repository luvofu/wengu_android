package com.culturebud.util;

import android.text.TextUtils;

import com.culturebud.BaseApp;
import com.culturebud.R;

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
        String message = super.getMessage();
        if (!TextUtils.isEmpty(message)) {
            return message;
        }

        return BaseApp.getInstance().getResources().getString(R.string.error_view_hint);
    }

    public int getCode() {
        return code;
    }

    public static String getErrorMessage(Throwable e) {
        String errorMessage = e.getMessage();
        if (!SystemParameterUtil.isNetWorkConnected()) {
            errorMessage = BaseApp.getInstance().getString(R.string.no_network_view_hint);
        } else if (!(e instanceof ApiException) || TextUtils.isEmpty(errorMessage)) {
            errorMessage = BaseApp.getInstance().getResources().getString(R.string.error_view_hint);
        }

        return errorMessage;
    }
}




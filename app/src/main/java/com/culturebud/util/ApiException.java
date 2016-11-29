package com.culturebud.util;

/**
 * Created by XieWei on 2016/11/7.
 */

public class ApiException extends Exception {
    private int code;

    public ApiException(int errCode, String msg) {
        super(msg);
        this.code = errCode;
    }

    public int getCode() {
        return code;
    }
}

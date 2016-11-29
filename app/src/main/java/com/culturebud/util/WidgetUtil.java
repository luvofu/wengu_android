package com.culturebud.util;

import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * Created by XieWei on 2016/11/16.
 */

public class WidgetUtil {

    public static void setRawTextSize(TextView view, int size) {
        Class tvNameCls = view.getClass();
        if (view instanceof AppCompatTextView) {
            tvNameCls = view.getClass().getSuperclass();
        }
        try {
            Method setRawTS = tvNameCls.getDeclaredMethod("setRawTextSize", float.class);
            setRawTS.setAccessible(true);
            setRawTS.invoke(view, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

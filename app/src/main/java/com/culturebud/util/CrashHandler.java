package com.culturebud.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by XieWei on 2016/10/20.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler defaultHalder;
    private static CrashHandler instance;
    private Context context;
    private Map<String, String> infos;

    private CrashHandler() {
        infos = new HashMap<>();
    }

    public static CrashHandler getInstance() {
        if (instance == null) {
            instance = new CrashHandler();
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        defaultHalder = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && defaultHalder != null) {
            defaultHalder.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Log.e(TAG, ex.toString());
            }
            BaseActivity.finishAll();
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
//            int pid = android.os.Process.myPid();
//            String command = "kill -9 "+ pid;
//
//            try {
//                Runtime.getRuntime().exec(command);
//            } catch (IOException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //收集设备参数信息
        collectDeviceInfo(context);
        //保存日志文件
        saveCrashLog(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "CrashHandleran.NameNotFoundException---> error occured when collectAdd package info", e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                Log.e(TAG, "CrashHandler.NameNotFoundException---> an error occured when collectAdd crash info", e);
            }
        }
    }

    /**
     * 写日志文件
     * 同时上传到服务器
     *
     * @param ex
     */
    public void saveCrashLog(Throwable ex) {
        ex.printStackTrace();
//        StackTraceElement[] stes = ex.getStackTrace();
//        for (StackTraceElement ste : stes) {
//            Log.e(TAG, ste.toString());
//        }
    }

}

package com.culturebud;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.culturebud.bean.User;
import com.culturebud.db.DataHelper;
import com.culturebud.util.CrashHandler;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;
import com.tendcloud.tenddata.TCAgent;

import java.util.UUID;

import cn.sharesdk.framework.ShareSDK;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import static android.text.TextUtils.isEmpty;

/**
 * Created by XieWei on 2016/10/19.
 */

public class BaseApp extends Application {
    private static BaseApp app;
    private DataHelper dataHelper;
    private User user;
    private static int currTabIndex;

    public void setCurrTabIndex(int index) {
        currTabIndex = index;
    }

    public int getCurrTabIndex() {
        return currTabIndex;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isMe(long userId) {
        if (user == null) {
            return false;
        }
        if (userId == user.getUserId()) {
            return true;
        }
        return false;
    }

    public DataHelper getDataHelper() {
        if (dataHelper == null) {
            dataHelper = new DataHelper();
        }
        return dataHelper;
    }

    public static BaseApp getInstance() {
        return app;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CommonConst.initHost();
        app = this;
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/FangZhengLanTingZhunHei_GBK.TTF")
                .setFontAttrId(R.attr.fontPath).build());
        Fresco.initialize(this);
//        DiskCacheConfig main = DiskCacheConfig.newBuilder(this)
//                .setBaseDirectoryPath(Environment
//                        .getExternalStorageDirectory()
//                        .getAbsoluteFile())
//                .setBaseDirectoryName("wenya")
//                .setMaxCacheSize(1024 * 1024 * 1024 * 1)
//                .setMaxCacheSizeOnLowDiskSpace(1024 * 1024 * 1024 * 1)
//                .setDiskTrimmableRegistry(NoOpDiskTrimmableRegistry.getInstance())
//                .build();
//        ImagePipelineConfig imgPipelineCfg = ImagePipelineConfig.newBuilder(this)
//                .setBitmapsConfig(Bitmap.Config.ARGB_8888)
//                .setMainDiskCacheConfig(main)
//                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
//                .setResizeAndRotateEnabledForNetwork(true)
//                .build();

//        Fresco.initialize(this, imgPipelineCfg);
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        CrashHandler.getInstance().init(getApplicationContext());
        dataHelper = getDataHelper();//初始化数据库
        dataHelper.getWritableDatabase();
        ShareSDK.initSDK(this);
        CrashReport.initCrashReport(getApplicationContext(), "bc70bcc814", true);
        TCAgent.init(getApplicationContext());
    }

    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");

        try {

            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }

            //如果上面都没有， 则生成一个id：随机码
            String uuid = UUID.randomUUID().toString();
            if (!isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                Log.e("getDeviceId : ", deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(UUID.randomUUID().toString());
        }

        Log.e("getDeviceId : ", deviceId.toString());

        return deviceId.toString();

    }

    public String getDeviceId() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        return imei;
    }
}

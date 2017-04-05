package com.culturebud.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.User;
import com.culturebud.model.MeModel;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by XieWei on 2016/10/20.
 * <p>
 * 欢迎界面的显示
 * <p>
 * 初始化数据的服务启动
 * 应用数据初始化
 * 除了应用必须数据外，其它数据的初始化只允许异步完成
 */

public class WelcomeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.SEND_SMS,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.GET_ACCOUNTS, Manifest.permission.INTERNET)
        .subscribe(grant -> {

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        new Handler().postDelayed(() -> {
            new MeModel().loadLastUser().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<User>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(User user) {
                            if (user != null) {
                                BaseApp.getInstance().setUser(user);
                            }
                            Intent intent = new Intent();
                            intent.setClass(WelcomeActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }, 500);//欢迎界面的时间随初始化数据的时间而定，可加上下限
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

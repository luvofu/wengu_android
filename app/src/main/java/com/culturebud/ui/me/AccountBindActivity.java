package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.bean.User;
import com.culturebud.widget.SettingItemView;


public class AccountBindActivity extends BaseActivity {
    private SettingItemView bindingMobile, bindingWeXin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_binding);
        showTitlebar();
        bindingMobile = obtainViewById(R.id.binding_mobile);
        bindingWeXin = obtainViewById(R.id.binding_wexin);
        setTitle(R.string.binding_account);
        setListeners();

    }

    private void setListeners() {
        bindingMobile.setOnClickListener(this);
        bindingWeXin.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        User user = BaseApp.getInstance().getUser();
        if (null != user) {
            String mobile = user.getRegMobile();
            if (!TextUtils.isEmpty(mobile)) {
                bindingMobile.setRightInfo( mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
            }else {
                bindingMobile.setRightInfo(R.string.no_binding);
            }
            String wexinId = user.getWeixinId();
            String nick = user.getNickname();
            if (!TextUtils.isEmpty(wexinId)) {
                bindingWeXin.setRightInfo(nick);
            } else {
                bindingWeXin.setRightInfo(R.string.no_binding);
            }

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.binding_mobile://绑定手机号
            {
                Intent intent = new Intent(this,MobileBindingActivity.class);
                String mobile =  BaseApp.getInstance().getUser().getRegMobile();
                intent.putExtra("mobile",mobile);
                startActivity(intent);
                break;
            }
            case R.id.binding_wexin://绑定微信
            {

                break;
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

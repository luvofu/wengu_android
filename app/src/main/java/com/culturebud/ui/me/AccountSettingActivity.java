package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.TitleBarActivity;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.LoginContract;
import com.culturebud.presenter.LoginPresenter;
import com.culturebud.util.DataCleanManager;
import com.culturebud.widget.SettingItemView;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_CHANGE_PWD;

/**
 * Created by XieWei on 2016/11/1.
 */

@PresenterInject(LoginPresenter.class)
public class AccountSettingActivity extends BaseActivity<LoginContract.Presenter> implements LoginContract.View {

    private  String cacheSize;
    private SettingItemView cleanView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_setting);
        cleanView = obtainViewById(R.id.siv_clear_buffer);

        presenter.setView(this);
        showTitlebar();
        setBgColor(R.color.litter_gray_bg_border);
        setTitle(R.string.account_setting);
        initCacheSize();
    }

    private void initCacheSize() {
        try {
           cacheSize = DataCleanManager.getCacheSize(getCacheDir());
           cleanView.setRightInfo(cacheSize);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);//不要删除此行，否则左上角的返回将不响应，欲知为何，请看TitleBarActivity

        switch (v.getId()) {
            case R.id.siv_user_info: {
                Intent intent = new Intent(this, UserInfoActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.siv_binding:
                startActivity(new Intent(this,AccountBindActivity.class));
                break;
            case R.id.siv_alter_pwd:
                startActivityForResult(new Intent(this, ChangePasswordActivity.class), REQUEST_CODE_CHANGE_PWD);
                break;
            case R.id.siv_clear_buffer:
                //TODO
                DataCleanManager.cleanInternalCache(this);
                cleanView.setRightInfo("0.00M");
                Toast.makeText(this, "清理缓存成功，本次共为您清理了"+cacheSize, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_logout:
                presenter.logout();
                break;
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void loginSuccess(User user) {

    }

    @Override
    public void onLogout(boolean success) {
        if (success) {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHANGE_PWD:
                break;
        }
    }
}

package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.InviteFriendContract;
import com.culturebud.presenter.InviteFriendPresenter;

/**
 * Created by XieWei on 2016/12/28.
 */

@PresenterInject(InviteFriendPresenter.class)
public class InviteFriendActivity extends BaseActivity<InviteFriendContract.Presenter> implements InviteFriendContract.View {
    private EditText etValidateInfo;
    private long fid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_friend);
        presenter.setView(this);
        showTitlebar();
        setTitle("朋友验证");
        showBack();
        showOperas();
        setOperasText("发送");

        etValidateInfo = obtainViewById(R.id.et_validate_info);
        fid = getIntent().getLongExtra("user_id", -1);
        if (fid == -1) {
            finish();
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        presenter.invite(fid, etValidateInfo.getText().toString());
    }

    @Override
    public void onInviteSuccess(long userId) {
        if (userId == fid) {
            Intent data = new Intent();
            data.putExtra("user_id", fid);
            setResult(RESULT_OK, data);
            finish();
        }
    }
}

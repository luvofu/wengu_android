package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/11/1.
 */

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        showTitlebar();
        setTitle(R.string.about);
        setBgColor(R.color.litter_gray_bg_border);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.siv_user_agreement: {
                Intent intent = new Intent(this, UserAgreementActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}

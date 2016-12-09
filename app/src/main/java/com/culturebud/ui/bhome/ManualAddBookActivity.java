package com.culturebud.ui.bhome;

import android.os.Bundle;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/12/9.
 */

public class ManualAddBookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_add_book);
        showTitlebar();
        showBack();
        showOperas();
        setOperasText("提交审核");
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}

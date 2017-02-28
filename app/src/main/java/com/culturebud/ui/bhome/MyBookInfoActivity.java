package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2017/2/28.
 */

public class MyBookInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_book_info);
        showTitlebar();
        setTitle(R.string.my_book_info);
        showBack();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}

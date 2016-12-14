package com.culturebud.ui.bhome;

import android.os.Bundle;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/12/14.
 */

public class CreateBookSheetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_book_sheet);
        showTitlebar();
        showBack();
        setTitle("新建书单");
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}

package com.culturebud.ui.bhome;

import android.os.Bundle;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2017/3/13.
 */

public class GeneralAddBookTagsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_add_book_tags);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

}

package com.culturebud.ui.bhome;

import android.os.Bundle;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/12/29.
 */

public class UserBookHomeActivity extends BaseActivity {
    private static final String TAG = UserBookHomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_book_home);

        showTitlebar();
        showBack();

    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

}

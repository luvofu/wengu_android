package com.culturebud.ui.search;

import android.os.Bundle;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2017/1/9.
 */

public class SelectBookActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_book);
        showTitlebar();
        hideBack();
        enableSearch();
        setSearchHint(R.string.book_title_or_author);
        showOperas();
        setOperasText(R.string.cancel);
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        finish();
    }
}

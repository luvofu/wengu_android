package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.widget.SettingItemView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

/**
 * Created by XieWei on 2016/12/14.
 */

public class EditBookSheetActivity extends BaseActivity {
    private SimpleDraweeView sdvBookSheetCover;
    private SettingItemView sivBookSheetName;
    private SettingItemView sivBookSheetDesc;
    private BookSheetDetail bookSheetDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_book_sheet);
        showTitlebar();
        setTitle("编辑书单信息");
        showBack();

        sdvBookSheetCover = obtainViewById(R.id.sdv_bs_cover);
        sivBookSheetName = obtainViewById(R.id.siv_bs_name);
        sivBookSheetDesc = obtainViewById(R.id.siv_bs_desc);

        initData();
    }

    private void initData() {
        String sheetJson = getIntent().getStringExtra("bookSheet");
        if (TextUtils.isEmpty(sheetJson)) {
            finish();
            return;
        }
        bookSheetDetail = new Gson().fromJson(sheetJson, BookSheetDetail.class);
        sdvBookSheetCover.setImageURI(bookSheetDetail.getCover());
        sivBookSheetName.setRightInfo(bookSheetDetail.getName());
        sivBookSheetDesc.setRightInfo(bookSheetDetail.getDescription());
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

package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.bean.CheckedBook;
import com.culturebud.widget.FormItemView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

/**
 * Created by XieWei on 2017/4/22.
 */

public class BookInfoActivity extends BaseActivity {
    private SimpleDraweeView sdvCover;
    private FormItemView fivTitle, fivOrgTitle, fivSubTitle, fivISBN;
    private FormItemView fivAuthor, fivTranslator, fivPrice, fivPublisher;
    private FormItemView fivPubDate, fivBinding, fivPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTitlebar();
        setTitle("书籍信息");
        showBack();
        setContentView(R.layout.activity_book_info);
        sdvCover = obtainViewById(R.id.sdv_cover);
        fivTitle = obtainViewById(R.id.fiv_book_title);
        fivOrgTitle = obtainViewById(R.id.fiv_org_name);
        fivSubTitle = obtainViewById(R.id.fiv_sub_title);
        fivISBN = obtainViewById(R.id.fiv_isbn);
        fivAuthor = obtainViewById(R.id.fiv_author);
        fivTranslator = obtainViewById(R.id.fiv_translator);
        fivPrice = obtainViewById(R.id.fiv_price);
        fivPublisher = obtainViewById(R.id.fiv_publisher);
        fivPubDate = obtainViewById(R.id.fiv_pub_date);
        fivBinding = obtainViewById(R.id.fiv_binding);
        fivPages = obtainViewById(R.id.fiv_pages);

        String json = getIntent().getStringExtra("checked_book");
        if (TextUtils.isEmpty(json)) {
            finish();
        }
        CheckedBook book = new Gson().fromJson(json, CheckedBook.class);
        if (!TextUtils.isEmpty(book.getCover())) {
            sdvCover.setImageURI(book.getCover());
        }
        if (!TextUtils.isEmpty(book.getTitle())) {
            fivTitle.setContent(book.getTitle());
        } else {
            fivTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getOriginTitle())) {
            fivOrgTitle.setContent(book.getOriginTitle());
        } else {
            fivOrgTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getSubTitle())) {
            fivSubTitle.setContent(book.getSubTitle());
        } else {
            fivSubTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getIsbn13())) {
            fivISBN.setContent(book.getIsbn13());
        } else {
            fivISBN.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getAuthor())) {
            fivAuthor.setContent(book.getAuthor());
        } else {
            fivAuthor.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getTranslator())) {
            fivTranslator.setContent(book.getTranslator());
        } else {
            fivTranslator.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getPrice())) {
            fivPrice.setContent(book.getPrice());
        } else {
            fivPrice.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getPublisher())) {
            fivPublisher.setContent(book.getPublisher());
        } else {
            fivPublisher.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getPubDate())) {
            fivPubDate.setContent(book.getPubDate());
        } else {
            fivPubDate.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getBinding())) {
            fivBinding.setContent(book.getBinding());
        } else {
            fivBinding.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(book.getPages())) {
            fivPages.setContent(book.getPages());
        } else {
            fivPages.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}

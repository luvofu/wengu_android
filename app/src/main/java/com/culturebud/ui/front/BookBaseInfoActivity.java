package com.culturebud.ui.front;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;

/**
 * Created by XieWei on 2016/11/7.
 */

public class BookBaseInfoActivity extends BaseActivity {
    private TextView tvBookName;
    private TextView tvAuthor;
    private TextView tvTranslator;
    private TextView tvISBN;
    private TextView tvPrice;
    private TextView tvPublisher;
    private TextView tvPubDate;
    private TextView tvBinding;
    private TextView tvPages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_base_info);
        showTitlebar();
        setTitle(R.string.base_info);
        initView();
        initData();
    }

    private void initView() {
        tvBookName = obtainViewById(R.id.tv_book_name);
        tvAuthor = obtainViewById(R.id.tv_author);
        tvTranslator = obtainViewById(R.id.tv_translator);
        tvISBN = obtainViewById(R.id.tv_isbn);
        tvPrice = obtainViewById(R.id.tv_price);
        tvPublisher = obtainViewById(R.id.tv_publisher);
        tvPubDate = obtainViewById(R.id.tv_pub_date);
        tvBinding = obtainViewById(R.id.tv_binding);
        tvPages = obtainViewById(R.id.tv_pages);
    }

    private void initData() {
        Intent intent = getIntent();
        String bookName = intent.getStringExtra("bookName");
        String author = intent.getStringExtra("author");
        String translator = intent.getStringExtra("translator");
        String isbn = intent.getStringExtra("isbn");
        String price = intent.getStringExtra("price");
        String publisher = intent.getStringExtra("publisher");
        String pubDate = intent.getStringExtra("pubDate");
        String binding = intent.getStringExtra("binding");
        int pages = intent.getIntExtra("pages", 0);

        if (!TextUtils.isEmpty(bookName)) {
            tvBookName.setText(bookName);
        }

        if (!TextUtils.isEmpty(author)) {
            tvAuthor.setText(author);
        }

        if (!TextUtils.isEmpty(translator)) {
            tvTranslator.setText(translator);
        }

        if (!TextUtils.isEmpty(isbn)) {
            tvISBN.setText(isbn);
        }

        if (!TextUtils.isEmpty(publisher)) {
            tvPublisher.setText(publisher);
        }

        if (!TextUtils.isEmpty(pubDate)) {
            tvPubDate.setText(pubDate);
        }

        if (!TextUtils.isEmpty(binding)) {
            tvBinding.setText(binding);
        }

        if (!TextUtils.isEmpty(price)) {
            tvPrice.setText(price);
        }
        tvPages.setText(pages + "");
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }
}

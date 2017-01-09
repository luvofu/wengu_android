package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BookScanResultAdapter;
import com.culturebud.bean.Book;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2016/11/26.
 */

public class BookScanResultActivity extends BaseActivity implements BookScanResultAdapter.OnItemClickListener {
    private RecyclerView rvBooks;
    private TextView tvScanCount, tvAddFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_scan_result);
        showTitlebar();
        setTitle(R.string.scan_result);
        setOperasText(R.string.select_none);
        setOperasTextColor(getResources().getColor(R.color.title_font_white));
        rvBooks = obtainViewById(R.id.rv_scan_books);
        tvScanCount = obtainViewById(R.id.tv_scan_count);
        tvAddFavorite = obtainViewById(R.id.tv_add_favorite);
        initList();
    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvBooks.addItemDecoration(divider);
        BookScanResultAdapter adapter = new BookScanResultAdapter();
        adapter.setOnItemClickListener(this);
        rvBooks.setAdapter(adapter);
        Intent intent = getIntent();
        String booksJson = intent.getStringExtra("books");
        if (!TextUtils.isEmpty(booksJson)) {
            List<Book> books = new Gson().fromJson(booksJson, new TypeToken<List<Book>>() {
            }.getType());
            adapter.addItems(books);
            if (books != null && !books.isEmpty()) {
                tvScanCount.setText(String.format(Locale.getDefault(),
                        getString(R.string.scan_result_desc),
                        books.size(), books.size()));
            }
        }
        updateFavoriteCount(adapter);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateFavoriteCount(((BookScanResultAdapter) rvBooks.getAdapter()));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_add_favorite:

                break;
            case R.id.tv_operas:
                BookScanResultAdapter adapter = (BookScanResultAdapter) rvBooks.getAdapter();
                adapter.tongleAllChecked();
                if (adapter.isAllChecked()) {
                    setOperasText("全不选");
                } else {
                    setOperasText("全选");
                }
                updateFavoriteCount(adapter);
                break;
        }
    }

    private void updateFavoriteCount(BookScanResultAdapter adapter) {
        tvAddFavorite.setText(String.format(Locale.getDefault(),
                getString(R.string.book_select_count), adapter.getCheckedCount()));
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onItemClick(View v, Book book, int position) {
        BookScanResultAdapter adapter = (BookScanResultAdapter) rvBooks.getAdapter();
        updateFavoriteCount(adapter);
        if (adapter.isAllChecked()) {
            setOperasText("全不选");
        } else {
            setOperasText("全选");
        }
    }
}

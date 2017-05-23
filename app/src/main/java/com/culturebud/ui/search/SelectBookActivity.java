package com.culturebud.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.BooksSimpleAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.SelectBookContract;
import com.culturebud.presenter.SelectBookPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2017/1/9.
 */

@PresenterInject(SelectBookPresenter.class)
public class SelectBookActivity extends BaseActivity<SelectBookContract.Presenter>
        implements SelectBookContract.View, BooksSimpleAdapter.OnItemClickListener {
    private TextView tvOperaTips;
    private RecyclerView rvBooks;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_book);
        presenter.setView(this);
        showTitlebar();
        hideBack();
        enableSearch();
        setSearchHint(R.string.book_title_or_author);
        showOperas();
        setOperasText(R.string.cancel);
        tvOperaTips = obtainViewById(R.id.tv_hint);
        rvBooks = obtainViewById(R.id.rv_books);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvBooks.addItemDecoration(divider);
        BooksSimpleAdapter adapter = new BooksSimpleAdapter();
        rvBooks.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        Intent intent = getIntent();
        int categoryType = intent.getIntExtra("category_type", CommonConst.CategoryType.TYPE_ALL);
        String category = intent.getStringExtra("category");
        if (TextUtils.isEmpty(category)) {
            category = "全部";
        }
        presenter.getMyBooks(0, categoryType, category);
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        finish();
    }

    @Override
    public void onBooks(List<CollectedBook> books) {
        if (currentPage == 0) {
            ((BooksSimpleAdapter) rvBooks.getAdapter()).clearData();
        }
        ((BooksSimpleAdapter) rvBooks.getAdapter()).addItems(books);
        tvOperaTips.setText(String.format(Locale.getDefault(),
                getString(R.string.searched_books_tip),
                rvBooks.getAdapter().getItemCount()));
    }

    @Override
    public void onItemClick(View v, CollectedBook book) {
        Intent intent = new Intent();
        intent.putExtra("book_id", book.getBookId());
        intent.putExtra("user_book_id", book.getUserBookId());
        intent.putExtra("book_title", book.getTitle());
        intent.putExtra("book_cover", book.getCover());
        intent.putExtra("book_total_page", book.getTotalPage());
        setResult(RESULT_OK, intent);
        finish();
    }
}

package com.culturebud.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BookScanResultAdapter;
import com.culturebud.adapter.BooksSimpleAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Book;
import com.culturebud.bean.CollectedBook;
import com.culturebud.contract.CollectedBooksContract;
import com.culturebud.presenter.CollectedBooksPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;
import java.util.Locale;

/**
 * Created by XieWei on 2017/1/9.
 */

@PresenterInject(CollectedBooksPresenter.class)
public class SelectBookActivity extends BaseActivity<CollectedBooksContract.Presenter>
        implements CollectedBooksContract.View, BooksSimpleAdapter.OnItemClickListener {
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
        presenter.getMyBooks(0);
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
        setResult(RESULT_OK, intent);
        finish();
    }
}

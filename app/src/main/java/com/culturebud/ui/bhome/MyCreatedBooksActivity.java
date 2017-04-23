package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.MyCreatedBooksAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.CheckedBook;
import com.culturebud.contract.MyCreatedBooksContract;
import com.culturebud.presenter.MyCreatedBooksPresenter;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.widget.DividerItemDecoration;
import com.google.gson.Gson;

import java.util.List;

import static com.culturebud.CommonConst.BookVerifyStatus.STATUS_NO_PASS;
import static com.culturebud.CommonConst.BookVerifyStatus.STATUS_PASS;
import static com.culturebud.CommonConst.BookVerifyStatus.STATUS_VERIFY;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_MANUAL_BOOK_CHECK;

/**
 * Created by XieWei on 2017/3/30.
 */

@PresenterInject(MyCreatedBooksPresenter.class)
public class MyCreatedBooksActivity extends BaseActivity<MyCreatedBooksContract.Presenter> implements
        MyCreatedBooksContract.View, MyCreatedBooksAdapter.OnItemClickListener {
    private RecyclerView rvBooks;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_created_books);
        presenter.setView(this);
        showTitlebar();
        showBack();
        setTitle(R.string.my_created);
        rvBooks = obtainViewById(R.id.rv_books);
        rvBooks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvBooks.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL, true));
        MyCreatedBooksAdapter adapter = new MyCreatedBooksAdapter();
        rvBooks.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        presenter.myCreatedBooks(currentPage);
    }

    @Override
    public void onMyBooks(List<CheckedBook> books) {
        if (currentPage == 0) {
            ((MyCreatedBooksAdapter) rvBooks.getAdapter()).clearData();
        }
        ((MyCreatedBooksAdapter) rvBooks.getAdapter()).addItems(books);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onItemClick(View v, CheckedBook book, int position) {
        int status = book.getCheckStatus();
        switch (status) {
            case STATUS_NO_PASS: {
                Intent intent = new Intent(this, ManualAddBookActivity.class);
                intent.putExtra("book", new Gson().toJson(book));
                intent.putExtra("type", 1);
                startActivityForResult(intent, REQUEST_CODE_MANUAL_BOOK_CHECK);
            }
            break;
            case STATUS_VERIFY: {
                Intent intent = new Intent(this, BookInfoActivity.class);
                intent.putExtra("checked_book", new Gson().toJson(book));
                startActivity(intent);
            }
            break;
            case STATUS_PASS: {
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getBookId());
                startActivity(intent);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_MANUAL_BOOK_CHECK:
                if (resultCode == RESULT_OK) {
                    currentPage = 0;
                    presenter.myCreatedBooks(currentPage);
                }
                break;
        }
    }
}

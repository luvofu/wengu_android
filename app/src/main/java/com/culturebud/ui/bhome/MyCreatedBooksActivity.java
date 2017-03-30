package com.culturebud.ui.bhome;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.MyCreatedBooksAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.CheckedBook;
import com.culturebud.contract.MyCreatedBooksContract;
import com.culturebud.presenter.MyCreatedBooksPresenter;
import com.culturebud.widget.DividerItemDecoration;

import java.util.List;

/**
 * Created by XieWei on 2017/3/30.
 */

@PresenterInject(MyCreatedBooksPresenter.class)
public class MyCreatedBooksActivity extends BaseActivity<MyCreatedBooksContract.Presenter> implements
        MyCreatedBooksContract.View {
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
        rvBooks.setAdapter(new MyCreatedBooksAdapter());
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
}

package com.culturebud.ui.front;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.TitleBarActivity;
import com.culturebud.adapter.BookSheetAdapter;
import com.culturebud.adapter.BooksAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.contract.BookStoreContract;
import com.culturebud.presenter.BookStorePresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/11/3.
 */

@PresenterInject(BookStorePresenter.class)
public class BookStoreActivity extends BaseActivity<BookStoreContract.Presenter> implements BookStoreContract.View, RadioGroup.OnCheckedChangeListener, BookSheetAdapter.OnItemClickListener, BooksAdapter.OnItemClickListener {
    private RecyclerView rvBooks;
    private RadioGroup rgSortType;
    private RadioButton rbScore, rbColNum;
    private boolean isBookSheets;
    private int sortType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_store);
        isBookSheets = getIntent().getBooleanExtra("isBookSheets", false);
        showTitlebar();
        initView();

        if (isBookSheets) {
            setTitle(R.string.book_sheet_store);
            rbScore.setText("收藏量");
            rbColNum.setText("创建时间");
        } else {
            setTitle(R.string.book_store);
        }
        initList();

        setListeners();
        rgSortType.check(rbScore.getId());
    }

    private void setListeners() {
        rgSortType.setOnCheckedChangeListener(this);
        rvBooks.setOnScrollListener(listener);
    }

    private void initView() {
        rvBooks = obtainViewById(R.id.rv_books);
        rgSortType = obtainViewById(R.id.rg_sort_type);
        rbScore = obtainViewById(R.id.rb_score);
        rbColNum = obtainViewById(R.id.rb_collect_num);
    }

    private void initList() {
        if (isBookSheets) {
            GridLayoutManager glm = new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false);
            rvBooks.setLayoutManager(glm);
            RecyclerViewDivider hdivider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
            int color = getResources().getColor(R.color.gray_bg_border);
            hdivider.setDividerColor(color);
            hdivider.setDividerHeight(15);
            RecyclerViewDivider vdivider = new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL);
            vdivider.setDividerColor(color);
            vdivider.setDividerHeight(15);
            rvBooks.addItemDecoration(hdivider);
            rvBooks.addItemDecoration(vdivider);
            BookSheetAdapter adapter = new BookSheetAdapter();
            adapter.setOnItemClickListener(this);
            rvBooks.setAdapter(adapter);
        } else {
            LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvBooks.setLayoutManager(llm);
            RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
            divider.setDividerHeight(15);
            rvBooks.addItemDecoration(divider);
            BooksAdapter adapter = new BooksAdapter();
            adapter.setOnItemClickListener(this);
            rvBooks.setAdapter(adapter);
        }
        presenter.setView(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onShowBooks(List<Book> books) {
        loading = false;
        if (currentPage == 0) {
            ((BooksAdapter) rvBooks.getAdapter()).clearData();
        }
        ((BooksAdapter) rvBooks.getAdapter()).addItems(books);
    }

    @Override
    public void onShowBookSheets(List<BookSheet> sheets) {
        loading = false;
        if (currentPage == 0) {
            ((BookSheetAdapter) rvBooks.getAdapter()).clearData();
        }
        ((BookSheetAdapter) rvBooks.getAdapter()).addItems(sheets);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_score:
                sortType = 0;
                break;
            case R.id.rb_collect_num:
                sortType = 1;
                break;
        }
        if (isBookSheets) {
            presenter.getBookSheets(0, sortType, null);
            ((BookSheetAdapter) rvBooks.getAdapter()).clearData();
        } else {
            presenter.getBooks(0, sortType, null);
            ((BooksAdapter) rvBooks.getAdapter()).clearData();
        }
    }

    private int currentPage;
    private boolean loading = true;
    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d("bCircle", "dx = " + dx + ", dy = " + dy);
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                Log.d("bCircle", "lastPosition = " + lastPosition);
                Log.d("bCircle", "total = " + total);
                Log.d("bCircle", "loading is " + loading);
                Log.d("bCircle", "(lastPosition + 1 >= total) is " + (lastPosition + 1 >= total));
                Log.d("bCircle", "加载更多：" + ((lastPosition + 1 >= total) && !loading));
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    if (isBookSheets) {
                        presenter.getBookSheets(++currentPage, sortType, null);
                    } else {
                        presenter.getBooks(++currentPage, sortType, null);
                    }
                }
            } else {

            }
        }
    };

    @Override
    public void onItemClick(int position, View v, BookSheet sheet) {
        Intent intent = new Intent(this, BookSheetDetailActivity.class);
        intent.putExtra("sheetId", sheet.getSheetId());
        startActivity(intent);
    }

    @Override
    public void onItemClick(View v, Book book) {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("bookId", book.getBookId());
        startActivity(intent);
    }
}

package com.culturebud.ui.front;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BookFilterAdapter;
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
public class BookStoreActivity extends BaseActivity<BookStoreContract.Presenter> implements BookStoreContract.View,
        RadioGroup.OnCheckedChangeListener, BookSheetAdapter.OnItemClickListener, BooksAdapter.OnItemClickListener,
        View.OnClickListener {
    private RecyclerView rvBooks;
    private RadioGroup rgSortType;
    private RadioButton rbScore, rbColNum;
    private boolean isBookSheets;
    private TextView filterBtn, filterDisplayView;
    private RecyclerView bookFilterView;
    private int sortType = 0;

    private String filterType;

    private List<String> filterList;

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

    @Override
    public void initFilters(List<String> filters) {
        this.filterList = filters;

    }

    private void setListeners() {
        rgSortType.setOnCheckedChangeListener(this);
        filterDisplayView.setOnClickListener(this);
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupWindow(view);
            }
        });
        rvBooks.setOnScrollListener(listener);
    }

    private void initView() {
        rvBooks = obtainViewById(R.id.rv_books);
        rgSortType = obtainViewById(R.id.rg_sort_type);
        rbScore = obtainViewById(R.id.rb_score);
        rbColNum = obtainViewById(R.id.rb_collect_num);
        filterBtn = obtainViewById(R.id.btn_filter);
        filterDisplayView = obtainViewById(R.id.filter_display);
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
            BooksAdapter adapter = new BooksAdapter(0);
            adapter.setOnItemClickListener(this);
            rvBooks.setAdapter(adapter);
        }

        presenter.setView(this);
        presenter.getFiltersByIsBookSheets(isBookSheets);
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
            presenter.getBookSheets(0, sortType, filterType);
            ((BookSheetAdapter) rvBooks.getAdapter()).clearData();
        } else {
            presenter.getBooks(0, sortType, filterType);
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
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                Log.d("bCircle", "lastPosition = " + lastPosition);
                Log.d("bCircle", "total = " + total);
                Log.d("bCircle", "loading is " + loading);
                Log.d("bCircle", "(lastPosition + 1 >= total) is " + (lastPosition + 1 >= total));
                Log.d("bCircle", "加载更多：" + ((lastPosition + 1 >= total) && !loading));
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    if (isBookSheets) {
                        presenter.getBookSheets(++currentPage, sortType, filterType);
                    } else {
                        presenter.getBooks(++currentPage, sortType, filterType);
                    }
                }
            } else {

            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.filter_display:
                filterType = null;
                currentPage = 0;
                if (isBookSheets) {
                    presenter.getBookSheets(0, sortType, filterType);
                    ((BookSheetAdapter) rvBooks.getAdapter()).clearData();
                } else {
                    presenter.getBooks(0, sortType, filterType);
                    ((BooksAdapter) rvBooks.getAdapter()).clearData();
                }
                v.setVisibility(View.GONE);
                break;
            case R.id.btn_filter:
                showPopupWindow(v);
                break;

        }
    }

    @Override
    public void onItemClick(int position, View v, BookSheet sheet) {
        Intent intent = new Intent(this, BookSheetDetailActivity.class);
        intent.putExtra("sheetId", sheet.getSheetId());
        startActivity(intent);
    }

    @Override
    public void onItemClick(View v, Book book, int operaType) {
        if (operaType == 0) {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getBookId());
            startActivity(intent);
        }
    }


    private PopupWindow popWindow;

    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.book_store_filter, null);
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams
                    .WRAP_CONTENT);

            bookFilterView = (RecyclerView) view.findViewById(R.id.filters);

            GridLayoutManager glm = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
            bookFilterView.setLayoutManager(glm);
            RecyclerViewDivider hdivider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
            int color = getResources().getColor(R.color.gray_bg_border);
            hdivider.setDividerColor(color);
            hdivider.setDividerHeight(15);
            RecyclerViewDivider vdivider = new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL);
            vdivider.setDividerColor(color);
            vdivider.setDividerHeight(15);
            bookFilterView.addItemDecoration(hdivider);
            bookFilterView.addItemDecoration(vdivider);

            BookFilterAdapter bookFilterAdapter = new BookFilterAdapter();

            bookFilterAdapter.setOnItemClickListener(clickListener);
            bookFilterAdapter.addItems(filterList);
            bookFilterView.setAdapter(bookFilterAdapter);


        }
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow.showAsDropDown(parent, Gravity.CENTER, -parent.getHeight());
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);

        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

    }

    private BookFilterAdapter.OnItemClickListener clickListener = new BookFilterAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View v, String filter) {
            currentPage = 0;
            filterType = filter;
            popWindow.dismiss();
            filterDisplayView.setText(filter);
            filterDisplayView.setVisibility(View.VISIBLE);
            if (isBookSheets) {
                presenter.getBookSheets(0, sortType, filterType);
                ((BookSheetAdapter) rvBooks.getAdapter()).clearData();
            } else {
                presenter.getBooks(0, sortType, filterType);
                ((BooksAdapter) rvBooks.getAdapter()).clearData();
            }
        }
    };
}

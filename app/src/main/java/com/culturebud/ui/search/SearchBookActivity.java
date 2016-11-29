package com.culturebud.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BooksAdapter;
import com.culturebud.adapter.SearchKeywordHistoryAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Book;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.SearchBookContract;
import com.culturebud.presenter.SearchBookPresenter;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/11/4.
 */

@PresenterInject(SearchBookPresenter.class)
public class SearchBookActivity extends BaseActivity<SearchBookContract.Presenter>
        implements SearchBookContract.View, TextView.OnEditorActionListener,
        AdapterView.OnItemClickListener, TextWatcher, BooksAdapter.OnItemClickListener {
    private RecyclerView rvSearchedBooks;
    private ListView lvSearchHistory;
    private ProgressBar pbLoading;
    private EditText etSearchKey;
    private Button btnClearHistory;
    private String keyword;
    private InputMethodManager imm;
    private SearchKeywordHistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book);
        presenter.setView(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initView();
        setListeners();
        initList();
    }

    private void initList() {
        historyAdapter = new SearchKeywordHistoryAdapter();
        lvSearchHistory.setAdapter(historyAdapter);
        presenter.getHistorySearchKeyword();

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSearchedBooks.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvSearchedBooks.addItemDecoration(divider);
        BooksAdapter adapter = new BooksAdapter();
        rvSearchedBooks.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void setListeners() {
        etSearchKey.setOnEditorActionListener(this);
        lvSearchHistory.setOnItemClickListener(this);
        etSearchKey.addTextChangedListener(this);
        rvSearchedBooks.setOnScrollListener(listener);
        btnClearHistory.setOnClickListener(this);
    }

    private void initView() {
        rvSearchedBooks = obtainViewById(R.id.rv_searched_books);
        lvSearchHistory = obtainViewById(R.id.lv_search_history);
        pbLoading = obtainViewById(R.id.pb_loading);
        etSearchKey = obtainViewById(R.id.et_search_book);

        rvSearchedBooks.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        btnClearHistory = new Button(this);
        btnClearHistory.setBackgroundResource(android.R.color.white);
        btnClearHistory.setGravity(Gravity.CENTER);
        btnClearHistory.setText("清除历史记录");
        btnClearHistory.setTextColor(getResources().getColor(R.color.tabar_font_checked));
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        btnClearHistory.setLayoutParams(params);
//        lvSearchHistory.addFooterView(btnClearHistory);
    }

    public void onClick(View v) {
        super.onClick(v);
        if (v == btnClearHistory) {
//            lvSearchHistory.removeFooterView(btnClearHistory);
            presenter.clearHistory(historyAdapter.getData());
            return;
        }
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
        }
    }

    @Override
    public void onBooks(List<Book> books) {
        loading = false;
        if (currentPage == 0) {
            ((BooksAdapter) rvSearchedBooks.getAdapter()).clearData();
        }
        rvSearchedBooks.setVisibility(View.VISIBLE);
        lvSearchHistory.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        ((BooksAdapter) rvSearchedBooks.getAdapter()).addItems(books);
    }

    @Override
    public void onKeywords(List<SearchKeyword> keywords) {
        lvSearchHistory.removeFooterView(btnClearHistory);
        historyAdapter.addItems(keywords);
        lvSearchHistory.addFooterView(btnClearHistory);
    }

    @Override
    public void onKeyword(SearchKeyword keyword) {
        etSearchKey.setText(keyword.getKeyword());
        etSearchKey.setSelection(keyword.getKeyword().length());
        historyAdapter.addItem(keyword);
    }

    @Override
    public void onClearHistory() {
        historyAdapter.clearData();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_search_book:
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchKey = etSearchKey.getText().toString();
                    if (!TextUtils.isEmpty(searchKey)) {
                        imm.hideSoftInputFromWindow(etSearchKey.getWindowToken(), 0);
                        keyword = searchKey;
                        pbLoading.setVisibility(View.VISIBLE);
                        lvSearchHistory.setVisibility(View.GONE);
                        rvSearchedBooks.setVisibility(View.GONE);
                        presenter.cacheKeyworkd(searchKey);
                        presenter.searchBook(searchKey, 0);
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchKeyword sk = historyAdapter.getItem(position);
        pbLoading.setVisibility(View.VISIBLE);
        lvSearchHistory.setVisibility(View.GONE);
        rvSearchedBooks.setVisibility(View.GONE);
        presenter.searchBook(sk.getKeyword(), 0);
        keyword = sk.getKeyword();
        etSearchKey.setText(keyword);
        etSearchKey.setSelection(keyword.length());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String tmp = etSearchKey.getText().toString();

//        if (count > 0) {
        if (tmp.isEmpty()) {
            lvSearchHistory.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            rvSearchedBooks.setVisibility(View.GONE);
        }
//        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean loading = true;
    private int currentPage;
    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    presenter.searchBook(keyword, ++currentPage);
                }
            }
        }
    };

    @Override
    public void onItemClick(View v, Book book) {
        if (book != null && book.getBookId() >= 0) {
            Intent intent = new Intent(this, BookDetailActivity.class);
            intent.putExtra("bookId", book.getBookId());
            startActivity(intent);
        }
    }
}

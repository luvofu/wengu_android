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
import com.culturebud.adapter.BookCommunityAdapter;
import com.culturebud.adapter.SearchKeywordHistoryAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCommunity;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.SearchBookCommunityContract;
import com.culturebud.presenter.SearchBookCommunityPresenter;
import com.culturebud.ui.community.BookCommunityActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2016/11/6.
 */

@PresenterInject(SearchBookCommunityPresenter.class)
public class SearchBookCommunityActivity extends BaseActivity<SearchBookCommunityContract.Presenter>
        implements SearchBookCommunityContract.View, AdapterView.OnItemClickListener,
        TextView.OnEditorActionListener, TextWatcher, BookCommunityAdapter.OnItemCommunityListener {
    public static final int REQUEST_NORMAL = 0;
    public static final int REQUEST_SELECT = 1;
    public static final String REQUEST_TYPE = "requestType";
    public static final String RESULT_KEY = "community";
    private RecyclerView rvSearchedBooks;
    private ListView lvSearchHistory;
    private ProgressBar pbLoading;
    private EditText etSearchKey;
    private Button btnClearHistory;
    private String keyword;
    private InputMethodManager imm;
    private SearchKeywordHistoryAdapter historyAdapter;
    private int currentPage;
    private boolean loading = true;
    private int requestType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_book_community);
        presenter.setView(this);

        initView();
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        initList();
        setListeners();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        requestType = intent.getIntExtra(REQUEST_TYPE, REQUEST_NORMAL);
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
        btnClearHistory.setText(R.string.clear_history);
        btnClearHistory.setTextColor(getResources().getColor(R.color.tabar_font_checked));
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        btnClearHistory.setLayoutParams(params);
    }

    private void initList() {
        historyAdapter = new SearchKeywordHistoryAdapter();
        lvSearchHistory.setAdapter(historyAdapter);
        presenter.getHistorySearchKeyword();

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvSearchedBooks.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvSearchedBooks.addItemDecoration(divider);
        BookCommunityAdapter adapter = new BookCommunityAdapter();
        adapter.setOnItemCommunityListener(this);
        rvSearchedBooks.setAdapter(adapter);
    }

    private void setListeners() {
        rvSearchedBooks.setOnScrollListener(listener);
        lvSearchHistory.setOnItemClickListener(this);
        etSearchKey.setOnEditorActionListener(this);
        etSearchKey.addTextChangedListener(this);
        btnClearHistory.setOnClickListener(this);
    }

    @Override
    public void onKeywords(List<SearchKeyword> keywords) {
        lvSearchHistory.removeFooterView(btnClearHistory);
//        historyAdapter.clearData();
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
    public void onBookCommunities(List<BookCommunity> communities) {
        loading = false;
        if (currentPage == 0) {
            ((BookCommunityAdapter) rvSearchedBooks.getAdapter()).clearData();
        }

        rvSearchedBooks.setVisibility(View.VISIBLE);
        lvSearchHistory.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);

        ((BookCommunityAdapter) rvSearchedBooks.getAdapter()).addItems(communities);
    }

    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    presenter.search(keyword, ++currentPage);
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btnClearHistory) {
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchKeyword item = historyAdapter.getItem(position);

        pbLoading.setVisibility(View.VISIBLE);
        lvSearchHistory.setVisibility(View.GONE);
        rvSearchedBooks.setVisibility(View.GONE);

        presenter.search(item.getKeyword(), 0);

        keyword = item.getKeyword();
        etSearchKey.setText(keyword);
        etSearchKey.setSelection(keyword.length());
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_search_book:
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = etSearchKey.getText().toString();
                    if (!TextUtils.isEmpty(key)) {
                        imm.hideSoftInputFromWindow(etSearchKey.getWindowToken(), 0);
                        keyword = key;
                        pbLoading.setVisibility(View.VISIBLE);
                        lvSearchHistory.setVisibility(View.GONE);
                        rvSearchedBooks.setVisibility(View.GONE);
                        presenter.cacheKeyworkd(key);
                        presenter.search(key, 0);
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String tmp = etSearchKey.getText().toString();

        if (tmp.isEmpty()) {
            lvSearchHistory.setVisibility(View.VISIBLE);
            pbLoading.setVisibility(View.GONE);
            rvSearchedBooks.setVisibility(View.GONE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemCommunity(View v, BookCommunity community) {
        switch (requestType) {
            case REQUEST_NORMAL://社区详情页
                if (community != null) {
                    Intent intent = new Intent(this, BookCommunityActivity.class);
                    intent.putExtra("communityId", community.getCommunityId());
                    startActivity(intent);
                }
                break;
            case REQUEST_SELECT: {
                if (community != null) {
                    Intent intent = new Intent();
                    intent.putExtra(RESULT_KEY, new Gson().toJson(community));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            }
        }
    }
}

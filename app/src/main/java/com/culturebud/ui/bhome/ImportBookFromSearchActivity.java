package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
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
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.ImportBooksAdapter;
import com.culturebud.adapter.SearchKeywordHistoryAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Book;
import com.culturebud.bean.SearchKeyword;
import com.culturebud.contract.ImportBookContract;
import com.culturebud.presenter.ImportBookPresenter;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_ADD_BOOK_MANUAL;

/**
 * Created by XieWei on 2016/11/4.
 */

@PresenterInject(ImportBookPresenter.class)
public class ImportBookFromSearchActivity extends BaseActivity<ImportBookContract.Presenter>
        implements ImportBookContract.View, TextView.OnEditorActionListener,
        AdapterView.OnItemClickListener, TextWatcher, ImportBooksAdapter.OnItemClickListener {
    private RecyclerView rvSearchedBooks;
    private ListView lvSearchHistory;
//    private ProgressBar pbLoading;
    private EditText etSearchKey;
    private Button btnClearHistory;
    private TextView bottomCreateBtn;
    private String keyword;
    private InputMethodManager imm;
    private SearchKeywordHistoryAdapter historyAdapter;
    ImportBooksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_book_from_search);
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
        adapter = new ImportBooksAdapter();
        rvSearchedBooks.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    private void setListeners() {
        etSearchKey.setOnEditorActionListener(this);
        lvSearchHistory.setOnItemClickListener(this);
        etSearchKey.addTextChangedListener(this);
        rvSearchedBooks.setOnScrollListener(listener);
        btnClearHistory.setOnClickListener(this);
        bottomCreateBtn.setOnClickListener(this);
    }

    private void initView() {
        rvSearchedBooks = obtainViewById(R.id.rv_searched_books);
        lvSearchHistory = obtainViewById(R.id.lv_search_history);
//        pbLoading = obtainViewById(R.id.pb_loading);
        etSearchKey = obtainViewById(R.id.et_search_book);
        bottomCreateBtn = obtainViewById(R.id.bottom_create_btn);
        bottomCreateBtn.setText(Html.fromHtml("没有找到您想要的书籍，<a style='color:red'>尝试亲手创建吧</a>"));

        rvSearchedBooks.setVisibility(View.GONE);
//        pbLoading.setVisibility(View.GONE);
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
            case R.id.bottom_create_btn:
                Intent intent = new Intent(this, ManualAddBookActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_BOOK_MANUAL);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_ADD_BOOK_MANUAL:
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(this, CollectedBooksActivity.class));
                    finish();
                }
                break;
        }
    }

    @Override
    public void afterAddBook(long bookId) {
        adapter.afterAddBook(bookId);
        setResult(RESULT_OK);
    }

    @Override
    public void onBooks(List<Book> books) {
        loading = false;
        if (currentPage == 0) {
            ((ImportBooksAdapter) rvSearchedBooks.getAdapter()).clearData();
//            rvSearchedBooks.smoothScrollToPosition(0);
        }

        rvSearchedBooks.setVisibility(View.VISIBLE);
        lvSearchHistory.setVisibility(View.GONE);
//        pbLoading.setVisibility(View.GONE);

        if (books != null && books.size() > 0) {
            ((ImportBooksAdapter) rvSearchedBooks.getAdapter()).addItems(books);
        } else {
            onErrorTip("没有搜索到相关结果");
        }
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
//                        pbLoading.setVisibility(View.VISIBLE);
                        lvSearchHistory.setVisibility(View.GONE);
                        rvSearchedBooks.setVisibility(View.GONE);
                        presenter.cacheKeyworkd(searchKey);
                        presenter.searchBook(searchKey, currentPage = 0);
                    }
                }
                return true;
        }
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        imm.hideSoftInputFromWindow(etSearchKey.getWindowToken(), 0);

        SearchKeyword sk = historyAdapter.getItem(position);
//        pbLoading.setVisibility(View.VISIBLE);
        lvSearchHistory.setVisibility(View.GONE);
        rvSearchedBooks.setVisibility(View.GONE);
        presenter.searchBook(sk.getKeyword(), currentPage = 0);
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
//            pbLoading.setVisibility(View.GONE);
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
            if (v.getId() == R.id.add_book) {
                if (book.isContain()) {
                    return;
                }
                presenter.addBook(book);

            } else {
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("bookId", book.getBookId());
                startActivity(intent);
            }
        }
    }
}

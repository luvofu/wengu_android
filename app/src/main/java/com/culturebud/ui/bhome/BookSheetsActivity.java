package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.adapter.BookSheetsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.User;
import com.culturebud.contract.BookSheetsContract;
import com.culturebud.presenter.BookSheetsPresenter;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;
import java.util.Locale;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_BOOK_SHEET_CREATE;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_BOOK_SHEET_DETAIL;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_LOGIN;
import static com.culturebud.ui.bhome.CollectedBooksActivity.USER_ID_KEY;

/**
 * Created by XieWei on 2016/12/13.
 */

@PresenterInject(BookSheetsPresenter.class)
public class BookSheetsActivity extends BaseActivity<BookSheetsContract.Presenter>
        implements BookSheetsContract.View, BookSheetsAdapter.OnItemClickListener, BookSheetsAdapter
        .OnItemDeleteListener {
    private TextView tvCreated, tvFavorite;
    private RecyclerView rvCreated, rvFavorite;
    private long userId = -1;
    private boolean isMe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_home_sheets);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.book_sheet);
        showBack();
        User user = BaseApp.getInstance().getUser();
        long defaultId = user != null ? user.getUserId() : -1;
        userId = getIntent().getLongExtra(USER_ID_KEY, defaultId);
        isMe = BaseApp.getInstance().isMe(userId);
        if (isMe) {
            setOperasDrawable(R.drawable.titlebar_add_selector);
            showOperas();
        } else {
            hideOpears();
        }

        tvCreated = obtainViewById(R.id.tv_my_created);
        tvFavorite = obtainViewById(R.id.tv_my_favorite);
        tvCreated.setText(String.format(Locale.getDefault(), getString(R.string.sheets_my_created), isMe ? "我创建的" : "", 0));
        tvFavorite.setText(String.format(Locale.getDefault(), getString(R.string.sheets_my_favorite), isMe ? "我" : "", 0));

        rvCreated = obtainViewById(R.id.rv_sheets_created);
        rvFavorite = obtainViewById(R.id.rv_sheets_favorite);
        initList();

    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvCreated.setLayoutManager(llm);
        LinearLayoutManager llm2 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFavorite.setLayoutManager(llm2);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvCreated.addItemDecoration(divider);
        rvFavorite.addItemDecoration(divider);

        BookSheetsAdapter createdAdapter = new BookSheetsAdapter();
        if (!isMe) {
            createdAdapter.disableDel();
        }
        createdAdapter.setOnItemClickListener(this);
        createdAdapter.setDeleteListener(this);
        rvCreated.setAdapter(createdAdapter);

        BookSheetsAdapter favoriteAdapter = new BookSheetsAdapter();
        if (!isMe) {
            favoriteAdapter.disableDel();
        }
        favoriteAdapter.setOnItemClickListener(this);
        favoriteAdapter.setDeleteListener(this);
        rvFavorite.setAdapter(favoriteAdapter);

        presenter.getUserCreatedSheets(userId);
        presenter.getUserFavoriteSheets(userId);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_my_created: {
                Drawable drawable = null;
                if ("1".equals(tvCreated.getTag())) {
                    tvCreated.setTag(0);
                    drawable = getResources().getDrawable(R.mipmap.arrow_up);
                    rvCreated.setVisibility(View.GONE);
                } else {
                    tvCreated.setTag(1);
                    drawable = getResources().getDrawable(R.mipmap.arrow_down);
                    rvCreated.setVisibility(View.VISIBLE);
                }
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvCreated.setCompoundDrawables(null, null, drawable, null);

                break;
            }
            case R.id.tv_my_favorite: {
                Drawable drawable = null;
                if ("1".equals(tvFavorite.getTag())) {
                    tvFavorite.setTag(0);
                    drawable = getResources().getDrawable(R.mipmap.arrow_up);
                    rvFavorite.setVisibility(View.GONE);
                } else {
                    tvFavorite.setTag(1);
                    drawable = getResources().getDrawable(R.mipmap.arrow_down);
                    rvFavorite.setVisibility(View.VISIBLE);
                }
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                tvFavorite.setCompoundDrawables(null, null, drawable, null);

                break;
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        Intent intent = new Intent(this, CreateBookSheetActivity.class);
        startActivityForResult(intent, REQUEST_CODE_BOOK_SHEET_CREATE);
    }

    @Override
    public void onMyCreatedSheets(List<BookSheet> sheets) {
        ((BookSheetsAdapter) rvCreated.getAdapter()).clearData();
        ((BookSheetsAdapter) rvCreated.getAdapter()).addItems(sheets);
        tvCreated.setText(String.format(Locale.getDefault(), getString(R.string.sheets_my_created), userId == -1 ?
                "我创建的" : "", sheets
                .size()));
    }

    @Override
    public void onMyFavoriteSheets(List<BookSheet> sheets) {
        ((BookSheetsAdapter) rvFavorite.getAdapter()).clearData();
        ((BookSheetsAdapter) rvFavorite.getAdapter()).addItems(sheets);
        tvFavorite.setText(String.format(Locale.getDefault(), getString(R.string.sheets_my_favorite), userId == -1 ?
                "我" : "", sheets
                .size
                        ()));
    }

    @Override
    public void onDeleteMyCreated(boolean success, BookSheet sheet) {
        ((BookSheetsAdapter) rvCreated.getAdapter()).deleteItem(sheet);
    }

    @Override
    public void onDeleteMyFavorite(boolean success, BookSheet sheet) {
        ((BookSheetsAdapter) rvFavorite.getAdapter()).deleteItem(sheet);
    }

    @Override
    public void onItemClick(View v, int position, BookSheet bookSheet) {
        Intent intent = new Intent(this, BookSheetDetailActivity.class);
        intent.putExtra("sheetId", bookSheet.getSheetId());
        intent.putExtra("user_id", userId);
        startActivityForResult(intent, REQUEST_CODE_BOOK_SHEET_DETAIL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN:
                hasToLogin = false;
                if (resultCode == RESULT_OK) {
                    presenter.getUserCreatedSheets(userId);
                    presenter.getUserFavoriteSheets(userId);
                }
                break;
            case REQUEST_CODE_BOOK_SHEET_CREATE:
                if (resultCode == RESULT_OK) {
                    presenter.getUserCreatedSheets(userId);
                }
                break;
        }
    }

    private boolean hasToLogin;

    @Override
    public void onToLogin() {
        if (hasToLogin) {
            return;
        }
        hasToLogin = true;
        super.onToLogin();
    }

    @Override
    public void onItemDelete(int position, BookSheet bookSheet) {
        presenter.deleteBookSheet(bookSheet);
    }
}

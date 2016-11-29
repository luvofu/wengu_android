package com.culturebud.ui.front;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BookSheetDetailAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.contract.BookSheetDetailContract;
import com.culturebud.presenter.BookSheetDetailPresenter;
import com.culturebud.widget.RecyclerViewDivider;

/**
 * Created by XieWei on 2016/11/7.
 */

@PresenterInject(BookSheetDetailPresenter.class)
public class BookSheetDetailActivity extends BaseActivity<BookSheetDetailContract.Presenter>
        implements BookSheetDetailContract.View, BookSheetDetailAdapter.OnHeaderClickListener {
    private RecyclerView rvDetail;
    private int relationType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_sheet_detail);
        presenter.setView(this);
        showTitlebar();
        rvDetail = obtainViewById(R.id.rv_sheet_detail);

        initList();

        initData();
    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDetail.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvDetail.addItemDecoration(divider);
        BookSheetDetailAdapter adapter = new BookSheetDetailAdapter(this);
        adapter.setOnHeaderClickListener(this);
        rvDetail.setAdapter(adapter);
    }

    private void initData() {
        Intent intent = getIntent();
        long sheetId = intent.getLongExtra("sheetId", -1);
        if (sheetId > 0) {
            presenter.getBookSheetDetail(sheetId);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onBookSheetDetail(BookSheetDetail detail) {
        bookSheetDetail = detail;

        ((BookSheetDetailAdapter) rvDetail.getAdapter()).setData(detail);

    }

    @Override
    public void onRelationType(int type) {
        relationType = type;
    }

    @Override
    public void onCollect(boolean isCollected) {
        bookSheetDetail.setCollect(isCollected);
        rvDetail.getAdapter().notifyItemChanged(0);
    }

    private BookSheetDetail bookSheetDetail;

    @Override
    public void onHeaderClick(View v, int type, BookSheetDetail detail) {
        switch (type) {
            case 0://收藏
                if (detail.isCollect()) {
                    presenter.collectDel(detail.getSheetId());
                } else {
                    presenter.collectAdd(detail.getSheetId());
                }
                break;
            case 1://分享

                break;
        }
    }
}

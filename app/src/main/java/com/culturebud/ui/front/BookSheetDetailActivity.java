package com.culturebud.ui.front;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BookSheetDetailAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.bean.SheetBook;
import com.culturebud.contract.BookSheetDetailContract;
import com.culturebud.presenter.BookSheetDetailPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XieWei on 2016/11/7.
 */

@PresenterInject(BookSheetDetailPresenter.class)
public class BookSheetDetailActivity extends BaseActivity<BookSheetDetailContract.Presenter>
        implements BookSheetDetailContract.View, BookSheetDetailAdapter.OnHeaderClickListener, BookSheetDetailAdapter.OnItemListener {
    private RecyclerView rvDetail;
    private int relationType;
    private PopupWindow pwItemMenu;
    private BottomSheetDialog bsdDailog;

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
        adapter.setOnItemListener(this);
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

    private void initItemMenu() {
        if (pwItemMenu == null) {
            pwItemMenu = new PopupWindow(this, null, R.style.PopupWindow);
            View view = getLayoutInflater().inflate(R.layout.popup_menu_bg_top_single, null);
            view.findViewById(R.id.tv_add).setOnClickListener(this);
            pwItemMenu.setContentView(view);
            pwItemMenu.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            pwItemMenu.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            pwItemMenu.setOutsideTouchable(true);
            pwItemMenu.setFocusable(true);
            pwItemMenu.setBackgroundDrawable(new BitmapDrawable());
        }
    }

    private void initBottomDialog() {
        if (bsdDailog == null) {
            bsdDailog = new BottomSheetDialog(this);
            bsdDailog.setContentView(R.layout.add_to_book_sheet);
            bsdDailog.setCancelable(true);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_add: {
                if (pwItemMenu.isShowing()) {
                    pwItemMenu.dismiss();
                }
                initBottomDialog();
                if (!bsdDailog.isShowing()) {
                    bsdDailog.show();
                }
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
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(detail.getName());
                // text是分享文本，所有平台都需要这个字段
                oks.setText(detail.getName());
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://sharesdk.cn");
                // 启动分享GUI
                oks.show(this);
                break;
        }
    }

    @Override
    public void onItemOpera(View v, int position, int operaType, SheetBook sheetBook) {
        switch (operaType) {
            case OPERA_TYPE_ITEM: {
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("bookId", sheetBook.getBookId());
                startActivity(intent);
                break;
            }
            case OPERA_TYPE_ADD: {
                initItemMenu();
                if (pwItemMenu.isShowing()) {
                    pwItemMenu.dismiss();
                }
                int[] locs = new int[2];
                v.getLocationOnScreen(locs);
                int y = locs[1] - getResources().getDimensionPixelSize(R.dimen.item_popup_menu_height) + v.getHeight() / 2;
                pwItemMenu.showAtLocation(v, Gravity.NO_GRAVITY, locs[0], y);
                break;
            }
        }
    }
}

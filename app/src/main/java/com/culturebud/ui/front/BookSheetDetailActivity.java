package com.culturebud.ui.front;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.adapter.BookSheetDetailAdapter;
import com.culturebud.adapter.MyBookSheetAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.BookSheetDetail;
import com.culturebud.bean.SheetBook;
import com.culturebud.bean.User;
import com.culturebud.contract.BookSheetDetailContract;
import com.culturebud.presenter.BookSheetDetailPresenter;
import com.culturebud.ui.bhome.EditBookSheetActivity;
import com.culturebud.util.ShareHelper;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XieWei on 2016/11/7.
 */

@PresenterInject(BookSheetDetailPresenter.class)
public class BookSheetDetailActivity extends BaseActivity<BookSheetDetailContract.Presenter>
        implements BookSheetDetailContract.View, BookSheetDetailAdapter.OnHeaderClickListener,
        BookSheetDetailAdapter.OnItemListener, MyBookSheetAdapter.OnItemClickListener {
    private static final int REQUEST_CODE_BOOK_SHEET_EDIT = 1010;
    private RecyclerView rvDetail;
    private int relationType;
    private PopupWindow pwItemMenu;
    private BottomSheetDialog bsdDailog;
    private RecyclerView rvBookSheets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_sheet_detail);
        presenter.setView(this);
        showTitlebar();
        setOperasDrawable(R.drawable.titlebar_edit_selector);
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
            rvBookSheets = (RecyclerView) bsdDailog.getWindow().findViewById(R.id.rv_book_sheets);
            LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            rvBookSheets.setLayoutManager(llm);
            RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
            rvBookSheets.addItemDecoration(divider);
            MyBookSheetAdapter mbsAdapter = new MyBookSheetAdapter();
            mbsAdapter.setOnItemClickListener(this);
            rvBookSheets.setAdapter(mbsAdapter);
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
                presenter.getMySheets();
                break;
            }
        }
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        Intent intent = new Intent(this, EditBookSheetActivity.class);
        intent.putExtra("bookSheet", new Gson().toJson(bookSheetDetail));
        startActivityForResult(intent, REQUEST_CODE_BOOK_SHEET_EDIT);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onBookSheetDetail(BookSheetDetail detail) {
        bookSheetDetail = detail;
        User user = BaseApp.getInstance().getUser();
        if (user != null && bookSheetDetail.getUserId() == user.getUserId()) {
            showOperas();
        } else {
            hideOpears();
        }
        ((BookSheetDetailAdapter) rvDetail.getAdapter()).setData(detail);

    }

    @Override
    public void onRelationType(int type) {
        relationType = type;
    }

    @Override
    public void onCollect(boolean isCollected) {
        bookSheetDetail.setCollect(isCollected);
        bookSheetDetail.setCollectionNum(isCollected ?
                bookSheetDetail.getCollectionNum() + 1 :
                bookSheetDetail.getCollectionNum() - 1);
        rvDetail.getAdapter().notifyItemChanged(0);
    }

    @Override
    public void onMySheets(List<BookSheet> bookSheets) {
        if (rvBookSheets != null) {
            ((MyBookSheetAdapter) rvBookSheets.getAdapter()).clearData();
            if (bookSheets.size() > 3) {
                ViewGroup.LayoutParams params = rvBookSheets.getLayoutParams();
                params.height = getResources().getDimensionPixelSize(R.dimen.my_sheet_max_height);
                rvBookSheets.setLayoutParams(params);
            }
            ((MyBookSheetAdapter) rvBookSheets.getAdapter()).addItems(bookSheets);
        }
    }

    @Override
    public void onSheetAddBook(long bookSheetId, long bookId, boolean result) {

    }

    @Override
    public void onSheetDelBook(long sheetBookId, boolean result) {

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
                ShareHelper.share(this, detail.getName(), detail.getName(), null);
                break;
        }
    }

    private SheetBook currClickSheetBook;

    @Override
    public void onItemOpera(View v, int position, int operaType, SheetBook sheetBook) {
        currClickSheetBook = null;
        switch (operaType) {
            case OPERA_TYPE_ITEM: {
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("bookId", sheetBook.getBookId());
                startActivity(intent);
                break;
            }
            case OPERA_TYPE_ADD: {
                currClickSheetBook = sheetBook;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_BOOK_SHEET_EDIT:
                if (resultCode == RESULT_OK) {

                }
                break;
        }
    }

    @Override
    public void onItemClick(View v, BookSheet bookSheet) {
        if (bsdDailog != null && bsdDailog.isShowing()) {
            bsdDailog.dismiss();
        }
        if (bookSheet != null && currClickSheetBook != null) {
            presenter.bookSheetAddBook(bookSheet.getSheetId(), currClickSheetBook.getBookId());
        }
    }
}

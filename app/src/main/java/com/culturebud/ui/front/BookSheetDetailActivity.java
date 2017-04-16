package com.culturebud.ui.front;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.culturebud.ui.bhome.BookSheetEditRecommendActivity;
import com.culturebud.ui.bhome.EditBookSheetActivity;
import com.culturebud.ui.search.SearchBookActivity;
import com.culturebud.util.ShareHelper;
import com.culturebud.widget.RecyclerViewDivider;
import com.culturebud.widget.SettingItemView;
import com.google.gson.Gson;

import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_BOOK_SHEET_EDIT;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_BS_EDIT_RECOMMEND;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SEARCH_BOOK_ADD_TO_BOOK_SHEET;

/**
 * Created by XieWei on 2016/11/7.
 */

@PresenterInject(BookSheetDetailPresenter.class)
public class BookSheetDetailActivity extends BaseActivity<BookSheetDetailContract.Presenter>
        implements BookSheetDetailContract.View, BookSheetDetailAdapter.OnHeaderClickListener,
        BookSheetDetailAdapter.OnItemListener, MyBookSheetAdapter.OnItemClickListener {
    private RecyclerView rvDetail;
    private int relationType;
    private PopupWindow pwItemMenu;
    private BottomSheetDialog bsdDailog;
    private BottomSheetDialog bsdOperas;
    private RecyclerView rvBookSheets;
    private long userId = -1;
    private long sheetId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_sheet_detail);
        presenter.setView(this);
        showTitlebar();
        userId = getIntent().getLongExtra("user_id", -1);
        if (userId == -1) {
            setOperasDrawable(R.drawable.titlebar_edit_selector);
        }
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
        sheetId = intent.getLongExtra("sheetId", -1);
        if (sheetId > 0) {
            presenter.getBookSheetDetail(sheetId);
        } else {
            onErrorTip("非法操作");
            finish();
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

    private void showBottomDialog() {
        initBottomDialog();
        if (!bsdDailog.isShowing()) {
            bsdDailog.show();
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

    private void showBsdOperas() {
        initBsdOperas();
        if (!bsdOperas.isShowing()) {
            bsdOperas.show();
        }
    }

    private void hideBsdOperas() {
        if (bsdOperas != null && bsdOperas.isShowing()) {
            bsdOperas.dismiss();
        }
    }

    private TextView tvBsdOperaBookName;
    private SettingItemView sivRecommendReason, sivAddToBs, sivDelete;

    private void initBsdOperas() {
        if (bsdOperas == null) {
            bsdOperas = new BottomSheetDialog(this);
            bsdOperas.setContentView(R.layout.bsd_operas);
            tvBsdOperaBookName = (TextView) bsdOperas.getWindow().findViewById(R.id.tv_opera_book);
            sivRecommendReason = (SettingItemView) bsdOperas.getWindow().findViewById(R.id.siv_recommend_reason);
            sivAddToBs = (SettingItemView) bsdOperas.getWindow().findViewById(R.id.siv_add_to_bs);
            sivDelete = (SettingItemView) bsdOperas.getWindow().findViewById(R.id.siv_delete);
            bsdOperas.setCancelable(true);
            sivRecommendReason.setOnClickListener(this);
            sivAddToBs.setOnClickListener(this);
            sivDelete.setOnClickListener(this);
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
                showBottomDialog();
                presenter.getMySheets();
                break;
            }
            case R.id.siv_recommend_reason: {
                hideBsdOperas();
                if (currClickSheetBook != null) {
                    Intent intent = new Intent(this, BookSheetEditRecommendActivity.class);
                    intent.putExtra("sheet_book_id", currClickSheetBook.getSheetBookId());
                    intent.putExtra("recommend", currClickSheetBook.getRecommend());
                    startActivityForResult(intent, REQUEST_CODE_BS_EDIT_RECOMMEND);
                }
                break;
            }
            case R.id.siv_add_to_bs:
                hideBsdOperas();
                showBottomDialog();
                presenter.getMySheets();
                break;
            case R.id.siv_delete:
                hideBsdOperas();
                if (currClickSheetBook != null) {
                    presenter.bookSheetDelBook(currClickSheetBook.getSheetBookId());
                }
                break;
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
            if (bookSheetDetail.getSheetBookList() != null && !bookSheetDetail.getSheetBookList().isEmpty()) {

            }
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
        if (result) {
            ((BookSheetDetailAdapter) rvDetail.getAdapter()).deleteByBookId(sheetBookId);
        }
    }

    private BookSheetDetail bookSheetDetail;

    @Override
    public void onHeaderClick(View v, int type, BookSheetDetail detail) {
        switch (type) {
            case 0://收藏
                User user = BaseApp.getInstance().getUser();
                if (user != null && user.getUserId() == detail.getUserId()) {
                    return;
                }
                if (detail.isCollect()) {
                    presenter.collectDel(detail.getSheetId());
                } else {
                    presenter.collectAdd(detail.getSheetId());
                }
                break;
            case 1://分享
                ShareHelper.share(this, detail.getName(), detail.getName(), null);
                break;
            case 2: {//添加书目到书单
                Intent intent = new Intent(this, SearchBookActivity.class);
                intent.putExtra("opera_type", SearchBookActivity.OPERA_TYPE_ADD_TO_BOOK_SHEET);
                intent.putExtra("sheet_id", detail.getSheetId());
                List<SheetBook> books = detail.getSheetBookList();
                if (books != null && !books.isEmpty()) {
                    long[] ids = new long[books.size()];
                    int i = 0;
                    for (SheetBook sheetBook : books) {
                        ids[i] = sheetBook.getBookId();
                        i++;
                    }
                    intent.putExtra("book_ids", ids);
                }
                startActivityForResult(intent, REQUEST_CODE_SEARCH_BOOK_ADD_TO_BOOK_SHEET);
                break;
            }
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
//                initItemMenu();
//                if (pwItemMenu.isShowing()) {
//                    pwItemMenu.dismiss();
//                }
//                int[] locs = new int[2];
//                v.getLocationOnScreen(locs);
//                int y = locs[1] - getResources().getDimensionPixelSize(R.dimen.item_popup_menu_height) + v
// .getHeight() / 2;
//                pwItemMenu.showAtLocation(v, Gravity.NO_GRAVITY, locs[0], y);
                showBsdOperas();
                tvBsdOperaBookName.setText(sheetBook.getTitle());
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
                    presenter.getBookSheetDetail(sheetId);
                }
                break;
            case REQUEST_CODE_BS_EDIT_RECOMMEND: {
                if (resultCode == RESULT_OK) {
                    long sheetBookId = data.getLongExtra("sheet_book_id", -1);
                    String recommend = data.getStringExtra("recommend");
                    ((BookSheetDetailAdapter) rvDetail.getAdapter()).updateBySheetBookId(sheetBookId, recommend);
                }
                break;
            }
            case REQUEST_CODE_SEARCH_BOOK_ADD_TO_BOOK_SHEET: {
                if (resultCode == RESULT_OK) {
                    presenter.getBookSheetDetail(sheetId);
                }
            }
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

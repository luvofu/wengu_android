package com.culturebud.ui.bhome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseFragment;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.BookMarkAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookMark;
import com.culturebud.contract.BookHomeContract;
import com.culturebud.presenter.BookHomePresenter;
import com.culturebud.ui.bcircle.BookCircleActivity;
import com.culturebud.ui.search.SelectBookActivity;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.DividerItemDecoration;
import com.culturebud.widget.RecyclerViewDivider;
import com.culturebud.widget.SettingItemView;
import com.culturebud.widget.StepperView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by XieWei on 2016/10/20.
 */

@PresenterInject(BookHomePresenter.class)
public class BookHomeFragment extends BaseFragment<BookHomeContract.Presenter> implements View.OnClickListener,
        StepperView.OnValueChangedListener, BookHomeContract.View, SwipeRefreshLayout.OnRefreshListener,
        BookMarkAdapter.OnBookMarkItemClickListener {
    private SwipeRefreshLayout srlRefresh;
    private RelativeLayout rlPop;
    private ImageView ivBookMark;
    private TextView tvPageNum, tvBookName;
    private StepperView svStepper;
    private int page = 1;//默认为第一页

    private TextView tvCollect, tvNote, tvBookSheet, tvDateBook;
    private SimpleDraweeView sdvScaner;

    private RecyclerView rvBookMarks;
    private PopupWindow ppwBookMark;
    private FrameLayout flTop;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateView(R.layout.book_home);
        srlRefresh = WidgetUtil.obtainViewById(view, R.id.srl_refresh);
        rlPop = WidgetUtil.obtainViewById(view, R.id.rl_pop);
        ivBookMark = WidgetUtil.obtainViewById(view, R.id.iv_book_mark);
        tvBookName = WidgetUtil.obtainViewById(view, R.id.tv_book_name);
        tvPageNum = WidgetUtil.obtainViewById(view, R.id.tv_book_num);
        svStepper = WidgetUtil.obtainViewById(view, R.id.sv_stepper);

        tvCollect = WidgetUtil.obtainViewById(view, R.id.tv_collect);
        tvNote = WidgetUtil.obtainViewById(view, R.id.tv_note);
        tvBookSheet = WidgetUtil.obtainViewById(view, R.id.tv_sheet);
        tvDateBook = WidgetUtil.obtainViewById(view, R.id.tv_date_book);
        sdvScaner = WidgetUtil.obtainViewById(view, R.id.sdv_scanner);

        rvBookMarks = WidgetUtil.obtainViewById(view, R.id.rv_book_marks);
        flTop = WidgetUtil.obtainViewById(view, R.id.fl_top);

        tvPageNum.setText(String.valueOf(page));
        setListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvBookMarks.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), LinearLayoutManager.HORIZONTAL,
                android.R.color.transparent, getResources().getDimensionPixelSize(R.dimen
                .item_horizontal_divider_width));
        rvBookMarks.addItemDecoration(divider);
        BookMarkAdapter adapter = new BookMarkAdapter();
        adapter.setOnBookMarkItemClickListener(this);
        rvBookMarks.setAdapter(adapter);
    }

    private void setListener() {
        srlRefresh.setOnRefreshListener(this);
        ivBookMark.setOnClickListener(this);
        tvBookName.setOnClickListener(this);
        svStepper.setOnValueChangedListener(this);

        tvCollect.setOnClickListener(this);
        tvNote.setOnClickListener(this);
        tvBookSheet.setOnClickListener(this);
        tvDateBook.setOnClickListener(this);
        sdvScaner.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTitle(getString(R.string.book_home));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_book_mark:
                if (rlPop.getVisibility() != View.VISIBLE) {
                    rlPop.setVisibility(View.VISIBLE);
                } else {
                    rlPop.setVisibility(View.GONE);
                }
                break;
            case R.id.tv_book_name: {

                break;
            }
            case R.id.tv_collect:
                startActivity(new Intent(getActivity(), CollectedBooksActivity.class));
                break;
            case R.id.tv_sheet:
                startActivity(new Intent(getActivity(), BookSheetsActivity.class));
                break;
            case R.id.tv_date_book:
                startActivity(new Intent(getActivity(), BookCircleActivity.class));
                break;
            case R.id.tv_note:
                startActivity(new Intent(getActivity(), NotebookActivity.class));
                break;
            case R.id.sdv_scanner:
                startActivity(new Intent(getActivity(), BookScanActivity.class));
                break;
        }
    }

    public void hidePop() {
        rlPop.setVisibility(View.GONE);
    }

    @Override
    public void onValueChanged(int value, boolean isPlus) {
        tvPageNum.setText(String.valueOf(value));
        page = value;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            presenter.getMyBookMarks();
        }
    }

    @Override
    public void onRefresh() {
        presenter.getMyBookMarks();
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void onBookMarks(List<BookMark> bookMarks) {
        ((BookMarkAdapter) rvBookMarks.getAdapter()).clearData();
        ((BookMarkAdapter) rvBookMarks.getAdapter()).addItems(bookMarks);
    }

    @Override
    public void onAddBookMark(boolean success) {
        if (success) {
            ppwBookMark.dismiss();
            presenter.getMyBookMarks();
        }
    }

    @Override
    public void onAlterBookMark(boolean success) {
        if (success) {
            presenter.getMyBookMarks();
        }
    }

    private SettingItemView sivBookName, sivPages;
    private StepperView svPage;
    private TextView tvDel;

    private void initBookMarkDialog() {
        if (ppwBookMark == null) {
            ppwBookMark = new PopupWindow(getActivity(), null, R.style.PopupWindow);
            ppwBookMark.setBackgroundDrawable(new ColorDrawable(0x55333333));
            ppwBookMark.setOutsideTouchable(false);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.ppw_bookmark_opera, null);
            ImageView ivClose = WidgetUtil.obtainViewById(view, R.id.iv_close);
            ivClose.setOnClickListener(v -> {
                if (ppwBookMark.isShowing()) {
                    ppwBookMark.dismiss();
                }
            });
            TextView tvCompleted = WidgetUtil.obtainViewById(view, R.id.tv_completed);
            tvCompleted.setOnClickListener(v -> {
                if (ppwBookMark != null && ppwBookMark.isShowing()) {
                    if (currentBookMark == null) {
                        presenter.addBookMark(userBookId, svPage.getStepValue(), Integer.valueOf(sivPages.getInfo()));
                    } else {
                        ppwBookMark.dismiss();
                        presenter.alterBookMark(currentBookMark.getBookmarkId(), svPage.getStepValue(),
                                Integer.valueOf(sivPages.getInfo()));
                    }
                }
            });
            tvDel = WidgetUtil.obtainViewById(view, R.id.tv_delete);
            tvDel.setOnClickListener(v -> {
                if (ppwBookMark != null && ppwBookMark.isShowing()) {
                    ppwBookMark.dismiss();
                }
            });
            sivBookName = WidgetUtil.obtainViewById(view, R.id.siv_book_name);
            sivPages = WidgetUtil.obtainViewById(view, R.id.siv_book_pages);
            svPage = WidgetUtil.obtainViewById(view, R.id.sv_stepper);
            svPage.setOnValueChangedListener((value, isPlus) -> {

            });

            sivBookName.setOnClickListener(v -> {
                if (currentBookMark != null) {
                    return;
                }
                Intent intent = new Intent(getActivity(), SelectBookActivity.class);
                startActivityForResult(intent, CommonConst.RequestCode.REQUEST_CODE_SELECT_BOOK);
            });

            ppwBookMark.setContentView(view);
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
            ppwBookMark.setWidth((dm.widthPixels / 3) * 2);
            ppwBookMark.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            view.setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (ppwBookMark.isShowing()) {
                        ppwBookMark.dismiss();
                        return true;
                    }
                }
                return false;
            });
        }
    }

    private int screenWidth, screenHeight;
    private BookMark currentBookMark;

    private void showBookMarkDialog() {
        initBookMarkDialog();
        if (currentBookMark != null) {
            sivBookName.setRightInfo(currentBookMark.getName());
            sivPages.setRightInfo(currentBookMark.getTotalPage() + "");
            svPage.setStep(currentBookMark.getPages());
            sivBookName.setHasArrow(false);
            tvDel.setVisibility(View.VISIBLE);
        } else {
            sivBookName.setRightInfo("");
            sivPages.setRightInfo("300");
            svPage.setStep(0);
            sivBookName.setHasArrow(true);
            tvDel.setVisibility(View.GONE);
        }

        ppwBookMark.showAsDropDown(flTop, (screenWidth / 3) / 2, 4);
    }

    @Override
    public void onBookMarkItemClick(View v, BookMark item, int type) {
        currentBookMark = null;
        userBookId = -1;
        if (type == 1 && item != null) {
            currentBookMark = item;
        } else {

        }
        showBookMarkDialog();
    }

    private long userBookId = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CommonConst.RequestCode.REQUEST_CODE_SELECT_BOOK:
                if (resultCode == Activity.RESULT_OK) {
                    sivBookName.setRightInfo(data.getStringExtra("book_title"));
                    userBookId = data.getLongExtra("user_book_id", -1);
                }
                break;
        }
    }
}

package com.culturebud.ui.bhome;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseFragment;
import com.culturebud.R;
import com.culturebud.adapter.BookMarkAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookMark;
import com.culturebud.contract.BookHomeContract;
import com.culturebud.presenter.BookHomePresenter;
import com.culturebud.ui.bcircle.BookCircleActivity;
import com.culturebud.util.WidgetUtil;
import com.culturebud.widget.RecyclerViewDivider;
import com.culturebud.widget.StepperView;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by XieWei on 2016/10/20.
 */

@PresenterInject(BookHomePresenter.class)
public class BookHomeFragment extends BaseFragment<BookHomeContract.Presenter> implements View.OnClickListener,
        StepperView.OnValueChangedListener, BookHomeContract.View, SwipeRefreshLayout.OnRefreshListener, BookMarkAdapter.OnBookMarkItemClickListener {
    private SwipeRefreshLayout srlRefresh;
    private RelativeLayout rlPop;
    private ImageView ivBookMark;
    private TextView tvPageNum, tvBookName;
    private StepperView svStepper;
    private int page = 1;//默认为第一页

    private TextView tvCollect, tvNote, tvBookSheet, tvDateBook;
    private SimpleDraweeView sdvScaner;

    private RecyclerView rvBookMarks;

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

        tvPageNum.setText(String.valueOf(page));
        setListener();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rvBookMarks.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.VERTICAL, Color
                .WHITE, 32);
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
        ((BookMarkAdapter) rvBookMarks.getAdapter()).addItems(bookMarks);
    }

    @Override
    public void onBookMarkItemClick(View v, BookMark item, int type) {
        if (type == 1 && item != null) {

        } else {

        }
    }
}

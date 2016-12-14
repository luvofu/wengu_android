package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseFragment;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.BookHomeContract;
import com.culturebud.presenter.BookHomePresenter;
import com.culturebud.ui.bcircle.BookCircleActivity;
import com.culturebud.widget.StepperView;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by XieWei on 2016/10/20.
 */

@PresenterInject(BookHomePresenter.class)
public class BookHomeFragment extends BaseFragment<BookHomeContract.Presenter> implements View.OnClickListener,
        StepperView.OnValueChangedListener, BookHomeContract.View, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout srlRefresh;
    private RelativeLayout rlPop;
    private ImageView ivBookMark;
    private TextView tvPageNum, tvBookName;
    private StepperView svStepper;
    private int page = 1;//默认为第一页

    private TextView tvCollect, tvNote, tvBookSheet, tvDateBook;
    private SimpleDraweeView sdvScaner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateView(R.layout.book_home);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        rlPop = (RelativeLayout) view.findViewById(R.id.rl_pop);
        ivBookMark = (ImageView) view.findViewById(R.id.iv_book_mark);
        tvBookName = (TextView) view.findViewById(R.id.tv_book_name);
        tvPageNum = (TextView) view.findViewById(R.id.tv_book_num);
        svStepper = (StepperView) view.findViewById(R.id.sv_stepper);

        tvCollect = (TextView) view.findViewById(R.id.tv_collect);
        tvNote = (TextView) view.findViewById(R.id.tv_note);
        tvBookSheet = (TextView) view.findViewById(R.id.tv_sheet);
        tvDateBook = (TextView) view.findViewById(R.id.tv_date_book);
        sdvScaner = (SimpleDraweeView) view.findViewById(R.id.sdv_scanner);

        tvPageNum.setText(String.valueOf(page));
        setListener();
        return view;
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
        showTitle("书房");
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
    public void onRefresh() {
        srlRefresh.setRefreshing(false);
    }
}

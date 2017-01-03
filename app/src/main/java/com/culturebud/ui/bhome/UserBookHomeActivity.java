package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BookCircleDynamicAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.UserBookHomeContract;
import com.culturebud.presenter.UserBookHomePresenter;
import com.culturebud.ui.community.CommentDetailActivity;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by XieWei on 2016/12/29.
 */

@PresenterInject(UserBookHomePresenter.class)
public class UserBookHomeActivity extends BaseActivity<UserBookHomeContract.Presenter> implements UserBookHomeContract.View, BookCircleDynamicAdapter.OnItemClickListener {
    private static final String TAG = UserBookHomeActivity.class.getSimpleName();
    private SimpleDraweeView sdvBg, sdvFace;
    private TextView tvNick;

    private RecyclerView rvDynamics;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_book_home);
        presenter.setView(this);
        rvDynamics = obtainViewById(R.id.rv_dynamics);
        sdvBg = obtainViewById(R.id.sdv_bg);
        sdvFace = obtainViewById(R.id.sdv_face);
        tvNick = obtainViewById(R.id.tv_nick);

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDynamics.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvDynamics.addItemDecoration(divider);
        BookCircleDynamicAdapter adapter = new BookCircleDynamicAdapter();
        adapter.setOnItemClickListener(this);
        rvDynamics.setAdapter(adapter);
        initData();
    }

    private void initData() {
        long userId = getIntent().getLongExtra("user_id", -1);
        if (userId == -1) {
            finish();
        } else {
            presenter.getUserProfile(userId);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_book_shelf:

                break;
            case R.id.tv_note:

                break;
            case R.id.tv_book_sheet:

                break;
        }
    }

    @Override
    public void onUser(User user) {
        this.user = user;
        tvNick.setText(user.getNickname());
        sdvBg.setImageURI(user.getBackground());
        sdvFace.setImageURI(user.getAvatar());
        presenter.getDynamics(user.getUserId(), currPage);
    }

    private int currPage;

    @Override
    public void onDynamics(List<BookCircleDynamic> dynamics) {
        if (currPage == 0) {
            ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).clearData();
        }
        if (dynamics != null && !dynamics.isEmpty()) {
            ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).addItems(dynamics);
        }
    }

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {
        switch (type) {
            case BookCircleDynamicAdapter.ONCLICK_TYPE_DYNAMIC:
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_BOOK: {
                Intent intent = new Intent(this, BookDetailActivity.class);
                intent.putExtra("bookId", bcd.getLinkId());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_BOOK_SHEET: {
                Intent intent = new Intent(this, BookSheetDetailActivity.class);
                intent.putExtra("sheetId", bcd.getLinkId());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_SHORT_COMMENT:
                Intent intent = new Intent(this, CommentDetailActivity.class);
                intent.putExtra("commentId", bcd.getLinkId());
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_IMG:
                break;
            case BookCircleDynamicAdapter.ONCLICK_TYPE_REPLY:
                break;
        }
    }
}

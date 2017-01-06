package com.culturebud.ui.bcircle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.adapter.BookCircleDynamicAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.bean.User;
import com.culturebud.contract.BookCircleContract;
import com.culturebud.presenter.BookCirclePresenter;
import com.culturebud.ui.bhome.DynamicDetailActivity;
import com.culturebud.ui.community.CommentDetailActivity;
import com.culturebud.ui.front.BookDetailActivity;
import com.culturebud.ui.front.BookSheetDetailActivity;
import com.culturebud.ui.image.PreviewBigImgActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2016/11/20.
 */

@PresenterInject(BookCirclePresenter.class)
public class BookCircleActivity extends BaseActivity<BookCircleContract.Presenter>
        implements BookCircleContract.View, BookCircleDynamicAdapter.OnItemClickListener {
    private RecyclerView rvDynamics;
    private SimpleDraweeView sdvFace;
    private TextView tvNick;
    private ImageView ivPublish;
    private RelativeLayout rlBg;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_circle_activity);
        presenter.setView(this);

        rlBg = obtainViewById(R.id.rl_bc_bg);
        sdvFace = obtainViewById(R.id.sdv_face);
        tvNick = obtainViewById(R.id.tv_nick_name);

        rvDynamics = obtainViewById(R.id.rv_content);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvDynamics.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        divider.setDividerHeight(10);
        rvDynamics.addItemDecoration(divider);
        BookCircleDynamicAdapter adapter = new BookCircleDynamicAdapter();
        adapter.setOnItemClickListener(this);
        rvDynamics.setAdapter(adapter);
        setListeners();
        currentPage = 0;
        presenter.loadDynamics(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user = BaseApp.getInstance().getUser();
        if (user != null) {
            sdvFace.setImageURI(user.getAvatar());
            tvNick.setText(user.getNickname());
        }
    }

    private void setListeners() {
//        ivPublish.setOnClickListener(this);
//        srlRefresh.setOnRefreshListener(this);
        rvDynamics.setOnScrollListener(listener);
    }

    private int currentPage;
    private boolean loading = true;
    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d("bCircle", "dx = " + dx + ", dy = " + dy);
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                Log.d("bCircle", "lastPosition = " + lastPosition);
                Log.d("bCircle", "total = " + total);
                Log.d("bCircle", "loading is " + loading);
                Log.d("bCircle", "(lastPosition + 1 >= total) is " + (lastPosition + 1 >= total));
                Log.d("bCircle", "加载更多：" + ((lastPosition + 1 >= total) && !loading));
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    presenter.loadDynamics(++currentPage);
                }
            } else {

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_publish: {
                Intent intent = new Intent(this, PublishDynamicActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onDynamics(List<BookCircleDynamic> dynamics) {
        if (dynamics == null || dynamics.isEmpty()) {
            Log.d("bCircle", "没有更多了");
            return;
        }
        Log.d("bCircle", "onDynamics()");
        loading = false;
        if (currentPage == 0) {
            ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).clearData();
        }
        ((BookCircleDynamicAdapter) rvDynamics.getAdapter()).addItems(dynamics);
    }

    @Override
    public void onBgImg(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        rlBg.setBackgroundDrawable(drawable);
    }

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {
        switch (type) {
            case BookCircleDynamicAdapter.ONCLICK_TYPE_DYNAMIC: {
                Intent intent = new Intent(this, DynamicDetailActivity.class);
                intent.putExtra("dynamic", new Gson().toJson(bcd));
                startActivity(intent);
                break;
            }
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
            case BookCircleDynamicAdapter.ONCLICK_TYPE_SHORT_COMMENT: {
                Intent intent = new Intent(this, CommentDetailActivity.class);
                intent.putExtra("commentId", bcd.getLinkId());

                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_IMG: {
                Intent intent = new Intent(this, PreviewBigImgActivity.class);
                intent.putExtra("img_url", bcd.getImage());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_REPLY:
                break;
        }
    }
}

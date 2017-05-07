package com.culturebud.ui.bcircle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.culturebud.BaseApp;
import com.culturebud.BaseFragment;
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
 * Created by XieWei on 2016/10/20.
 */

@PresenterInject(BookCirclePresenter.class)
public class BookCircleFragment extends BaseFragment<BookCircleContract.Presenter> implements BookCircleContract.View,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, BookCircleDynamicAdapter.OnItemClickListener {
    private SwipeRefreshLayout srlRefresh;
    private RecyclerView rvDynamics;
    private SimpleDraweeView sdvFace;
    private TextView tvNick;
    private ImageView ivPublish;
    private RelativeLayout rlBg;
    private ImageView ivBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateView(R.layout.book_circle);
        if (getActivity() instanceof BookCircleActivity) {
            initTitleLeft(R.layout.titlebar_left_back);
            ivBack = (ImageView) view.findViewById(R.id.iv_back);
            ivBack.setOnClickListener(this);
        }
        initTitleRight(R.layout.titlebar_right_publish);

        ivPublish = (ImageView) view.findViewById(R.id.iv_publish);

        rlBg = (RelativeLayout) view.findViewById(R.id.rl_bc_bg);
        sdvFace = (SimpleDraweeView) view.findViewById(R.id.sdv_face);
        tvNick = (TextView) view.findViewById(R.id.tv_nick_name);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);

        rvDynamics = (RecyclerView) view.findViewById(R.id.rv_content);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvDynamics.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
        divider.setDividerHeight(10);
        rvDynamics.addItemDecoration(divider);
        BookCircleDynamicAdapter adapter = new BookCircleDynamicAdapter();
        adapter.setOnItemClickListener(this);
        rvDynamics.setAdapter(adapter);
        setListeners();
        return view;
    }

    private void setListeners() {
        ivPublish.setOnClickListener(this);
        srlRefresh.setOnRefreshListener(this);
        rvDynamics.setOnScrollListener(listener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            if (BaseApp.getInstance().getUser() != null) {
                currentPage = 0;
                presenter.loadDynamics(0);
                User user = BaseApp.getInstance().getUser();
                if (user == null) {
                    sdvFace.setImageURI(Uri.EMPTY);
                    tvNick.setText("");
                }
                presenter.downloadBgImg();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showTitle("书圈");
    }

    @Override
    public void onRefresh() {
        srlRefresh.setRefreshing(false);
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
    public void onUploadBgImg(String url) {

    }

    @Override
    public void onThumbUp(long dynamicId, boolean result) {

    }

    @Override
    public void onDynamicReply(DynamicReply dynamicReply) {

    }

    @Override
    public void onDeleteResult(long dynamicId, int deleteType, long deleteObjId, boolean res) {

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
                Intent intent = new Intent(getActivity(), PublishDynamicActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.iv_back:
                getActivity().finish();
                break;
        }
    }

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {
        switch (type) {
            case BookCircleDynamicAdapter.ONCLICK_TYPE_DYNAMIC: {
                Intent intent = new Intent(getActivity(), DynamicDetailActivity.class);
                intent.putExtra("dynamic", new Gson().toJson(bcd));
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_BOOK: {
                Intent intent = new Intent(getActivity(), BookDetailActivity.class);
                intent.putExtra("bookId", bcd.getLinkId());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_BOOK_SHEET: {
                Intent intent = new Intent(getActivity(), BookSheetDetailActivity.class);
                intent.putExtra("sheetId", bcd.getLinkId());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_SHORT_COMMENT: {
                Intent intent = new Intent(getActivity(), CommentDetailActivity.class);
                intent.putExtra("commentId", bcd.getLinkId());

                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_IMG: {
                Intent intent = new Intent(getActivity(), PreviewBigImgActivity.class);
                intent.putExtra("img_url", bcd.getImage());
                startActivity(intent);
                break;
            }
            case BookCircleDynamicAdapter.ONCLICK_TYPE_REPLY:
                break;
        }
    }
}

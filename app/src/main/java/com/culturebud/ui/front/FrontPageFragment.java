package com.culturebud.ui.front;

import android.content.Intent;
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

import com.culturebud.BaseFragment;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.CommentAdapter;
import com.culturebud.adapter.FrontPageAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.contract.FrontPageContract;
import com.culturebud.bean.Book;
import com.culturebud.bean.BookSheet;
import com.culturebud.bean.Comment;
import com.culturebud.presenter.FrontPagePresenter;
import com.culturebud.ui.community.CommunityActivity;
import com.culturebud.ui.search.SearchBookActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/10/20.
 */

@PresenterInject(FrontPagePresenter.class)
public class FrontPageFragment extends BaseFragment<FrontPageContract.Presenter> implements FrontPageContract.View,
        SwipeRefreshLayout.OnRefreshListener, FrontPageAdapter.OnMoreListener, CommentAdapter.OnThumbUpClickListener, View.OnClickListener {
    private static final String TAG = "FrontPageFragment";
    private SwipeRefreshLayout srlRefresh;
    private RecyclerView rvContent;
    private ImageView ivSearch;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateView(R.layout.front_page);
//        initTitleRight(R.layout.titlebar_right_search);

        initView(view);

        initList();

        setListeners();
        return view;
    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
        divider.setDividerHeight(15);
        rvContent.addItemDecoration(divider);
        FrontPageAdapter adapter = new FrontPageAdapter();
        adapter.setOnMoreListener(this);
        rvContent.setAdapter(adapter);
    }

    private void initView(View view) {
//        ivSearch = (ImageView) view.findViewById(R.id.iv_search);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        rvContent = (RecyclerView) view.findViewById(R.id.rv_content);
    }

    private void setListeners() {
        rvContent.setOnScrollListener(listener);
        srlRefresh.setOnRefreshListener(this);
//        ivSearch.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTitle(getString(R.string.tab_front_page));
        srlRefresh.setRefreshing(true);
        presenter.loadDatas();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            presenter.loadDatas();
        }
    }

    @Override
    public void onRefresh() {
        presenter.loadDatas();
    }

    @Override
    public void showBooks(List<Book> books) {
        Log.i(TAG, "showBooks()" + books.toString());
        ((FrontPageAdapter) rvContent.getAdapter()).addHotBooks(books);
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void showBookSheets(List<BookSheet> bookSheets) {
        Log.i(TAG, "showBookSheets()" + bookSheets.toString());
        ((FrontPageAdapter) rvContent.getAdapter()).addHotSheets(bookSheets);
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void showComment(List<Comment> comments) {
        Log.i(TAG, "showComment()" + comments.toString());
        ((FrontPageAdapter) rvContent.getAdapter()).addComments(comments);
        ((FrontPageAdapter) rvContent.getAdapter()).setThumbUpListener(this);
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void onThumbUp(long commentId, boolean isGood) {
        ((FrontPageAdapter) rvContent.getAdapter()).onThumbUp(commentId, isGood);
    }

    private boolean loading = true;

    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                if (!loading && (lastPosition + 1 > total)) {
                    loading = true;
                    //load more

                }
            } else {

            }
        }
    };

    @Override
    public void onMore(int type, RecyclerView.Adapter adapter) {
        switch (type) {
            case 0: {
                Intent intent = new Intent(getActivity(), BookStoreActivity.class);
                startActivity(intent);
                break;
            }
            case 1: {
                Intent intent = new Intent(getActivity(), BookStoreActivity.class);
                intent.putExtra("isBookSheets", true);
                startActivity(intent);
                break;
            }
            case 2: {
                Intent intent = new Intent(getActivity(), CommunityActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onThumbUpClick(long commentId) {
        presenter.thumbUp(CommonConst.ThumbUpType.TYPE_COMMENT, commentId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search: {
                Intent intent = new Intent(getActivity(), SearchBookActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}

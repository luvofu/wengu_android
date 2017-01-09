package com.culturebud.ui.community;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.culturebud.BaseFragment;
import com.culturebud.R;
import com.culturebud.adapter.CommentAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Comment;
import com.culturebud.contract.CommunityContract;
import com.culturebud.presenter.CommunityPresenter;
import com.culturebud.ui.MainActivity;
import com.culturebud.ui.search.SearchBookCommunityActivity;
import com.culturebud.util.ShareHelper;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2016/10/20.
 */

@PresenterInject(CommunityPresenter.class)
public class CommunityFragment extends BaseFragment<CommunityContract.Presenter>
        implements CommunityContract.View, SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, CommentAdapter.OnItemClickListener, CommentAdapter.OnThumbUpClickListener, CommentAdapter.OnBookCommunityClickListener, CommentAdapter.OnShareListener {
    private RecyclerView rvComments;
    private SwipeRefreshLayout srlRefresh;
    private ImageView ivBack, ivRelation, ivSearch, ivPublish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        inflateView(R.layout.community);
        resetTitleStyle();

        initTitlebarLeft(view);
        initTitleRight(R.layout.titlebar_right_publish);
        initView(view);
        initList();
        return view;
    }

    private void initView(View view) {
        ivPublish = (ImageView) view.findViewById(R.id.iv_publish);
        ivSearch = (ImageView) view.findViewById(R.id.iv_search);
        rvComments = (RecyclerView) view.findViewById(R.id.rv_content);
        srlRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(this);
        ivPublish.setOnClickListener(this);
    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvComments.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
        rvComments.addItemDecoration(divider);
        rvComments.setOnScrollListener(recyclerScrollerListener);
        CommentAdapter adapter = new CommentAdapter(true);
        adapter.setOnItemClickListener(this);
        adapter.setOnThumbUpClickListener(this);
        adapter.setOnBookCommunityClickListener(this);
        adapter.setOnShareListener(this);
        rvComments.setAdapter(adapter);
    }

    private void initTitlebarLeft(View view) {
        if (getActivity() instanceof MainActivity) {
            initTitleLeft(R.layout.titlebar_left_relation);
            ivRelation = (ImageView) view.findViewById(R.id.iv_relation);
            ivRelation.setOnClickListener(this);
        } else if (getActivity() instanceof CommunityActivity) {
            initTitleLeft(R.layout.titlebar_left_back);
            ivBack = (ImageView) view.findViewById(R.id.iv_back);
            ivBack.setOnClickListener(this);
        }
    }

    private void resetTitleStyle() {
        TextView tvTitle = getTileView();
        ViewGroup.LayoutParams lp = tvTitle.getLayoutParams();
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        tvTitle.setLayoutParams(lp);
        tvTitle.setBackgroundResource(R.drawable.gray_circle_bg_dark);
        Drawable ldrawable = getResources().getDrawable(R.mipmap.titlebar_search_icon_normal);
        ldrawable.setBounds(0, 0, ldrawable.getIntrinsicWidth(), ldrawable.getIntrinsicHeight());
        tvTitle.setCompoundDrawables(ldrawable, null, null, null);
        tvTitle.setCompoundDrawablePadding(12);
        tvTitle.setTextColor(getResources().getColor(R.color.title_font_white));
        tvTitle.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTitle(getString(R.string.input_bname_query_short_comment));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            presenter.loadComments(0);
            currentPage = 0;
        }
    }

    @Override
    public void onShowComments(List<Comment> comments) {
        CommentAdapter adapter = (CommentAdapter) rvComments.getAdapter();
        if (currentPage == 0) {
            adapter.clearData();
        }
        adapter.addItems(comments);
        loading = false;
        srlRefresh.setRefreshing(false);
    }

    @Override
    public void onThumbUp(long commentId, boolean isGood) {
        CommentAdapter adapter = (CommentAdapter) rvComments.getAdapter();
        adapter.onThumbUp(commentId, isGood);
    }

    private boolean loading = true;
    private int currentPage;

    private RecyclerView.OnScrollListener recyclerScrollerListener = new RecyclerView.OnScrollListener() {
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
                if (!loading && (lastPosition + 1 >= total)) {
                    loading = true;
                    presenter.loadComments(++currentPage);
                }
            } else {

            }
        }
    };

    @Override
    public void onRefresh() {
        currentPage = 0;
        presenter.loadComments(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().finish();
                break;
            case R.id.et_title: {
                Intent intent = new Intent(getActivity(), SearchBookCommunityActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.iv_publish: {
                Intent intent = new Intent(getActivity(), PublishShortCommentActivity.class);
                startActivityForResult(intent, 1003);
                break;
            }
            case R.id.iv_relation: {
                Intent intent = new Intent(getActivity(), MyCommunityActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    public void onItemClick(View v, int position, Comment comment) {
        Intent intent = new Intent(getActivity(), CommentDetailActivity.class);
        intent.putExtra("comment", new Gson().toJson(comment));
        startActivity(intent);
    }

    @Override
    public void onThumbUpClick(long commentId) {
        presenter.thumbUp(0, commentId);
    }

    @Override
    public void onBookCommunityClick(View v, int position, Comment comment) {
        Intent intent = new Intent(getActivity(), BookCommunityActivity.class);
        intent.putExtra("communityId", comment.getCommunityId());
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1003:
                if (resultCode == -1) {
                    currentPage = 0;
                    presenter.loadComments(0);
                }
                break;
        }
    }

    @Override
    public void onShare(View v, int position, Comment comment) {
        ShareHelper.share(getActivity(), comment.getTitle(), comment.getTitle(), null);
    }
}

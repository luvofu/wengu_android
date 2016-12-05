package com.culturebud.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.culturebud.BaseFragment;
import com.culturebud.R;
import com.culturebud.adapter.CommentAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Comment;
import com.culturebud.bean.MyRelatedComment;
import com.culturebud.contract.MyCommentContract;
import com.culturebud.presenter.MyCommentPresenter;
import com.culturebud.util.ShareHelper;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XieWei on 2016/11/19.
 */

@PresenterInject(MyCommentPresenter.class)
public class MyPublishedFragment extends BaseFragment<MyCommentContract.Presenter> implements MyCommentContract.View, CommentAdapter.OnItemClickListener, CommentAdapter.OnBookCommunityClickListener, CommentAdapter.OnThumbUpClickListener, CommentAdapter.OnShareListener {
    private RecyclerView rvComments;
    private int currentPage;
    private boolean loading = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_published_related, container, false);
        rvComments = (RecyclerView) view.findViewById(R.id.rv_contents);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvComments.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(getActivity(), LinearLayoutManager.HORIZONTAL);
        rvComments.addItemDecoration(divider);
        rvComments.addOnScrollListener(onScrollListener);
        CommentAdapter adapter = new CommentAdapter(true);
        adapter.setOnItemClickListener(this);
        adapter.setOnBookCommunityClickListener(this);
        adapter.setOnThumbUpClickListener(this);
        adapter.setOnShareListener(this);
        rvComments.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getMyPublished(0);
    }

    @Override
    public void onComments(List<Comment> comments) {
        if (currentPage == 0) {
            ((CommentAdapter) rvComments.getAdapter()).clearData();
        }
        loading = false;
        ((CommentAdapter) rvComments.getAdapter()).addItems(comments);
    }

    @Override
    public void onMyRelatedComments(List<MyRelatedComment> comments) {
        //不需要实现
    }

    @Override
    public void onThumbUp(boolean res, long goodObjId) {
        ((CommentAdapter) rvComments.getAdapter()).onThumbUp(goodObjId, res);
    }

    @Override
    public void onItemClick(View v, int position, Comment comment) {
        Intent intent = new Intent(getActivity(), CommentDetailActivity.class);
        intent.putExtra("comment", new Gson().toJson(comment));
        startActivity(intent);
    }

    @Override
    public void onBookCommunityClick(View v, int position, Comment comment) {
        Intent intent = new Intent(getActivity(), BookCommunityActivity.class);
        intent.putExtra("communityId", comment.getCommunityId());
        startActivity(intent);

    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                if (!loading && (lastPosition + 1 >= total)) {
                    loading = true;
                    presenter.getMyPublished(++currentPage);
                }
            } else {

            }
        }
    };

    @Override
    public void onThumbUpClick(long commentId) {
        presenter.thumbUp(commentId);
    }

    @Override
    public void onShare(View v, int position, Comment comment) {
        ShareHelper.share(getActivity(), comment.getTitle(), comment.getTitle(), null);
    }
}

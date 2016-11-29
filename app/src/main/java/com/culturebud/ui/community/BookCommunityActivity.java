package com.culturebud.ui.community;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst.CommentOperaType;
import com.culturebud.R;
import com.culturebud.adapter.BookCommunityCommentAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCommunity;
import com.culturebud.bean.BookCommunityDetail;
import com.culturebud.bean.Comment;
import com.culturebud.contract.BookCommunityContract;
import com.culturebud.presenter.BookCommunityPresenter;
import com.culturebud.util.ImgUtil;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by XieWei on 2016/11/10.
 */

@PresenterInject(BookCommunityPresenter.class)
public class BookCommunityActivity extends BaseActivity<BookCommunityContract.Presenter>
        implements BookCommunityContract.View, RadioGroup.OnCheckedChangeListener,
        BookCommunityCommentAdapter.OnOperaClickListener, BookCommunityCommentAdapter.OnItemClickListener {
    private RecyclerView rvComments;
    private TextView tvBookTitle, tvCount, tvAuthor;
    private RadioGroup rgSortType;
    private long communityId;
    private BookCommunity community;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_community);
        presenter.setView(this);
        showTitlebar();
        setOperasDrawable(R.drawable.titlebar_add_selector);
        setTitle("书籍社区");
        rvComments = obtainViewById(R.id.rv_comments);
        tvBookTitle = obtainViewById(R.id.tv_book_theme);
        tvCount = obtainViewById(R.id.tv_count);
        tvAuthor = obtainViewById(R.id.tv_author);
        rgSortType = obtainViewById(R.id.rg_sort_type);

        Bitmap bitmap = ImgUtil.doBlur(BitmapFactory.decodeResource(getResources(),
                R.mipmap.book_home_top_bg), 25, false);
        obtainViewById(R.id.rl_top).setBackgroundDrawable(new BitmapDrawable(bitmap));
        initList();

        initData();

        setListeners();
    }

    private void setListeners() {
        rgSortType.setOnCheckedChangeListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        communityId = intent.getLongExtra("communityId", -1);
        if (communityId <= 0) {
            return;
        }
        presenter.getCommunityDetail(communityId);
    }

    private void initList() {
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvComments.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvComments.addItemDecoration(divider);
        BookCommunityCommentAdapter adapter = new BookCommunityCommentAdapter();
        adapter.setOnItemClickListener(this);
        adapter.setOnOperaClickListener(this);
        rvComments.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_operas: {
                if (community != null) {
                    Intent intent = new Intent(this, PublishShortCommentActivity.class);
                    intent.putExtra("community", new Gson().toJson(community));
                    startActivityForResult(intent, 1004);
                }
                break;
            }
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onCommunityDetail(BookCommunityDetail detail) {
        community = new BookCommunity();
        community.setAuthor(detail.getAuthor());
        community.setCommentNum(detail.getCommentNum());
        community.setCommunityId(detail.getCommunityId());
        community.setSubTitle(detail.getSubTitle());
        community.setTitle(detail.getTitle());

        tvBookTitle.setText(detail.getTitle());
        tvAuthor.setText(detail.getAuthor());
        tvCount.setText(String.format(Locale.getDefault(), getString(R.string.comment_num), detail.getCommentNum()));
        presenter.getCommunityComments(0, rgSortType.getCheckedRadioButtonId() == R.id.rb_new
                ? 1 : 0, detail.getCommunityId());
    }

    @Override
    public void onComments(List<Comment> comments) {
        BookCommunityCommentAdapter adapter = (BookCommunityCommentAdapter) rvComments.getAdapter();
        adapter.clearData();
        adapter.addItems(comments);
    }

    @Override
    public void onThumbUp(boolean res, long commentId, int position) {
        ((BookCommunityCommentAdapter) rvComments.getAdapter()).onThumbUp(res, commentId, position);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_new:
                presenter.getCommunityComments(0, 1, communityId);
                break;
            case R.id.rb_hot:
                presenter.getCommunityComments(0, 0, communityId);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1004:
                if (resultCode == RESULT_OK) {
                    presenter.getCommunityComments(0, rgSortType.getCheckedRadioButtonId() == R.id.rb_new
                            ? 1 : 0, community.getCommunityId());
                }
                break;
        }
    }

    @Override
    public void onOperaClick(int position, View v, int type, Comment comment) {
        switch (type) {
            case CommentOperaType.TYPE_THUMB_UP:
                presenter.thumbUp(comment.getCommentId(), position);
                break;
            case CommentOperaType.TYPE_REPLY:
                Intent intent = new Intent(this, CommentDetailActivity.class);
                intent.putExtra("comment", new Gson().toJson(comment));
                startActivity(intent);
                break;
            case CommentOperaType.TYPE_SHARE:
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(comment.getTitle());
                // text是分享文本，所有平台都需要这个字段
                oks.setText(comment.getTitle());
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://sharesdk.cn");
                // 启动分享GUI
                oks.show(this);
                break;
        }
    }

    @Override
    public void onItemClick(int position, View v, Comment comment) {
        Intent intent = new Intent(this, CommentDetailActivity.class);
        intent.putExtra("comment", new Gson().toJson(comment));
        startActivity(intent);
    }
}

package com.culturebud.ui.community;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCommunity;
import com.culturebud.contract.PublishShortCommentContract;
import com.culturebud.presenter.PublishShortCommentPresenter;
import com.culturebud.ui.search.SearchBookCommunityActivity;
import com.google.gson.Gson;

/**
 * Created by XieWei on 2016/11/11.
 */

@PresenterInject(PublishShortCommentPresenter.class)
public class PublishShortCommentActivity extends BaseActivity<PublishShortCommentContract.Presenter>
        implements PublishShortCommentContract.View {
    private static final String TAG = PublishShortCommentActivity.class.getSimpleName();
    private EditText etInputComment;
    private TextView tvSelectCommunity;
    private InputMethodManager imm;
    private BookCommunity community;
    private RelativeLayout rlCommunityInfo;
    private FrameLayout flCenter;
    private TextView tvCommTitle, tvAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_short_comment);
        presenter.setView(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        showTitlebar();
        setTitle(R.string.publish_short_comment);
        setBackText(R.string.cancel);
        setOperasText(R.string.publish);
        setBackTextColor(getResources().getColor(R.color.title_font_white));
        setOperasTextColor(getResources().getColor(R.color.title_font_white));
        etInputComment = obtainViewById(R.id.et_comment_content);
        flCenter = obtainViewById(R.id.fl_center);
        tvSelectCommunity = obtainViewById(R.id.tv_select_community);
        rlCommunityInfo = obtainViewById(R.id.rl_info);
        tvCommTitle = obtainViewById(R.id.tv_community_title);
        tvAuthor = obtainViewById(R.id.tv_author);
        flCenter.setOnClickListener(this);
        initData();
    }

    private void initData() {
        String communityJson = getIntent().getStringExtra("community");
        if (!TextUtils.isEmpty(communityJson)) {
            community = new Gson().fromJson(communityJson, BookCommunity.class);
            if (community != null) {
                flCenter.setVisibility(View.GONE);
            } else {
                flCenter.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.fl_center: {
                Intent intent = new Intent(this, SearchBookCommunityActivity.class);
                intent.putExtra(SearchBookCommunityActivity.REQUEST_TYPE, SearchBookCommunityActivity.REQUEST_SELECT);
                startActivityForResult(intent, REQUEST_CODE_SELECT_COMMUNITY);
                break;
            }
        }
    }

    @Override
    protected void onBack() {//取消
        super.onBack();
        finish();
    }

    @Override
    protected void onOptions(View view) {//发布
        super.onOptions(view);
        if (community != null) {
            presenter.publish(community.getCommunityId(), etInputComment.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SELECT_COMMUNITY:
                if (resultCode == RESULT_OK) {
                    String communityJson = data.getStringExtra(SearchBookCommunityActivity.RESULT_KEY);
                    if (!TextUtils.isEmpty(communityJson)) {
                        community = new Gson().fromJson(communityJson, BookCommunity.class);
                        Log.e(TAG, community.toString());
                        if (community != null) {
                            tvCommTitle.setText(community.getTitle());
                            tvAuthor.setText(community.getAuthor());

                            rlCommunityInfo.setVisibility(View.VISIBLE);
                            tvSelectCommunity.setVisibility(View.GONE);
                        }
                    }
                }
                break;
        }
    }

    @Override
    public void onPublisResult(boolean res) {
        if (res) {
            imm.hideSoftInputFromWindow(etInputComment.getWindowToken(), 0);
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }

    @Override
    public void onContentEmpty() {

    }
}

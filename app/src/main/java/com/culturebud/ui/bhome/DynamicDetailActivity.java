package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.DynamicDetailCommentAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.DynamicReply;
import com.culturebud.contract.DynamicDetailContract;
import com.culturebud.presenter.DynamicDetailPresenter;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2017/1/3.
 */

@PresenterInject(DynamicDetailPresenter.class)
public class DynamicDetailActivity extends BaseActivity<DynamicDetailContract.Presenter>
        implements DynamicDetailContract.View {
    private static final String TAG = DynamicDetailActivity.class.getSimpleName();
    private BookCircleDynamic bcd;
    private RecyclerView rvReplies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_detail);
        presenter.setView(this);

        rvReplies = obtainViewById(R.id.rv_replies);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvReplies.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvReplies.addItemDecoration(divider);
        DynamicDetailCommentAdapter adapter = new DynamicDetailCommentAdapter();
        rvReplies.setAdapter(adapter);

        showTitlebar();
        showBack();
        setTitle("动态详情");
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String dynamic = intent.getStringExtra("dynamic");
        if (!TextUtils.isEmpty(dynamic)) {
            bcd = new Gson().fromJson(dynamic, BookCircleDynamic.class);
            onDynamic(bcd);
            return;
        }
        long dynamicId = intent.getLongExtra("dynamic_id", -1);
        if (dynamicId == -1) {
            finish();
        }
        presenter.dynamicDetail(dynamicId);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onDynamic(BookCircleDynamic dynamic) {
        if (dynamic == null) {
            return;
        }
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).setDynamic(dynamic);
        if (dynamic.getDynamicReplies() == null) {
            return;
        }
        presenter.processReplies(dynamic.getDynamicReplies());
    }

    @Override
    public void onReplies(List<DynamicReply> replies) {
        ((DynamicDetailCommentAdapter) rvReplies.getAdapter()).addItems(replies);
    }
}

package com.culturebud.ui.bcircle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.BookCircleDynamicAdapter;
import com.culturebud.adapter.RelationMeBookCircleDynamicAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.BookCircleDynamic;
import com.culturebud.bean.BookCircleDynamicRelationMe;
import com.culturebud.bean.DynamicReply;
import com.culturebud.contract.MyDynamicsContract;
import com.culturebud.presenter.MyDynamicsPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2017/1/12.
 */

@PresenterInject(MyDynamicsPresenter.class)
public class MyDynamicActivity extends BaseActivity<MyDynamicsContract.Presenter>
        implements MyDynamicsContract.View, BookCircleDynamicAdapter.OnItemClickListener,
        RelationMeBookCircleDynamicAdapter.OnItemClickListener {
    private RecyclerView rvDynamics;
    private BookCircleDynamicAdapter adapter;
    private RelationMeBookCircleDynamicAdapter rmbcdAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_dynamics);
        presenter.setView(this);
        rvDynamics = obtainViewById(R.id.rv_my_dynamics);
        rvDynamics.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvDynamics.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));

        showTitlebar();
        showBack();
        int type = getIntent().getIntExtra("type", 0);//0表示我发布的 1表示与我相关的
        if (type == 0) {
            adapter = new BookCircleDynamicAdapter();
            adapter.setOnItemClickListener(this);
            rvDynamics.setAdapter(adapter);
            setTitle(R.string.my_published_dynamic);
            presenter.myPublished(0);
        } else {
            rmbcdAdapter = new RelationMeBookCircleDynamicAdapter();
            rmbcdAdapter.setOnItemClickListener(this);
            rvDynamics.setAdapter(rmbcdAdapter);
            setTitle(R.string.related_me);
            presenter.myRelations(0);
        }
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onDynamics(List<BookCircleDynamic> dynamics) {
        if (adapter != null) {
            adapter.addItems(dynamics);
        }
    }

    @Override
    public void onRelations(List<BookCircleDynamicRelationMe> dynamics) {
        if (rmbcdAdapter != null) {
            rmbcdAdapter.addItems(dynamics);
        }
    }

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {

    }
}

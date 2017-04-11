package com.culturebud.ui.bcircle;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private int type;

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

        rvDynamics.setOnScrollListener(listener);

        type = getIntent().getIntExtra("type", 0);//0表示我发布的 1表示与我相关的
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
        if (dynamics == null || dynamics.isEmpty()) {
            Log.d("bCircle", "没有更多了");
            return;
        }
        loading = false;
        if (adapter != null) {
            if (currentPage == 0) {
                adapter.clearData();
            }
            adapter.addItems(dynamics);
        }
    }

    @Override
    public void onRelations(List<BookCircleDynamicRelationMe> dynamics) {
        if (dynamics == null || dynamics.isEmpty()) {
            Log.d("bCircle", "没有更多了");
            return;
        }
        loading = false;
        if (rmbcdAdapter != null) {
            if (currentPage == 0) {
                rmbcdAdapter.clearData();
            }
            rmbcdAdapter.addItems(dynamics);
        }
    }

    @Override
    public void onItemClick(View v, int type, BookCircleDynamic bcd, DynamicReply dy) {

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
            if (dy > 0) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
                        .findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                if (dy > 0 && (lastPosition + 1 >= total) && !loading) {
                    loading = true;
                    if (type == 0) {
                        presenter.myPublished(++currentPage);
                    } else {
                        presenter.myRelations(++currentPage);
                    }
                }
            } else {

            }
        }
    };
}

package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Toast;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.NotebookAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Notebook;
import com.culturebud.contract.NotebookContract;
import com.culturebud.presenter.NotebookPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/11/20.
 */

@PresenterInject(NotebookPresenter.class)
public class NotebookActivity extends BaseActivity<NotebookContract.Presenter> implements NotebookContract.View, NotebookAdapter.OnItemClickListener {
    private static final int REQUEST_CREATE_NOTEBOOK = 201;
    private RecyclerView rvNotebooks;
    private int currentPage;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.setView(this);
        rvNotebooks = new RecyclerView(this);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        rvNotebooks.setLayoutParams(params);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvNotebooks.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvNotebooks.addItemDecoration(divider);
        NotebookAdapter adapter = new NotebookAdapter();
        adapter.setOnItemClickListener(this);
        rvNotebooks.setAdapter(adapter);
        rvNotebooks.addOnScrollListener(onScrollListener);
        setContentView(rvNotebooks);
        showTitlebar();
        setTitle("笔记");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notebook_operas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notebook_create:
                startActivityForResult(new Intent(this, CreateNotebookActivity.class), REQUEST_CREATE_NOTEBOOK);
                return true;
            case R.id.menu_notebook_managment:
                Toast.makeText(this, "管理", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.myNotebooks(currentPage);
    }

    @Override
    public void onNotebooks(List<Notebook> notebooks) {
        if (currentPage == 0) {
            ((NotebookAdapter) rvNotebooks.getAdapter()).clearData();
        }
        ((NotebookAdapter) rvNotebooks.getAdapter()).addItems(notebooks);
        loading = false;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);

    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CREATE_NOTEBOOK:
                if (resultCode == RESULT_OK) {
                    currentPage = 0;
                    presenter.myNotebooks(currentPage);
                }
                break;
        }
    }

    private OnScrollListener onScrollListener = new OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);//接口暂没有实现分页
//            if (dy > 0) {
//                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
//                int total = recyclerView.getLayoutManager().getItemCount();
//                if (!loading && (lastPosition + 1 >= total)) {
//                    presenter.myNotebooks(++currentPage);
//                    loading = true;
//                }
//            } else {
//
//            }
        }
    };

    @Override
    public void onItemClick(int position, View v, Notebook notebook) {
        Intent intent = new Intent(this, NotebookDetailActivity.class);
        intent.putExtra("notebookId", notebook.getNotebookId());
        startActivity(intent);
    }
}

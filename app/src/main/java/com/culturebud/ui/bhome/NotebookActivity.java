package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.culturebud.adapter.CollectedBooksAdapter;
import com.culturebud.adapter.NotebookAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Notebook;
import com.culturebud.contract.NotebookContract;
import com.culturebud.presenter.NotebookPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CREATE_NOTEBOOK;

/**
 * Created by XieWei on 2016/11/20.
 */

@PresenterInject(NotebookPresenter.class)
public class NotebookActivity extends BaseActivity<NotebookContract.Presenter> implements NotebookContract.View,
        NotebookAdapter.OnItemClickListener {
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
        setTitle(R.string.note);
        showOperas();
        setOperasDrawable(R.drawable.titlebar_add_selector);
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        startActivityForResult(new Intent(this, CreateNotebookActivity.class), REQUEST_CREATE_NOTEBOOK);
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
    public void onDeleteNotebook(Notebook notebook, boolean success) {
        if (success) {
            ((NotebookAdapter) rvNotebooks.getAdapter()).deleteItem(notebook);
        }
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
//                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager())
// .findLastVisibleItemPosition();
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
    public void onItemClick(int position, View v, Notebook notebook, int operaType) {
        switch (operaType) {
            case 0:
                Intent intent = new Intent(this, NotebookDetailActivity.class);
                intent.putExtra("notebookId", notebook.getNotebookId());
                startActivity(intent);
                break;
            case 1://删除
                new AlertDialog.Builder(this).setMessage("确定删除《" + notebook.getTitle() + "》下的所有笔记？")
                        .setPositiveButton("删除", (dialog, which) -> {
                            setOperasText(null);
                            setOperasDrawable(R.drawable.titlebar_add_selector);
                            presenter.deleteNotebook(notebook);
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
        }

    }
}

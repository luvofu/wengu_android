package com.culturebud.ui.bhome;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.adapter.NotebookAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Notebook;
import com.culturebud.bean.User;
import com.culturebud.contract.NotebookContract;
import com.culturebud.presenter.NotebookPresenter;
import com.culturebud.ui.search.SelectBookActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SELECT_BOOK;

/**
 * Created by XieWei on 2016/11/20.
 */

@PresenterInject(NotebookPresenter.class)
public class NotebookActivity extends BaseActivity<NotebookContract.Presenter> implements NotebookContract.View,
        NotebookAdapter.OnItemClickListener {
    private RecyclerView rvNotebooks;
    private int currentPage;
    private boolean loading = true;
    private long userId;

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
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        Intent intent = new Intent(this, SelectBookActivity.class);
        startActivityForResult(intent, REQUEST_CODE_SELECT_BOOK);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        User user = BaseApp.getInstance().getUser();
        long defaultId = user != null ? user.getUserId() : -1;
        userId = intent.getLongExtra("user_id", defaultId);

        boolean isMe = BaseApp.getInstance().isMe(userId);
        if (isMe) {
            setOperasDrawable(R.drawable.titlebar_add_selector);
            showOperas();
        } else {
            hideOpears();
        }
        ((NotebookAdapter) rvNotebooks.getAdapter()).setMe(isMe);
        presenter.userNotebooks(currentPage, userId);
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
    public void onCreateNotebook(boolean res, long bookId) {
        if (res) {
            //笔记本创建成功，需要刷新数据.
            currentPage = 0;
            presenter.userNotebooks(currentPage, userId);
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
//            case REQUEST_CREATE_NOTEBOOK:
//                if (resultCode == RESULT_OK) {
//                    currentPage = 0;
//                    presenter.userNotebooks(currentPage, userId);
//                }
//                break;
            case REQUEST_CODE_SELECT_BOOK:
                //选择一本书后开始创建笔记本.
                if (resultCode == RESULT_OK) {
                    long bookId = data.getLongExtra("book_id", -1);
                    if (bookId != -1) {
                        //获取到bookid，调用创建API.
                        presenter.createNotebook(bookId);
                    }
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
//                    presenter.userNotebooks(++currentPage);
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
                intent.putExtra("user_id", userId);
                startActivity(intent);
                break;
            case 1://删除
                new AlertDialog.Builder(this).setMessage("确定删除《" + notebook.getTitle() + "》下的所有笔记？")
                        .setPositiveButton(R.string.delete, (dialog, which) -> {
                            setOperasText(null);
                            setOperasDrawable(R.drawable.titlebar_add_selector);
                            presenter.deleteNotebook(notebook);
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                break;
        }

    }
}

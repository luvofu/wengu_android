package com.culturebud.ui.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.UsersAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.UserSearchContract;
import com.culturebud.presenter.UserSearchPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/12/26.
 */

@PresenterInject(UserSearchPresenter.class)
public class SearchUserActivity extends BaseActivity<UserSearchContract.Presenter>
        implements UserSearchContract.View, TextView.OnEditorActionListener {
    private RecyclerView rvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rvUsers = new RecyclerView(this);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvUsers.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvUsers.addItemDecoration(divider);
        setContentView(rvUsers);
        presenter.setView(this);

        showTitlebar();
        hideBack();
        showOperas();
        setOperasText(R.string.cancel);

        int padding = getResources().getDimensionPixelSize(R.dimen.common_padding_middle);
        rvUsers.setPadding(padding, padding, padding, padding);
        rvUsers.setAdapter(new UsersAdapter());

        enableSearch();
        setOnTitleEditorActionListener(this);
        setSearchHint(R.string.search_user_hint);
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        finish();
    }

    @Override
    public void onUsers(List<User> users) {
        ((UsersAdapter) rvUsers.getAdapter()).addItems(users);
    }

    @Override
    public void onClearOldData() {
        ((UsersAdapter) rvUsers.getAdapter()).clearData();
    }

    private String inputContent;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        inputContent = getInputContent().toString();
        presenter.search(inputContent, 0);
        return true;
    }
}

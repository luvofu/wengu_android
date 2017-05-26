package com.culturebud.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.adapter.FriendsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Friend;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFriendsContract;
import com.culturebud.presenter.MyFriendsPresenter;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by XieWei on 2017/1/12.
 */

@PresenterInject(MyFriendsPresenter.class)
public class SelectUserActivity extends BaseActivity<MyFriendsContract.Presenter>
        implements MyFriendsContract.View, FriendsAdapter.OnItemClickListener {
    private RecyclerView rvUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rvUsers = new RecyclerView(this);
        rvUsers.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        rvUsers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvUsers.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL));
        setContentView(rvUsers);
        presenter.setView(this);
        showTitlebar();
        showBack();
        setBackText(R.string.cancel);
        setTitle(R.string.select_friend);
        FriendsAdapter adapter = new FriendsAdapter(false);
        adapter.setOnItemClickListener(this);
        rvUsers.setAdapter(adapter);
        User user = BaseApp.getInstance().getUser();
        long userId = user != null ? user.getUserId() : -1;
        presenter.friends(true, userId);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onFriends(List<Friend> friends) {
        ((FriendsAdapter) rvUsers.getAdapter()).addItems(friends);
    }

    @Override
    public void onConcern(Friend friend) {

    }

    @Override
    public void onItemClick(View v, Friend friend, int opt) {
        Intent intent = new Intent();
        intent.putExtra("user", new Gson().toJson(friend));
        setResult(RESULT_OK, intent);
        finish();
    }
}

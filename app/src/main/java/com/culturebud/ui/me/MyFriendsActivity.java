package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.R;
import com.culturebud.adapter.FriendsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Friend;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFriendsContract;
import com.culturebud.presenter.MyFriendsPresenter;
import com.culturebud.ui.search.SearchUserActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/12/5.
 */

@PresenterInject(MyFriendsPresenter.class)
public class MyFriendsActivity extends BaseActivity<MyFriendsContract.Presenter> implements MyFriendsContract.View, FriendsAdapter.OnItemClickListener {
    private RecyclerView rvFriends;
    private List<Friend> friends = new ArrayList<>();
    private boolean isConcern = false;
    private long userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);
        presenter.setView(this);
        showTitlebar();

        setOperasDrawable(R.drawable.btn_add_friend_selector);

        setNoDataView(R.id.main_multiplestatusview);

        Intent intent = getIntent();
        isConcern = intent.getBooleanExtra("is_concern", false);
        User user = BaseApp.getInstance().getUser();
        long defUserId = user != null ? user.getUserId() : -1;
        userId = intent.getLongExtra("user_Id", defUserId);
        String title = intent.getStringExtra("title");
        setTitle(title != null ? title : "");


        rvFriends = obtainViewById(R.id.rv_friends);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFriends.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvFriends.addItemDecoration(divider);
        FriendsAdapter adapter = new FriendsAdapter(true);
        rvFriends.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        presenter.friends(isConcern, userId);
    }

    @Override
    public void onRetryData() {
        presenter.friends(isConcern, userId);
    }

    @Override
    protected void onBack() {
        super.onBack();
        finish();
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        startActivity(new Intent(this, SearchUserActivity.class));
    }

    @Override
    public void onFriends(List<Friend> friends) {
        if (friends != null && !friends.isEmpty()) {
            this.friends.addAll(friends);
        }
        ((FriendsAdapter) rvFriends.getAdapter()).addItems(friends);
    }

    @Override
    public void onConcern(Friend friend) {
        //关注状态：未相互关注NOEachConcern(0), 单向关注SingleConcern(1), 单向被关注SingleBeConcerned(2), 互相关注MutualConcern(3);
        switch (friend.getConcernStatus()) {
            case 0:
                friend.setConcernStatus(1);
                break;
            case 1:
                friend.setConcernStatus(0);
                break;
            case 2:
                friend.setConcernStatus(3);
                break;
            case 3:
                friend.setConcernStatus(2);
                break;
        }
        rvFriends.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, Friend friend, int opt) {
        switch (opt) {
            case 0: {
                Intent intent = new Intent(this, FriendDetailActivity.class);
                intent.putExtra("user_id", friend.getUserId());
                startActivity(intent);
                break;
            }
            case 1: {
                presenter.concern(friend);
            }
        }
    }
}

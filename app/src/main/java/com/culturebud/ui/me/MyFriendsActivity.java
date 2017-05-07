package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.MyFriendsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFriendsContract;
import com.culturebud.presenter.MyFriendsPresenter;
import com.culturebud.ui.search.SearchUserActivity;
import com.culturebud.widget.IndexsView;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_SEARCH_USER;
import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_USER_PROFILE;

/**
 * Created by XieWei on 2016/12/5.
 */

@PresenterInject(MyFriendsPresenter.class)
public class MyFriendsActivity extends BaseActivity<MyFriendsContract.Presenter> implements MyFriendsContract.View, IndexsView.OnIndexChangedListener, MyFriendsAdapter.OnItemClickListener {
    private RecyclerView rvFriends;
    private IndexsView ivIndexs;
    private List<User> friends = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);
        presenter.setView(this);
        showTitlebar();
        setTitle(R.string.add_book_friend);
        setOperasDrawable(R.drawable.btn_add_friend_selector);

        rvFriends = obtainViewById(R.id.rv_friends);
        ivIndexs = obtainViewById(R.id.iv_indexs);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFriends.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvFriends.addItemDecoration(divider);
        MyFriendsAdapter adapter = new MyFriendsAdapter();
        rvFriends.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        ivIndexs.setOnIndexChangedListener(this);

        presenter.myFriends();
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
    protected void onOptions(View view) {
        super.onOptions(view);
        startActivityForResult(new Intent(this, SearchUserActivity.class), REQUEST_CODE_SEARCH_USER);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFriends(List<User> friends) {
        if (friends != null && !friends.isEmpty()) {
            this.friends.addAll(friends);
        }
        ((MyFriendsAdapter) rvFriends.getAdapter()).addItems(friends);
        List<String> indexs = ((MyFriendsAdapter) rvFriends.getAdapter()).getIndexs();
        ivIndexs.setIndexs(indexs);
    }

    @Override
    public void onIndexChanged(int index, String content) {
//        onErrorTip("index = " + index + ", char is " + content);
        int position = ((MyFriendsAdapter) rvFriends.getAdapter()).indexAlph(content);
        if (position >= 0 && position < rvFriends.getAdapter().getItemCount() - 1) {
            rvFriends.smoothScrollToPosition(position);
        }
    }

    @Override
    public void onItemClick(int position, View v, Object item) {
        if (item instanceof User) {
            User user = (User) item;
            Intent intent = new Intent(this, FriendDetailActivity.class);
            intent.putExtra("user_id", user.getUserId());
            startActivityForResult(intent, REQUEST_CODE_USER_PROFILE);
        } else if (item instanceof Integer) {
            if (friends.isEmpty()) {
                return;
            }
            Intent intent = new Intent(this, SearchUserActivity.class);
            intent.putExtra("search_in_local", true);
            intent.putExtra("friends", new Gson().toJson(friends));
            startActivityForResult(intent, REQUEST_CODE_SEARCH_USER);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

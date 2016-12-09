package com.culturebud.ui.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.MyFriendsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.User;
import com.culturebud.contract.MyFriendsContract;
import com.culturebud.presenter.MyFriendsPresenter;
import com.culturebud.widget.IndexsView;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/12/5.
 */

@PresenterInject(MyFriendsPresenter.class)
public class MyFriendsActivity extends BaseActivity<MyFriendsContract.Presenter> implements MyFriendsContract.View {
    private RecyclerView rvFriends;
    private IndexsView ivIndexs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);
        presenter.setView(this);
        showTitlebar();
        setTitle("添加书友");
        setOperasDrawable(R.drawable.btn_add_friend_selector);

        rvFriends = obtainViewById(R.id.rv_friends);
        ivIndexs = obtainViewById(R.id.iv_indexs);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFriends.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvFriends.addItemDecoration(divider);
        rvFriends.setAdapter(new MyFriendsAdapter());

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
    public void onFriends(List<User> friends) {
        ((MyFriendsAdapter) rvFriends.getAdapter()).addItems(friends);
        List<String> indexs = ((MyFriendsAdapter) rvFriends.getAdapter()).getIndexs();
        ivIndexs.setIndexs(indexs);
    }
}

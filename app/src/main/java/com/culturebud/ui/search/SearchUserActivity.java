package com.culturebud.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.culturebud.BaseActivity;
import com.culturebud.R;
import com.culturebud.adapter.FriendsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Friend;
import com.culturebud.contract.UserSearchContract;
import com.culturebud.presenter.UserSearchPresenter;
import com.culturebud.ui.bhome.UserBookHomeActivity;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

import static com.culturebud.CommonConst.RequestCode.REQUEST_CODE_USER_PROFILE;

/**
 * Created by XieWei on 2016/12/26.
 */

@PresenterInject(UserSearchPresenter.class)
public class SearchUserActivity extends BaseActivity<UserSearchContract.Presenter>
        implements UserSearchContract.View, TextView.OnEditorActionListener, FriendsAdapter.OnItemClickListener {
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

        int padding = getResources().getDimensionPixelSize(R.dimen.common_padding_big);
        rvUsers.setPadding(padding, padding, padding, padding);
        FriendsAdapter adapter = new FriendsAdapter(false);
        rvUsers.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        rvUsers.setOnScrollListener(listener);

        enableSearch();
        setSearchInputType(InputType.TYPE_CLASS_TEXT);
        setOnTitleEditorActionListener(this);
        setSearchHint(R.string.search_user_hint);
    }

    @Override
    protected void onOptions(View view) {
        super.onOptions(view);
        finish();
    }

    @Override
    public void onUsers(List<Friend> friends) {
        if (friends.size() > 0) {
            loading = false;
            if (currentPage == 0) {
                ((FriendsAdapter) rvUsers.getAdapter()).clearData();
            }
            ((FriendsAdapter) rvUsers.getAdapter()).addItems(friends);
        }
    }

    private String inputContent;
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String newInput = getInputContent().toString();
        if (!newInput.equals(inputContent)) {
            inputContent = newInput;
            loading = false;
            presenter.search(inputContent, currentPage = 0);
        }
        return true;
    }

    @Override
    public void onItemClick(View v, Friend friend, int opt) {
        Intent intent = new Intent(this, UserBookHomeActivity.class);
        intent.putExtra("user_id", friend.getUserId());
        startActivityForResult(intent, REQUEST_CODE_USER_PROFILE);
    }

    private int currentPage;
    private boolean loading = true;
    private RecyclerView.OnScrollListener listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (dy > 0 && !loading) {
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int total = recyclerView.getLayoutManager().getItemCount();
                if (lastPosition + 1 >= total) {
                    loading = true;
                    presenter.search(inputContent, ++currentPage);
                }
            }
        }
    };
}

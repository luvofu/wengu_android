package com.culturebud.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.BaseApp;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.FriendsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.Friend;
import com.culturebud.bean.User;
import com.culturebud.contract.FriendsContract;
import com.culturebud.presenter.FriendsPresenter;
import com.culturebud.ui.bhome.UserBookHomeActivity;
import com.culturebud.ui.search.SearchUserActivity;
import com.culturebud.widget.RecyclerViewDivider;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieWei on 2016/12/5.
 */

@PresenterInject(FriendsPresenter.class)
public class FriendsActivity extends BaseActivity<FriendsContract.Presenter> implements FriendsContract.View, FriendsAdapter.OnItemClickListener {
    private RecyclerView rvFriends;
    private List<Friend> friends = new ArrayList<>();
    private boolean isConcern = false;
    private boolean hasItemOpt = false;
    private int optType = 0;
    private long userId = -1;

    public final int OPT_TYPE_USER_FRIEND = 0;//用户关注、粉丝
    public final int OPT_TYPE_SELECT_FRIEND = 1;//选择关注

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_friends);
        presenter.setView(this);
        showTitlebar();
        setNoDataView(R.id.main_multiplestatusview);

        Intent intent = getIntent();

        User user = BaseApp.getInstance().getUser();
        long defUserId = user != null ? user.getUserId() : -1;
        userId = intent.getLongExtra("user_Id", defUserId);

        optType = intent.getIntExtra("opt_type", 0);

        if (optType == 0) {
            String title = intent.getStringExtra("title");
            setTitle(title != null ? title : "");
            setOperasDrawable(R.drawable.btn_add_friend_selector);
            isConcern = intent.getBooleanExtra("is_concern", false);
            hasItemOpt = true;
        } else {
            setBackText(R.string.cancel);
            setTitle(R.string.select_friend);
            isConcern = true;
            hasItemOpt = false;
        }

        rvFriends = obtainViewById(R.id.rv_friends);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFriends.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvFriends.addItemDecoration(divider);
        FriendsAdapter adapter = new FriendsAdapter(hasItemOpt);
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
    public void onConcern(Friend friend, long concernNum, long fanNum, int status) {
        friend.setConcernStatus(status);
        switch (status) {
            case CommonConst.ConcernStatus.NO_EACHCONCERN_STATUS:
            case CommonConst.ConcernStatus.SINGLE_BECONVERNED_STATUS:
                friend.setFanNum(friend.getFanNum() - 1);
                break;
            case CommonConst.ConcernStatus.SINGLE_CONCERN_STATUS:
            case CommonConst.ConcernStatus.EACH_CONCERN_STATUS:
                friend.setFanNum(friend.getFanNum() + 1);
                break;
        }
        User user = BaseApp.getInstance().getUser();
        if (user != null) {
            user.setConcernNum(concernNum);
            user.setFanNum(fanNum);
        }
        rvFriends.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View v, Friend friend, int opt) {
        switch (opt) {
            case 0: {
                if (optType == 0) {
                    Intent intent = new Intent(this, UserBookHomeActivity.class);
                    intent.putExtra("user_id", friend.getUserId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("user", new Gson().toJson(friend));
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            }
            case 1: {
                switch (friend.getConcernStatus()) {
                    case CommonConst.ConcernStatus.NO_EACHCONCERN_STATUS:
                    case CommonConst.ConcernStatus.SINGLE_BECONVERNED_STATUS:
                        presenter.concern(friend);
                        break;
                    case CommonConst.ConcernStatus.SINGLE_CONCERN_STATUS:
                    case CommonConst.ConcernStatus.EACH_CONCERN_STATUS:
                        new AlertDialog.Builder(this).setMessage(getString(R.string.cancel_concern_notice))
                                .setPositiveButton(R.string.confirm, (dialog, which) ->
                                        presenter.concern(friend)).setNegativeButton(R.string.cancel, null).show();
                        break;
                }
            }
        }
    }
}

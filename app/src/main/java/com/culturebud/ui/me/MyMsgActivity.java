package com.culturebud.ui.me;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.culturebud.BaseActivity;
import com.culturebud.CommonConst;
import com.culturebud.R;
import com.culturebud.adapter.MyMsgsAdapter;
import com.culturebud.annotation.PresenterInject;
import com.culturebud.bean.UserMessage;
import com.culturebud.contract.MyMsgsContract;
import com.culturebud.presenter.MyMsgsPresenter;
import com.culturebud.widget.RecyclerViewDivider;

import java.util.List;

/**
 * Created by XieWei on 2016/11/16.
 */

@PresenterInject(MyMsgsPresenter.class)
public class MyMsgActivity extends BaseActivity<MyMsgsContract.Presenter>
        implements MyMsgsContract.View, MyMsgsAdapter.OnAgreeListener, MyMsgsAdapter.OnItemClickListener {
    private static final String TAG = MyMsgActivity.class.getSimpleName();
    private RecyclerView rvMyMsgs;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_msg);
        showTitlebar();
        presenter.setView(this);
        setTitle(R.string.my_msg);
        rvMyMsgs = obtainViewById(R.id.rv_my_msgs);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvMyMsgs.setLayoutManager(llm);
        RecyclerViewDivider divider = new RecyclerViewDivider(this, LinearLayoutManager.HORIZONTAL);
        rvMyMsgs.addItemDecoration(divider);
        MyMsgsAdapter adapter = new MyMsgsAdapter();
        adapter.setOnAgreeListener(this);
        adapter.setOnItemClickListener(this);
        rvMyMsgs.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = 0;
        presenter.getInviteMsgs(0);
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
    protected void onBack() {
        super.onBack();

        finish();
    }

    @Override
    public void onInviteMsgs(List<UserMessage> msgs) {
        if (currentPage == 0) {
            ((MyMsgsAdapter) rvMyMsgs.getAdapter()).clearData();
        }
        ((MyMsgsAdapter) rvMyMsgs.getAdapter()).addItems(msgs);
    }

    @Override
    public void onAgreeInvite(long messageId, boolean success) {
        if (success) {
            ((MyMsgsAdapter) rvMyMsgs.getAdapter()).updateItemStatus(messageId, CommonConst.MessageDealStatus
                    .STATUS_ACCEPT);
        }
    }


    @Override
    public void onAgree(View v, UserMessage userMessage) {
        presenter.agreeInvite(userMessage.getMessageId());
    }

    @Override
    public void onDeleteUserMessage(UserMessage userMessage, boolean success) {
        if (success) {
            ((MyMsgsAdapter) rvMyMsgs.getAdapter()).deleteItem(userMessage);
        }
    }

    @Override
    public void onItemClick(int position, View v, UserMessage userMessage, int operaType) {
        switch (operaType) {
            case 0://msg detail
                break;
            case 1://删除
                presenter.deleteUserMessage(userMessage);
                break;
        }
    }

}
